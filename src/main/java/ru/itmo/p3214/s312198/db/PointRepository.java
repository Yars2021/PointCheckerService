package ru.itmo.p3214.s312198.db;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.p3214.s312198.rest.Point;

public interface PointRepository extends JpaRepository<Point, Long> {
}
