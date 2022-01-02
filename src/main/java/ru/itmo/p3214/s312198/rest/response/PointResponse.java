package ru.itmo.p3214.s312198.rest.response;

import ru.itmo.p3214.s312198.rest.Point;

public class PointResponse extends Response {
    private Point point = new Point();

    public PointResponse() {
        super();
    }

    public PointResponse(Point point) {
        this.point = point;
    }

    public PointResponse(Status status, String description, Point point) {
        super(status, description);
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
