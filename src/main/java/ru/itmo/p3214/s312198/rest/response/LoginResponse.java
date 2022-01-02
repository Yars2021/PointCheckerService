package ru.itmo.p3214.s312198.rest.response;

public class LoginResponse extends Response {
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(String token) {
        this.token = token;
    }

    public LoginResponse(Status Status, String description, String token) {
        super(Status, description);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
