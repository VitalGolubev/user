package com.wh.test.rest.user.repository;

import com.wh.test.rest.user.entity.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    Map<Integer, User> map = new HashMap<>();

    @Override
    public List<User> findAll() {
        return List.copyOf(map.values());
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public User create(User user) {
        user.setId(generateId());
        map.put(user.getId(), user.toBuilder().build());
        return user;
    }

    public Integer generateId() {
        int i = map.size() + 1;
        while (findById(i).isPresent()) {
            i++;
        }
        return i;
    }

    @Override
    public User update(User user) {
        if (findById(user.getId()).isEmpty()) {
            throw new IllegalArgumentException("User with given ID=" + user.getId() + " doesn't exist");
        }
        map.put(user.getId(), user.toBuilder().build());
        return user;
    }

    @Override
    public boolean delete(User user) {
        return map.remove(user.getId(), user);
    }

    @Override
    public List<User> findByBirthday(LocalDate from, LocalDate to) {
        return List.copyOf(map.values().stream()
                .filter(u -> u.getBirthday().isAfter(from) && u.getBirthday().isBefore(to))
                .toList());
    }
}
