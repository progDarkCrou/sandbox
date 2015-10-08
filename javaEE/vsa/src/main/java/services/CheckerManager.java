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

    private static String defaultEmailProp = "default.email";
    private static String defaultNameProp = "default.name";

    private final ConfigurableApplicationContext context;

    private String defaultName;
    private String defaultEmail;

    private Logger logger = Logger.getLogger(CheckerManager.class.getName());

    private Set<Checker> checkers = new HashSet<>();

    @Autowired
    public CheckerManager(ConfigurableApplicationContext context) {
        this.context = context;
        ConfigurableEnvironment contextEnvironment = context.getEnvironment();

        String defaultToSendEmail = contextEnvironment.getProperty(defaultEmailProp);
        String defaultToSendName = contextEnvironment.getProperty(defaultNameProp);

        if (defaultToSendEmail == null || defaultToSendName == null) {
            throw new RuntimeException("Both of the \"" + defaultEmailProp + "\" and \"" + defaultNameProp + "\" are required.");
        }

        this.defaultName = defaultToSendName;
        this.defaultEmail = defaultToSendEmail;
    }

    public String run(String data, String url, String referer, String name, String email) {
        Checker checker = (Checker) context.getBean("checker", data, url, referer, name, email);
        checker.init(data, url, referer, new RegisteredPerson(name, email));
        checkers.add(checker);
        return checker.isRunning() ? checker.getName() : null;
    }

    public String run(String data, String url, String referer) {
        return this.run(data, url, referer, this.defaultName, this.defaultEmail);
    }

    public String run(String data, String url, String referer, RegisteredPerson person) {
        return this.run(data, url, referer, person.getName(), person.getEmail());
    }

    public boolean stop(String checkerName) {
        if (this.checkers.parallelStream().anyMatch(checker1 -> checker1.getName().equals(checkerName))) {
            Checker checker = this.getForName(checkerName);
            return checker.stop();
        }
        logger.error("There is no checker with name \"" + checkerName + "\" to stop.");
        return false;
    }

    public boolean stopAll() {
        logger.info("Stopping all checkers.");
        boolean result = checkers.parallelStream()
                .filter(Checker::isRunning)
                .map(Checker::stop)
                .allMatch(a -> a);
        if (!result) {
            long notStopped = checkers.parallelStream().filter(Checker::isRunning).count();
            logger.error(notStopped + " checkers were now stopped. Will be trying to stop them again.");
            this.stopAll();
        }
        return true;
    }

    public boolean isRunning(String checkerName) {
        return this.getForName(checkerName).isRunning();
    }

    public Checker getForName(String checkerName) {
        return this.checkers.parallelStream().filter(checker -> checker.getName().equals(checkerName)).findFirst().get();
    }

}
