package com.avorona.entity;

import javax.persistence.*;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;

/**
 * Created by avorona on 11.04.16.
 */

@Entity
public class Image {

    @Id
    private BigInteger id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Image originalImage;

    private String title;
    private String extension;

    public Image() {
    }

    public Image(String title, String extension) {
        this.title = title;
        this.extension = extension;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Image getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(Image originalImage) {
        this.originalImage = originalImage;
    }
}
