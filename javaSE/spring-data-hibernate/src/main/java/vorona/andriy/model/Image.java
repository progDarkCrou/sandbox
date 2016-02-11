package vorona.andriy.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Blob;

/**
 * Created by avorona on 29.12.15.
 */
@Entity
public class Image {

    @Id
    private String name;

    @Fetch(FetchMode.SELECT)
    private Blob file;

    public Image() {
    }

    public Image(String name, Blob file) {
        this.name = name;
        this.file = file;
    }

    public Blob getFile() {
        return file;
    }

    public void setFile(Blob file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
