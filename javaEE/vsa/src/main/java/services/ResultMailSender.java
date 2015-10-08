package services;

import model.Checker;
import model.RegisteredPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by avorona on 08.10.15.
 */
@Service
@Scope("prototype")
public class ResultMailSender {

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

    public void sendSuccess(String message) {
        this.send(message, MessagesSubject.successResultHeader);
    }

    public void sendFatalError(String message) {
        this.send(message, MessagesSubject.fatalErrorResultHeader);
    }

    public void sendError(String message) {
        this.send(message, MessagesSubject.errorResultHeader);
    }

    public void send(String body, MessagesSubject subject) {
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

    public enum MessagesSubject {
        successResultHeader("Successful found date"),
        errorResultHeader("Some error occurred"),
        stopCheckerHeader("Checker stopped"),
        fatalErrorResultHeader("Fatal error occurred!!!");

        private final String name;

        MessagesSubject(String name) {
            this.name = name;
        }
    }
}
