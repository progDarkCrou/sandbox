package com.avorona.web;

import com.avorona.domain.model.User;

/**
 * Created by avorona on 01.06.16.
 */
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String login;
    private Boolean expired;
    private Boolean active;
    private Integer birthYear;

    public UserResponse() {
    }

    public UserResponse(User user) {
        this.id = user.getId();
        this.active = user.getActive();
        this.firstName = user.getFirstName();
        this.birthYear = user.getBirthYear();
        this.login = user.getUsername();
        this.lastName = user.getLastName();
        this.active = user.getActive();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }
}
