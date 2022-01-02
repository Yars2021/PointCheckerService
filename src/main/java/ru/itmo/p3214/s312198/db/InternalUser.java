package ru.itmo.p3214.s312198.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
public class InternalUser {
    @Id
    @Column(unique = true, nullable = false, length = 64)
    private String login;

    @Column(nullable = false, length = 256)
    private String password;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false, length = 64)
    private String authority;

    @Column(nullable = false)
    private Boolean logged;

    @Column(nullable = true, length = 128)
    private String token;

    @Column(nullable = false)
    private Timestamp updatedAt;

    public InternalUser() {
    }

    public InternalUser(String login, String password, Boolean active, String authority, Boolean logged, String token, Timestamp updatedAt) {
        this.login = login;
        this.password = password;
        this.active = active;
        this.authority = authority;
        this.logged = logged;
        this.token = token;
        this.updatedAt = updatedAt;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Boolean getLogged() {
        return logged;
    }

    public void setLogged(Boolean logged) {
        this.logged = logged;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
