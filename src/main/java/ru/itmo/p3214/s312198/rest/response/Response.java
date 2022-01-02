package ru.itmo.p3214.s312198.rest.response;

public class Response {
    public Status status;
    public String description;

    public Response() {
    }

    public Response(Status Status, String description) {
        this.status = Status;
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status Status) {
        this.status = Status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
