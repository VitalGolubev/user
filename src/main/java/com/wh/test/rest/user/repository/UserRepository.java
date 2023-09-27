package com.wh.test.rest.user.repository;

import com.wh.test.rest.user.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    Optional<User> findById(int id);

    void create(User user);

    Integer generateId();

    void update(User user);

    void delete(User user);

    List<User> findByBirthday(LocalDate from, LocalDate to);
}
