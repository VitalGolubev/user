package com.wh.test.rest.user.service;

import com.wh.test.rest.user.entity.User;
import com.wh.test.rest.user.repository.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepositoryImpl repository;

    @Override
    public User create(User user) {
        user.setId(repository.generateId());
        repository.create(user);
        return user;
    }

    @Override
    public User update(User user) {
        repository.update(user);
        return user;
    }

    @Override
    public void delete(User user) {
        repository.delete(user);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<User> findByBirthday(LocalDate from, LocalDate to) {
        return repository.findByBirthday(from, to);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

}
