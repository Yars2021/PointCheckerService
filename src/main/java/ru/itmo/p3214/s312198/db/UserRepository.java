package ru.itmo.p3214.s312198.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<InternalUser, String> {
    InternalUser findByLogin(String login);
    InternalUser findByToken(String token);
}
