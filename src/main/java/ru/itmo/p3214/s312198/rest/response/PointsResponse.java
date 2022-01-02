package ru.itmo.p3214.s312198.rest.response;

import ru.itmo.p3214.s312198.rest.Point;

import java.util.ArrayList;
import java.util.List;

public class PointsResponse extends Response {
    private List<Point> points = new ArrayList<>();

    public PointsResponse() {
        super();
    }

    public PointsResponse(List<Point> points) {
        this.points = points;
    }

    public PointsResponse(Status status, String description, List<Point> points) {
        super(status, description);
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
