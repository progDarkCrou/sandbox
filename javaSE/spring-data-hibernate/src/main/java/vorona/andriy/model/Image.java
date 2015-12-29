package vorona.andriy.model;

import com.sun.istack.internal.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Blob;

/**
 * Created by avorona on 29.12.15.
 */
@Entity
public class Image {

    @Id
    @NotNull
    private String name;

    @NotNull
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
