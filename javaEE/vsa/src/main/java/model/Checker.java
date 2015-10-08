package model;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import services.ResultMailSender;

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
@Component("checker")
public class Checker {

    private static long count = 0;

    private static String invalidAttemptPattern = ".*?Invalid.*?attempt.*?";
    private static String successResultPattern = ".*[0-9]{1,2}..+?.[0-9]{1,4}.*";
    private static String searchElementId = "#ctl00_plhMain_lblMsg";
    private static int maxFails = 20;

    private static long latency = 5000;

    private Logger logger = Logger.getLogger(Checker.class.getName());
    private String name;
    private long id;

    private String data;

    private String url;

    private String referer;
    private RegisteredPerson person;
    private Thread runner;
    private ArrayList<CheckResult> results = new ArrayList<>();

    @Autowired
    private ResultMailSender resultMailSender;

//    public Checker(String name, String data, String url, String referer, RegisteredPerson person) {
//        this.id = count++;
//        this.name = name;
//        this.data = data;
//        this.url = url;
//        this.referer = referer;
//        this.runner = new Thread(this::run, this.name);
//        runner.start();
//        this.resultMailSender = new ResultMailSender(person, this);
//    }

    public Checker() {
    }

    public void init(String data, String url, String referer, RegisteredPerson person) {
        this.id = count++;
        this.name = Checker.class.getSimpleName() + "-" + count;
        this.data = data;
        this.url = url;
        this.referer = referer;
        this.runner = new Thread(this::run, this.name);
        this.runner.start();
        this.resultMailSender.setChecker(this);
        this.resultMailSender.setPerson(person);
    }

//    public Checker(String name, String data, String url, String referer, String nameToSend, String emailToSend) {
//        this(name, data, url, referer, new RegisteredPerson(nameToSend, emailToSend));
//    }
//
//    public Checker(String data, String url, String referer, String nameToSend, String emailToSend) {
//        this(Checker.class.getName() + "-" + count, data, url, referer, nameToSend, emailToSend);
//    }

    private void run() {
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
                        this.resultMailSender.sendFatalError(checkResult.getMessage());
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
                        this.resultMailSender.sendSuccess(checkResult.getMessage());
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
                        this.resultMailSender.sendError(checkResult.getMessage());
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
    }

    public boolean stop() {
        runner.interrupt();
        logger.info(this.name + " - stopped.");
        String message = this.name + ": stopped";
        this.results.add(new CheckResult(message, CheckResult.CheckStatus.RESULT_STOPED));
        this.resultMailSender.send(message, ResultMailSender.MessagesSubject.stopCheckerHeader);
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
}
