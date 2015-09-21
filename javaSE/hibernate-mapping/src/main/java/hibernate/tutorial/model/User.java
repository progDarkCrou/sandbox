package hibernate.tutorial.model;

import java.io.Serializable;

/**
 * Created by avorona on 03.09.15.
 */
public class User implements Serializable{

    private Long id;
    private long age;
    private String name;
    private String surname;
    private String login;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
