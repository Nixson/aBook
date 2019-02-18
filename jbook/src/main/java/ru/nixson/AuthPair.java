package ru.nixson;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class AuthPair {
    @Column(name = "login")
    private String login;

    @Column(name = "pass")
    private String pass;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
