package ru.itmo.p3214.s312198.rest.response;

import ru.itmo.p3214.s312198.db.InternalUser;

public class UserResponse extends Response {
    private InternalUser user = new InternalUser();

    public UserResponse() {
        super();
    }

    public UserResponse(InternalUser user) {
        this.user = user;
    }

    public UserResponse(Status Status, String description, InternalUser user) {
        super(Status, description);
        this.user = user;
    }

    public InternalUser getUser() {
        return user;
    }

    public void setUser(InternalUser user) {
        this.user = user;
    }
}
