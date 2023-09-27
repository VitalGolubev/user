package com.wh.test.rest.user.repository;

import com.wh.test.rest.user.entity.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    List<User> list = new ArrayList<>();

    @Override
    public List<User> findAll() {
        return list;
    }

    @Override
    public Optional<User> findById(int id) {
        if (id < 1 || id > list.size()) {
            return Optional.empty();
        }
        return Optional.of(list.get(id - 1));
    }

    @Override
    public void create(User user) {
        list.add(user);
    }

    @Override
    public Integer generateId() {
        return list.size() + 1;
    }

    @Override
    public void update(User user) {
        if (findById(user.getId()).isEmpty()) {
            throw new IllegalArgumentException("User with given ID=" + user.getId() + " doesn't exist");
        }
        list.set(user.getId() - 1, user);
    }

    @Override
    public void delete(User user) {
        list.remove(user);
    }

    @Override
    public List<User> findByBirthday(LocalDate from, LocalDate to) {
        return list.stream()
                .filter(u -> u.getBirthday().isAfter(from) && u.getBirthday().isBefore(to))
                .toList();
    }
}
