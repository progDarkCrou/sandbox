package com.vsa.checker.model;

import com.vsa.checker.services.ResultMailSender;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by crou on 07.10.15.
 */
public class Checker {

    private static long count = 0;

    private static String invalidAttemptPattern = ".*?Invalid.*?attempt.*?";
    private static String successResultPattern = ".*[0-9]{1,2}..+?.[0-9]{1,4}.*";
    private static String searchElementId = "#ctl00_plhMain_lblMsg";
    private static int maxFails = 20;

    private static long maxLatency = 60000;
    private static long latency = 5000;
    private static long minLatency = 1000;
    private static float latensyVatiationStep = 0.2f;

    private Logger logger;
    private String name;
    private Long id;
    private LocalDate startTime;

    private String data;
    private String url;
    private String referer;

    private RegisteredPerson person;
    private Thread runner;
    private ConcurrentLinkedDeque<CheckerResult> results = new ConcurrentLinkedDeque<>();

    private ResultMailSender resultMailSender;

    public Checker(String data, String url, String referer) {
        this.id = count++;
        this.name = Checker.class.getSimpleName() + "-" + count;
        this.data = data;
        this.url = url;
        this.referer = referer;
        this.logger = Logger.getLogger(this.name);
        this.runner = new Thread(this::run, this.name);
        runner.start();
    }

    private void run() {
        int fails = 0;
        logger.info("Started");

        this.startTime = LocalDate.now();

        while (!Thread.currentThread().isInterrupted()) {
            CheckerResult checkerResult = null;
            DataOutputStream outStream = null;
            BufferedReader inStream = null;

            try {
                HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                    connection.setRequestProperty("Connection", "keep-alive");
                connection.setRequestProperty("Referer", referer);

                connection.setDoOutput(true);
                connection.setDoInput(true);

                outStream = new DataOutputStream(connection.getOutputStream());

                outStream.writeBytes(data);
                outStream.flush();
                outStream.close();

                inStream = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

                Document document = Jsoup.parse(inStream.lines().reduce((s, s2) -> s + s2).get());

                if (document.html().matches(invalidAttemptPattern)) {
                    checkerResult = new CheckerResult("Invalid attempt was find. Please re-init the checker.",
                            CheckerResult.CheckStatus.RESULT_ERROR_CRITICAL);
                    logger.error(checkerResult.getMessage());
                    this.resultMailSender.sendFatalError(checkerResult);
                    this.stop();
                    break;
                }

                Elements searchElements = document.select(searchElementId);

                if (searchElements == null || searchElements.size() == 0) {
                    String message = "Cannot find proper element to check. Please check it out, " +
                            "and, if needed, reinitialize checker.";
                    logger.error(message);
                    throw new IOException(message);
                }

                Element searchElement = searchElements.first();

                if (searchElement.html().matches(successResultPattern)) {
                    checkerResult = new CheckerResult("Success check result: " + searchElement.html(),
                            CheckerResult.CheckStatus.RESULT_SUCCESS);
                    this.resultMailSender.sendSuccess(checkerResult);
                    logger.info(checkerResult.getMessage());
                } else {
                    logger.info("Result: " + searchElement.html());
                }

                fails = 0;
            } catch (IOException e) {
                fails++;

                this.latencyIncrement();

                logger.error("Some connectivity error occurred.\n\t\t\t" + e.getMessage());
                continue;
            } finally {
                if (fails >= maxFails) {
                    checkerResult = new CheckerResult("Max fails count was reached. Please reinitialize checker.",
                            CheckerResult.CheckStatus.RESULT_ERROR_CRITICAL);
                    this.results.add(checkerResult);
                    logger.error(checkerResult.getMessage());
                    this.resultMailSender.sendError(checkerResult);
                    this.stop();
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(latency);
                } catch (InterruptedException e) {
                }
                if (checkerResult != null) {
                    this.results.add(checkerResult);
                    if (checkerResult.getCheckerStatus().equals(CheckerResult.CheckStatus.RESULT_SUCCESS)) {
                        this.latencyIncrement();
                    } else {
                        this.latencyDecrement();
                    }
                }
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

        logger.info("Stopped");
        Period deltaWorkTime = Period.between(this.startTime, LocalDate.now());
        logger.info("Worked for: " + deltaWorkTime.get(ChronoUnit.HOURS) + " hours and " + deltaWorkTime.get(ChronoUnit.MINUTES));
        String message = this.name + ": stopped";
        this.results.add(new CheckerResult(message, CheckerResult.CheckStatus.RESULT_STOPED));
        this.resultMailSender.send(message, ResultMailSender.MessagesSubject.stopCheckerHeader);
    }

    private void latencyIncrement() {
        latency = (long) (latency * (1 + latensyVatiationStep));
        latency = latency >= maxLatency ? maxLatency : latency;
    }

    private void latencyDecrement() {
        latency = (long) (latency * (1 - latensyVatiationStep));
        latency = latency <= minLatency ? minLatency : latency;
    }

    public boolean stop() {
        runner.interrupt();
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

    public Long getId() {
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

    public ConcurrentLinkedDeque<CheckerResult> getResults() {
        return results;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public RegisteredPerson getPerson() {
        return person;
    }

    public void setPerson(RegisteredPerson person) {
        this.person = person;
    }

    public ResultMailSender getResultMailSender() {
        return resultMailSender;
    }

    public void setResultMailSender(ResultMailSender resultMailSender) {
        this.resultMailSender = resultMailSender;
    }
}
