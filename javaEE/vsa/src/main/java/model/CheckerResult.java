package model;

import java.util.Date;

/**
 * Created by avorona on 06.10.15.
 */
public class CheckerResult {

    private Date time;
    private String message;
    private CheckStatus checkResult;

    public CheckerResult(Date time, String message) {
        this.time = time;
        this.message = message;
    }

    public CheckerResult(String message, CheckStatus checkResult) {
        this(new Date(), message);
        this.checkResult = checkResult;
    }

    public CheckerResult() {
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

    public CheckStatus getCheckerStatus() {
        return checkResult;
    }

    public void setCheckResult(CheckStatus checkResult) {
        this.checkResult = checkResult;
    }

    public enum CheckStatus {
        RESULT_SUCCESS,
        RESULT_ERROR,
        RESULT_ERROR_CRITICAL,
        RESULT_STOPED;
    }
}
