package com.wh.test.rest.user.service;

import com.wh.test.rest.user.entity.User;
import com.wh.test.rest.user.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;

import static com.wh.test.rest.user.generator.UserGenerator.generateUsers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
        user.setId(null);
        when(userRepository.create(any(User.class))).thenReturn(generateUsers(2).get(1));
        assertEquals(2, userService.create(user).getId());
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

}
