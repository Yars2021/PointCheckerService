package ru.itmo.p3214.s312198.rest.reqest;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class RegistrationRequest {

    @Min(1)
    @Max(32)
    private String login;

    @Min(8)
    private String password;

    @Min(8)
    private String passwordRepeat;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String login, String password, String passwordRepeat) {
        this.login = login;
        this.password = password;
        this.passwordRepeat = passwordRepeat;
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

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }
}
