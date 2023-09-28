package com.wh.test.rest.user.repository;

import com.wh.test.rest.user.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static com.wh.test.rest.user.generator.UserGenerator.generateUsers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepositoryImpl repository;

    @AfterEach
    void clearRepository() {
        repository.findAll().forEach(u -> repository.delete(u));
    }

    @ParameterizedTest
    @MethodSource("usersGenerator")
    void findAll(List<User> users, int count) {
        users.forEach(u -> repository.create(u));
        assertEquals(count, repository.findAll().size());
    }

    @ParameterizedTest
    @MethodSource("usersGenerator")
    void findById(List<User> users, int count) {
        users.forEach(u -> repository.create(u));
        for (int i = 0; i < count; i++) {
            assertTrue(repository.findById(i + 1).isPresent());
        }
    }

    @Test
    void findByIdThatNotExists() {
        assertTrue(repository.findById(0).isEmpty());
    }

    @ParameterizedTest
    @MethodSource("usersGenerator")
    void create(List<User> users, int count) {
        users.forEach(u -> repository.create(u));
        assertEquals(count, repository.findAll().size());
    }

    @ParameterizedTest
    @MethodSource("usersGenerator")
    void generateId(List<User> users, int count) {
        for (int i = 0; i < count; i++) {
            assertEquals(i + 1, repository.generateId());
            repository.create(users.get(i));
        }
        assertEquals(count + 1, repository.generateId());
    }

    @Test
    void update() {
        List<User> users = generateUsers(2);
        repository.create(users.get(0));
        User updatedUser = users.get(1);
        updatedUser.setId(1);
        repository.update(updatedUser);
        assertNotEquals(users.get(0), repository.findById(1).orElse(null));
        assertEquals(users.get(1), repository.findById(1).orElse(null));

    }

    @ParameterizedTest
    @MethodSource("usersGenerator")
    void delete(List<User> users, int count) {
        users.forEach(u -> repository.create(u));
        assertEquals(count, repository.findAll().size());
        for (int i = 0; i < count; i++) {
            repository.delete(users.get(i));
            assertEquals(count - (i + 1), repository.findAll().size());
        }
    }

    @ParameterizedTest
    @MethodSource("usersGenerator")
    void findByBirthday(List<User> users, int count) {
        users.forEach(u -> repository.create(u));
        assertEquals(count, repository.findAll().size());
        assertEquals(count - 2,
                repository.findByBirthday(LocalDate.of(1999, 12, 31),
                        LocalDate.of(2000 + (count - 2), 1, 1)).size());

        assertEquals(0,
                repository.findByBirthday(LocalDate.now(),
                        LocalDate.now().plusYears(1)).size());
    }

    private static Stream<Arguments> usersGenerator() {
        return Stream.of(
                Arguments.of(generateUsers(3), 3),
                Arguments.of(generateUsers(4), 4),
                Arguments.of(generateUsers(5), 5),
                Arguments.of(generateUsers(6), 6));
    }

}
