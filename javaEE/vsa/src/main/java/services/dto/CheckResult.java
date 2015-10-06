package services.dto;

import java.util.Date;

/**
 * Created by avorona on 06.10.15.
 */
public class CheckResult {

    private Date time;
    private String message;

    public CheckResult(Date time, String message) {
        this.time = time;
        this.message = message;
    }

    public CheckResult(String message) {
        this(new Date(), message);
    }

    public CheckResult() {
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
