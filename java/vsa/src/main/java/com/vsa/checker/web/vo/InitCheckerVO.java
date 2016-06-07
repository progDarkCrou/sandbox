package com.vsa.checker.web.vo;

/**
 * Created by avorona on 05.10.15.
 */
public class InitCheckerVO {

    private String referer;
    private String data;
    private String url;
    private String email;
    private String name;

    public InitCheckerVO(String referer, String data, String url, String email) {
        this.referer = referer;
        this.data = data;
        this.url = url;
        this.email = email;
    }

    public InitCheckerVO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
