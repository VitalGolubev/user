package com.wh.test.rest.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wh.test.rest.user.entity.User;
import com.wh.test.rest.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void findAll() throws Exception {
        when(userService.findAll()).thenReturn(generateUsers(10));
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("f0"))
                .andExpect(jsonPath("$[0].lastName").value("l0"))
                .andExpect(jsonPath("$[0].email").value("email0@gmail.com"));
    }

    @Test
    void findByBirthday() throws Exception {
        when(userService.findByBirthday(LocalDate.of(1999, 12, 31), LocalDate.of(2005, 1, 2)))
                .thenReturn(generateUsers(6));
        mockMvc.perform(get("/api/users/search?from=1999-12-31&to=2005-01-02"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("f0"))
                .andExpect(jsonPath("$[0].lastName").value("l0"))
                .andExpect(jsonPath("$[0].email").value("email0@gmail.com"));

    }

    @Test
    void create() throws Exception {
        User user = generateUsers(1).get(0);
        when(userService.create(any(User.class))).thenReturn(user);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("f0"))
                .andExpect(jsonPath("$.lastName").value("l0"))
                .andExpect(jsonPath("$.email").value("email0@gmail.com"));
    }

    @Test
    void partialUpdateOneParameter() throws Exception {
        when(userService.findById(1))
                .thenReturn(Optional.of(generateUsers(1).get(0)));
        when(userService.update(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String requestBody = "{\"firstName\": \"uf1\"}";

        mockMvc.perform(patch("/api/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("uf1"))
                .andExpect(jsonPath("$.lastName").value("l0"))
                .andExpect(jsonPath("$.email").value("email0@gmail.com"));
    }

    @Test
    void fullUpdate() throws Exception {
        User user = generateUsers(1).get(0);
        user.setFirstName("newF0");
        user.setLastName("newL0");
        user.setEmail("new_email0@gmail.com");
        when(userService.findById(1)).thenReturn(Optional.of(user));
        when(userService.update(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("newF0"))
                .andExpect(jsonPath("$.lastName").value("newL0"))
                .andExpect(jsonPath("$.email").value("new_email0@gmail.com"));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException));

        when(userService.findById(1)).thenReturn(Optional.of(generateUsers(1).get(0)));
        doNothing().when(userService).delete(any(User.class));
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1"))
                .andExpect(status().isNoContent());
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
