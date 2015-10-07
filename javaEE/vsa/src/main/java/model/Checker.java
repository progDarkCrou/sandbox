package model;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.mail.SimpleMailMessage;
import services.dto.CheckResult;
import sun.rmi.runtime.Log;

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

    private Logger logger;

    private String name;
    private long id;

    private String data;
    private String url;
    private String referer;

    private RegistredPerson person;

    private Thread runner;

    private ArrayList<CheckResult> checkResults = new ArrayList<>();

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
                        String message = this.name + " - Invalid attempt was find. Please re-init the checker.";
                        this.checkResults.add(new CheckResult(message));
                        logger.error(message);

                        SimpleMailMessage mail = new SimpleMailMessage(templateMessage);
                        mail.setTo("dark.crou@gmail.com");
                        mail.setText(message);
                        mail.setSubject("Invalid check attempt.");
                        mailSender.send(mail);

                        this.stop();
                        break;
                    }

                    Elements searchElements = document.select(searchElementId);

                    if (searchElements == null || searchElements.size() == 0) {
                        String message = "Cannot find proper element to check. Please check it out, " +
                                "and, if needed, reinitialize checker: " + checkerName;
                        this.checkResults.add(new CheckResult(message));
                        logger.error(message);
                        throw new IOException(message);
                    }

                    Element searchElement = searchElements.first();

                    if (searchElement.html().matches(successResultPattern)) {
                        this.checkResults.add(new CheckResult(searchElement.html()));
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
        };
    }

}
