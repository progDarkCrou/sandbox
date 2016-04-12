package com.avorona.web.vo;

/**
 * Created by avorona on 12.04.16.
 */
public class ImageVO {

    private Long id;
    private String title;

    public ImageVO() {
    }

    public ImageVO(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
