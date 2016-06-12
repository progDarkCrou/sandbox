package com.vsa.checker.web.ctrl.vo;

import com.vsa.checker.model.CheckerResult;

import java.text.DateFormat;
import java.util.Locale;

/**
 * Created by avorona on 14.10.15.
 */
public class CheckerResultVO {

    private String date;
    private String result;

    CheckerResultVO(CheckerResult checkerResult) {
        this.date = DateFormat.getDateTimeInstance(DateFormat.DATE_FIELD,
                DateFormat.MEDIUM,
                Locale.getDefault())
                .format(checkerResult.getTime());
        this.result = checkerResult.getMessage();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
