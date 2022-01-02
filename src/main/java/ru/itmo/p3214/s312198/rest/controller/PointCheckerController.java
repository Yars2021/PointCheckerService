package ru.itmo.p3214.s312198.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.p3214.s312198.db.InternalUser;
import ru.itmo.p3214.s312198.db.PointRepository;
import ru.itmo.p3214.s312198.db.UserRepository;
import ru.itmo.p3214.s312198.rest.Point;
import ru.itmo.p3214.s312198.rest.response.PointResponse;
import ru.itmo.p3214.s312198.rest.response.PointsResponse;
import ru.itmo.p3214.s312198.rest.response.Status;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
public class PointCheckerController {

    private final PointRepository pointRepository;
    private final UserRepository userRepository;

    public PointCheckerController(PointRepository repository, UserRepository userRepository) {
        this.pointRepository = repository;
        this.userRepository = userRepository;
    }

    /**
     * Returns Point-in-Area check result
     *
     * @param x point's X coordinate
     * @param y point's Y coordinate
     * @param r area radius
     * @return true if the point hits the area, false otherwise
     */
    private Boolean checkHit(Float x, Float y, Float r) {
        if (r == 0 && x == 0 && y == 0) {
            return true;
        }
        if (r > 0) {
            return (x < 0 && x >= -r / 2 && y >= 0 && y <= r) || (x <= 0 && y <= 0 && x * x + y * y <= (r * r / 4)) || (x >= 0 && y <= 0 && 2 * y >= x - r);
        } else {
            return (x > 0 && x <= -r / 2 && y <= 0 && y >= r) || (x >= 0 && y >= 0 && x * x + y * y <= (r * r / 4)) || (x <= 0 && y >= 0 && 2 * y <= x - r);
        }
    }

    @PostMapping(path = "/points",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PointResponse> add(@RequestBody Point pointToAdd, @RequestHeader(name = "Token", required = false) String token) {
        PointResponse response = new PointResponse();
        if (!isRequestAuthorized(token)) {
            return new ResponseEntity<PointResponse>(response, HttpStatus.UNAUTHORIZED);
        } else {
            Point point = new Point();
            point.setX(pointToAdd.getX());
            point.setY(pointToAdd.getY());
            point.setR(pointToAdd.getR());
            point.setCreated(new Timestamp(new Date().getTime()));
            point.setHit(checkHit(pointToAdd.getX(), pointToAdd.getY(), pointToAdd.getR()));
            pointRepository.save(point);
            response.setPoint(point);
            response.setStatus(point.getHit() ? Status.SUCCESS : Status.FAIL);
            response.setDescription("Hit checked. The result is " + response.getStatus());
            return new ResponseEntity<PointResponse>(response, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/points/{id}")
    public ResponseEntity<PointResponse> getPoint(@PathVariable(name = "id") Long id, @RequestHeader(name = "Token", required = false) String token) {
        PointResponse response = new PointResponse();
        if (!isRequestAuthorized(token)) {
            return new ResponseEntity<PointResponse>(response, HttpStatus.UNAUTHORIZED);
        } else {
            Optional<Point> point = pointRepository.findById(id);
            if (point.isPresent()) {
                response.setPoint(point.get());
                response.setStatus(Status.SUCCESS);
                response.setDescription("OK");
            } else {
                response.setStatus(Status.FAIL);
                response.setDescription("ERROR");
            }
            return new ResponseEntity<PointResponse>(response, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/points")
    public ResponseEntity<PointsResponse> getAll(@RequestHeader(name = "Token", required = false) String token) {
        PointsResponse response = new PointsResponse();
        if (!isRequestAuthorized(token)) {
            return new ResponseEntity<PointsResponse>(response, HttpStatus.UNAUTHORIZED);
        } else {
            List<Point> list = pointRepository.findAll();
            list.sort(Comparator.comparing(Point::getCreated).reversed());
            response.setPoints(list);
            response.setStatus(Status.SUCCESS);
            response.setDescription("OK");
            return new ResponseEntity<PointsResponse>(response, HttpStatus.OK);
        }
    }

    /**
     * Check if the request is authorized (has token and not timed out)
     *
     * @param token token to validate
     * @return true if the request is valid, false otherwise.
     */
    private Boolean isRequestAuthorized(String token) {
        if (token == null) {
            return Boolean.FALSE;
        } else {
            InternalUser user = userRepository.findByToken(token);
            if (user == null) {
                return Boolean.FALSE;
            } else {
                if ((new Date().getTime() - user.getUpdatedAt().getTime()) >= 600000) {
                    return Boolean.FALSE;
                } else {
                    user.setUpdatedAt(new Timestamp(new Date().getTime()));
                    userRepository.save(user);
                    return Boolean.TRUE;
                }
            }
        }
    }
}
