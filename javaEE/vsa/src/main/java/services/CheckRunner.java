package services;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by avorona on 05.10.15.
 */
@Service
@Scope("singleton")
public class CheckRunner {

    private int maxFails = 20;
    private long latency = 5000;
    private ExecutorService exec;
    private Logger logger = Logger.getLogger(CheckRunner.class.getName());
    private ConcurrentHashMap<Date, String> results = new ConcurrentHashMap<>();

    private String failPattern = ".*Invalid.*";

    public void run(String url, String referrer, String data) {
        exec = Executors.newFixedThreadPool(1);
        exec.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                DataOutputStream outStream = null;
                BufferedReader inStream = null;

                int fails = 0;

                try {
                    HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Connection", "keep-alive");
                    connection.setRequestProperty("Referer", referrer);

                    connection.setDoOutput(true);

                    outStream = new DataOutputStream(connection.getOutputStream());

                    outStream.writeBytes(data);
                    outStream.flush();
                    outStream.close();

                    inStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Document document = Jsoup.parse(inStream.lines().reduce((s, s2) -> s + s2).get());

                    Elements searchElements = document.select("#ctl00_plhMain_lblMsg");

                    if (searchElements == null || searchElements.size() == 0) {
                        fails++;
                        String message = "There some problem occurred while getting. Please check it out...";
                        results.put(new Date(), message);
                        logger.error(message);
                    }
                    Element searchElement = searchElements.first();

                    if (searchElement.html().matches("")) {

                    }

                    fails = 0;

                    Thread.currentThread().sleep(latency);
                } catch (IOException e) {
                    fails = 0;
                    if (fails >= maxFails) {
                        logger.error("Max fails count was reached. Please reinitialize checker.");
                        break;
                    }
                    try {
                        if (inStream != null) {
                            inStream.close();
                        }
                        if (outStream != null) {
                            outStream.close();
                        }
                    } catch (IOException e1) {
                        logger.error("Something occurred while requesting.", e1);
                    }
                    fails++;
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void stop() {
        if (exec != null && exec.isShutdown()) {
            exec.shutdown();
        } else {
            logger.info("There is no checkers to stop.");
        }
    }

}
