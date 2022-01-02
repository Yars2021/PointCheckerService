package ru.itmo.p3214.s312198.rest.reqest;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class LoginRequest {
    @Min(1)
    @Max(32)
    private String login;

    @Min(8)
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
