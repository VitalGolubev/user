package com.wh.test.rest.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wh.test.rest.user.entity.User;
import com.wh.test.rest.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.NoSuchElementException;

import static com.wh.test.rest.user.generator.UserGenerator.generateUsers;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserApplicationIntegratedTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    void createUsers() {
        generateUsers(10).forEach(u -> userService.create(u));
    }

    @AfterEach
    void clearUsers() {
        userService.findAll().forEach(u -> userService.delete(u));
    }

    @Test
    void testEndpoint() {
        String url = "http://127.0.0.1:" + port + "/api/users/{id}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<User> response = restTemplate
                .exchange(url, HttpMethod.GET, new HttpEntity<>(headers), User.class, "1");

        User user = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(user);
        assertEquals(1, user.getId());
    }

    @Test
    void findAll() throws Exception {
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
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.firstName").value("f0"))
                .andExpect(jsonPath("$.lastName").value("l0"))
                .andExpect(jsonPath("$.email").value("email0@gmail.com"));
    }

    @Test
    void partialUpdateOneParameter() throws Exception {
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
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/12"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

}
