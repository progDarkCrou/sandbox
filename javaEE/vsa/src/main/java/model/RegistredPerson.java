package model;

/**
 * Created by crou on 07.10.15.
 */
public class RegistredPerson {
    private String name;
    private String email;

    public RegistredPerson() {
    }

    public RegistredPerson(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
