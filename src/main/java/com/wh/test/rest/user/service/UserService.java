package com.wh.test.rest.user.service;

import com.wh.test.rest.user.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(User user);

    User update(User user);

    void delete(User user);

    Optional<User> findById(Integer id);

    List<User> findByBirthday(LocalDate from, LocalDate to);

    List<User> findAll();

}
