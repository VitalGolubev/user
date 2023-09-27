package com.wh.test.rest.user.service;

import com.wh.test.rest.user.entity.User;
import com.wh.test.rest.user.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepositoryImpl userRepository;

    @Test
    void create() {
        User user = generateUsers(1).get(0);
        when(userRepository.generateId()).thenReturn(2);
        doNothing().when(userRepository).create(any(User.class));
        User createdUser = userService.create(user);
        assertEquals(createdUser.getId(), 2);
    }

    @Test
    void findById() {
        User user = generateUsers(1).get(0);
        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        assertTrue(userService.findById(2).isPresent());
        assertTrue(userService.findById(1).isEmpty());
    }

    @Test
    void findByBirthday() {
        when(userRepository.findByBirthday(LocalDate.of(1999,12,31), LocalDate.of(2003, 1, 2)))
                .thenReturn(generateUsers(4));
        assertEquals(4,
                userService.findByBirthday(LocalDate.of(1999,12,31), LocalDate.of(2003, 1, 2)).size());
    }

    @Test
    void findAll() {
        when(userRepository.findAll()).thenReturn(generateUsers(5));
        assertEquals(5, userService.findAll().size());
    }

    private static List<User> generateUsers(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> buildUser(i + 1, "f" + i, "l" + i, "email" + i + "@gmail.com", "01.01.200" + i))
                .collect(Collectors.toList());
    }

    public static User buildUser(int id, String firstName, String lastName, String email, String birthday) {
        return User.builder().id(id).firstName(firstName).lastName(lastName).email(email)
                .birthday(LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd.MM.yyyy"))).build();
    }

}
