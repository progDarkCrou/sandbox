package services;

import model.CheckResult;
import model.Checker;
import model.RegisteredPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by avorona on 08.10.15.
 */
@Component
public class ResultMailSender {

    private static long sendFrequencyInMin = 10;

    private Date lastSent;
    private RegisteredPerson person;

    private Checker checker;

    @Autowired
    private MailSender mailSender;
    @Autowired
    private SimpleMailMessage mailTemplate;

    public ResultMailSender(RegisteredPerson person, Checker checker) {
        this.person = person;
        this.checker = checker;
    }

    public ResultMailSender() {
    }

    public void sendSuccess(CheckResult result) {
        if (TimeUnit.MINUTES.convert(result.getTime().getTime() -
                this.lastSent.getTime(), TimeUnit.MILLISECONDS) > this.sendFrequencyInMin) {
            this.send(result.getMessage(), MessagesSubject.successResultHeader);
            this.lastSent = result.getTime();
        }
    }

    public void sendFatalError(CheckResult result) {
        this.send(result.getMessage(), MessagesSubject.fatalErrorResultHeader);
    }

    public void sendError(CheckResult result) {
        this.send(result.getMessage(), MessagesSubject.errorResultHeader);
    }

    public void send(String body, String subject) {
        SimpleMailMessage message = new SimpleMailMessage(mailTemplate);
        message.setTo(person.getEmail());
        message.setSubject(this.checker.getName() + ": " + subject.toString());
        message.setSentDate(new Date());
        message.setText(body);
        message.setFrom(this.checker.getName() + "@vsa.io");
        mailSender.send(message);
    }

    public RegisteredPerson getPerson() {
        return person;
    }

    public void setPerson(RegisteredPerson person) {
        this.person = person;
    }

    public Checker getChecker() {
        return checker;
    }

    public void setChecker(Checker checker) {
        this.checker = checker;
    }

    public interface MessagesSubject {

        String successResultHeader = "Successful found date";
        String errorResultHeader = "Some error occurred";
        String stopCheckerHeader = "Checker stopped";
        String fatalErrorResultHeader = "Fatal error occurred!!!";
    }
}
