package services;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import services.dto.CheckResult;
import util.InstallCert;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertPathBuilderException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by avorona on 05.10.15.
 */
@Service
@Scope("singleton")
public class CheckRunner {

    private Logger logger = Logger.getLogger(CheckRunner.class.getName());

    private int maxFails = 20;
    private long latency = 5000;

    private Map<String, ConcurrentLinkedDeque<CheckResult>> results = new HashMap<>();
    private Map<String, Thread> checkersPool = new HashMap<>();
    private String defaultCheckerNameBase = "checker-";
    private String searchElementId = "#ctl00_plhMain_lblMsg";
    private String invalidAttemptPattern = ".*?Invalid.*?attempt.*?";

    @Autowired
    private MailSender mailSender;
    @Autowired
    private SimpleMailMessage templateMessage;

    public String run(String url, String referer, String data) {
        String checkerName = defaultCheckerNameBase + checkersPool.size();
        ConcurrentLinkedDeque<CheckResult> checkerResults = new ConcurrentLinkedDeque<>();
        results.put(checkerName, checkerResults);

        Thread checker = new Thread(() -> {
            int fails = 0;
            logger.info("Checker started: " + checkerName);

            while (!Thread.currentThread().isInterrupted()) {
                DataOutputStream outStream = null;
                BufferedReader inStream = null;

                try {
                    HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Connection", "keep-alive");
                    connection.setRequestProperty("Referer", referer);

                    connection.setDoOutput(true);

                    outStream = new DataOutputStream(connection.getOutputStream());

                    outStream.writeBytes(data);
                    outStream.flush();
                    outStream.close();

                    inStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    Document document = Jsoup.parse(inStream.lines().reduce((s, s2) -> s + s2).get());

                    if (document.html().matches(invalidAttemptPattern)) {
                        String message = "Invalid attempt was find. Please reinitialize checker: " + checkerName;
                        checkerResults.add(new CheckResult(message));
                        logger.error(message);

                        SimpleMailMessage mail = new SimpleMailMessage(templateMessage);
                        mail.setTo("dark.crou@gmail.com");
                        mail.setText(message);
                        mail.setSubject("Invalid check attempt.");
                        mailSender.send(mail);

                        this.stop(checkerName);
                        break;
                    }

                    Elements searchElements = document.select(searchElementId);

                    if (searchElements == null || searchElements.size() == 0) {
                        String message = "Cannot find proper element to check. Please check it out, " +
                                "and, if needed, reinitialize checker: " + checkerName;
                        checkerResults.add(new CheckResult(message));
                        logger.error(message);
                        throw new IOException(message);
                    }

                    Element searchElement = searchElements.first();

                    if (searchElement.html().matches(".*[0-9]{1,2}..+?.[0-9]{2,4}.*")) {
                        checkerResults.add(new CheckResult(searchElement.html()));
                        logger.info("Success check result: " + searchElement.html());
                    } else {
                        logger.info("Checker \"" + checkerName + "\" result: " + searchElement.html());
                    }

                    fails = 0;

                    Thread.currentThread().sleep(latency);
                } catch (IOException e) {
                    if (fails >= maxFails) {
                        logger.error("Max fails count was reached. Please reinitialize checker.");
                        stop(checkerName);
                        break;
                    }
                    fails++;
                    logger.error("Some connectivity error occurred.", e);
                    continue;
                } catch (InterruptedException e) {
                    logger.error("Checker stopped: " + checkerName, e);
                } finally {
                    try {
                        if (inStream != null) {
                            inStream.close();
                        }
                        if (outStream != null) {
                            outStream.close();
                        }
                    } catch (IOException e) {
                        logger.error("Something happened while closing connection.", e);
                    }
                }
            }
        });
        checkersPool.put(checkerName, checker);
        checker.setName(checkerName);
        checker.start();
        return checker.isAlive() && checkersPool.containsKey(checkerName) ? checkerName : null;
    }

    public boolean stop(String checkerName) {
        if (checkersPool != null && checkersPool.containsKey(checkerName)) {
            Thread thread = checkersPool.get(checkerName);
            thread.interrupt();
            checkersPool.remove(checkerName);
            return !thread.isAlive() && !checkersPool.containsKey(checkerName);
        }

        logger.error("There is no checker with name \"" + checkerName + "\" to stop.");
        return false;
    }

    public boolean stopAll() {
        logger.error("Stopping all checkers.");
        return checkersPool.entrySet()
                .stream()
                .map(checkerEntry -> this.stop(checkerEntry.getKey()))
                .reduce((a, b) -> a && b)
                .get();
    }

}
