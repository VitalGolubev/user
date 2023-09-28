package com.wh.test.rest.user.repository;

import com.wh.test.rest.user.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    Optional<User> findById(int id);

    User create(User user);

    User update(User user);

    boolean delete(User user);

    List<User> findByBirthday(LocalDate from, LocalDate to);
}
