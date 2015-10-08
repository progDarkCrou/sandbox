package services;

import model.Checker;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by avorona on 05.10.15.
 */
@Service
@Scope("singleton")
public class CheckerManager {

    private Logger logger = Logger.getLogger(CheckerManager.class.getName());

    private Set<Checker> checkers = new HashSet<>();

    public String run(String url, String referer, String data) {

    }

    public boolean stop(String checkerName) {
        if (this.checkers.stream().anyMatch(checker1 -> checker1.getName().equals(checkerName))) {
            Checker checker = this.checkers.stream()
                    .filter(ch -> ch.getName().equals(checkerName))
                    .findFirst()
                    .get();
            checker.stop();
            return checker.isRunning();
        }
        logger.error("There is no checker with name \"" + checkerName + "\" to stop.");
        return false;
    }

    public boolean stopAll() {
        logger.error("Stopping all checkers.");
        return checkers.stream()
                .parallel()
                .map(checker -> checker.stop())
                .reduce((a, b) -> a && b)
                .get();
    }

}
