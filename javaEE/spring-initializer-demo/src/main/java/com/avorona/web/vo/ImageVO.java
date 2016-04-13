package com.avorona.web.vo;

import com.avorona.web.Image;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by avorona on 12.04.16.
 */
public class ImageVO {

    private BigInteger id;
    private String title;
    private Date dateCreated;

    public ImageVO() {
    }

    public ImageVO(Image image) {
        this.id = image.getId();
        this.title = image.getTitle() + "." + image.getExtension();
        if (image.getDateCreated() != null) {
            this.dateCreated = Date.from(image.getDateCreated());
        }
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

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
