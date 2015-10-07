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

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by avorona on 05.10.15.
 */
@Service
@Scope("singleton")
public class CheckManager {

    private Logger logger = Logger.getLogger(CheckManager.class.getName());

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
