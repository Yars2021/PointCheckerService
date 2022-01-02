package ru.itmo.p3214.s312198.rest;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "points")
public class Point {
    @Id
    @SequenceGenerator(name = "genSeqPoints", sequenceName = "seq_points", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "genSeqPoints")
    private Long Id;
    @Column(nullable = false)
    private Float x;
    @Column(nullable = false)
    private Float y;
    @Column(nullable = false)
    private Float r;
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp created;
    @Column(nullable = false)
    private Boolean hit;

    public Point() {
    }

    public Point(Float x, Float y, Float r, Timestamp created, Boolean hit) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.created = created;
        this.hit = hit;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getR() {
        return r;
    }

    public void setR(Float r) {
        this.r = r;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp date) {
        this.created = date;
    }

    public Boolean getHit() {
        return hit;
    }

    public void setHit(Boolean hit) {
        this.hit = hit;
    }
}
