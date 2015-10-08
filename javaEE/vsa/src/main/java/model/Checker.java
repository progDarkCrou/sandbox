package model;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by crou on 07.10.15.
 */
public class Checker {

    private static long count = 0;

    private static String invalidAttemptPattern = ".*?Invalid.*?attempt.*?";
    private static String successResultPattern = ".*[0-9]{1,2}..+?.[0-9]{1,4}.*";
    private static String searchElementId = "#ctl00_plhMain_lblMsg";
    private static int maxFails = 20;

    private static long latency = 5000;

    private Logger logger;
    private String name;
    private long id;

    private String data;

    private String url;

    private String referer;
    private RegistredPerson person;
    private Thread runner;
    private ArrayList<CheckResult> results = new ArrayList<>();

    @Autowired
    private MailSender mailSender;
    @Autowired
    private SimpleMailMessage templateMessage;

    public Checker(String name, String data, String url, String referer, RegistredPerson person) {
        this.id = count++;
        this.name = name;
        this.data = data;
        this.url = url;
        this.referer = referer;
        this.person = person;
        this.runner = new Thread(this::run, this.name);
        runner.start();
    }

    public Checker(String name, String data, String url, String referer, String nameToSend, String emailToSend) {
        this(name, data, url, referer, new RegistredPerson(nameToSend, emailToSend));
    }

    public Checker(String data, String url, String referer, String nameToSend, String emailToSend) {
        this(Checker.class.getName() + "-" + count, data, url, referer, nameToSend, emailToSend);
    }

    private Runnable run() {
        return () -> {
            int fails = 0;
            logger.info(this.name + " - started.");

            while (!Thread.currentThread().isInterrupted()) {
                CheckResult checkResult = null;
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
                        checkResult = new CheckResult(this.name + " - Invalid attempt was find. Please re-init the checker.",
                                CheckResult.CheckStatus.RESULT_ERROR_CRITICAL);
                        logger.error(checkResult.getMessage());
                        this.stop();
                        break;
                    }

                    Elements searchElements = document.select(searchElementId);

                    if (searchElements == null || searchElements.size() == 0) {
                        String message = this.name + " - cannot find proper element to check. Please check it out, " +
                                "and, if needed, reinitialize checker.";
                        logger.error(message);
                        throw new IOException(message);
                    }

                    Element searchElement = searchElements.first();

                    if (searchElement.html().matches(successResultPattern)) {
                        checkResult = new CheckResult("Success check result: " + searchElement.html(),
                                CheckResult.CheckStatus.RESULT_SUCCESS);
                        logger.info(checkResult.getMessage());
                    } else {
                        logger.info(this.name + " - result: " + searchElement.html());
                    }

                    if (checkResult != null) {
                        this.results.add(checkResult);
                    }

                    fails = 0;

                    Thread.currentThread().sleep(latency);
                } catch (IOException e) {
                    if (fails >= maxFails) {
                        checkResult = new CheckResult("Max fails count was reached. Please reinitialize checker.",
                                CheckResult.CheckStatus.RESULT_ERROR_CRITICAL);
                        this.results.add(checkResult);
                        logger.error(checkResult.getMessage());
                        this.stop();
                        break;
                    }
                    fails++;
                    logger.error(this.name + " - some connectivity error occurred.", e);
                    continue;
                } catch (InterruptedException e) {
                    break;
                } finally {
                    try {
                        if (inStream != null) {
                            inStream.close();
                        }
                        if (outStream != null) {
                            outStream.close();
                        }
                    } catch (IOException e) {
                        logger.error(this.name + " - something happened while closing connection.", e);
                    }
                }
            }
        };
    }

    public boolean stop() {
        runner.interrupt();
        logger.info(this.name + " - halted.");
        return !runner.isAlive();
    }

    public boolean isRunning() {
        return runner.isAlive();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<CheckResult> getResults() {
        return results;
    }
}
