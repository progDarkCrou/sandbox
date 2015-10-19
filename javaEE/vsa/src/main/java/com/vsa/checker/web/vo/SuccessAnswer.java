package com.vsa.checker.web.vo;

/**
 * Created by avorona on 06.10.15.
 */
public class SuccessAnswer extends Answer {

    private String result;

    public SuccessAnswer(String res) {
        this.result = res;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
