package com.vsa.checker.services;

import com.vsa.checker.model.Checker;
import com.vsa.checker.model.CheckerResult;
import com.vsa.checker.model.RegisteredPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by avorona on 08.10.15.
 */
@Component
@Scope("prototype")
public class ResultMailSender {

    private static long sendFrequencyInMin = 10;

    private Date lastSendAttempt;
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

    public void sendSuccess(CheckerResult result) {
        if (this.lastSendAttempt != null && TimeUnit.MINUTES.convert(result.getTime().getTime() -
                this.lastSendAttempt.getTime(), TimeUnit.MILLISECONDS) > sendFrequencyInMin || this.lastSendAttempt == null) {
            this.send(result.getMessage(), MessagesSubject.successResultHeader);
            this.lastSendAttempt = result.getTime();
        }
        this.lastSendAttempt = result.getTime();
    }

    public void sendFatalError(CheckerResult result) {
        this.send(result.getMessage(), MessagesSubject.fatalErrorResultHeader);
    }

    public void sendError(CheckerResult result) {
        this.send(result.getMessage(), MessagesSubject.errorResultHeader);
    }

    public void send(String body, String subject) {
        SimpleMailMessage message = new SimpleMailMessage(mailTemplate);
        message.setTo(this.person.getEmail());
        message.setSubject(this.checker.getName() + ": " + subject);
        message.setSentDate(new Date());
        message.setText("Dear " + this.person.getName() + "!\n" + body);
        message.setFrom(this.checker.getName() + "@vsa.io");
        this.mailSender.send(message);
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
