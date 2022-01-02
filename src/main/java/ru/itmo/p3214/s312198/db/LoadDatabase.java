package ru.itmo.p3214.s312198.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.itmo.p3214.s312198.rest.Point;

import java.sql.Timestamp;
import java.util.Date;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
class LoadDatabase {
    private static final Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(PointRepository repository) {
        return args -> {
//            logger.info("Preloading..." + repository.save(new Point(1.23456f, 2f, 3f,
//                    new Timestamp(new java.util.Date().getTime()), Boolean.TRUE)));
        };
    }

    @Bean
    CommandLineRunner preloadUser(UserRepository repository) {
        return args -> {
            logger.info("Preloading..." + repository.save(
                    new InternalUser("user",
                            "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
                            Boolean.TRUE,
                            "USER",
                            Boolean.FALSE,
                            null,
                            new Timestamp(new Date().getTime()))));
        };
    }

}