package com.wh.test.rest.user.controller;

import com.wh.test.rest.user.entity.User;
import com.wh.test.rest.user.mapper.UserMapper;
import com.wh.test.rest.user.request.UserRequest;
import com.wh.test.rest.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("")
    public ResponseEntity<List<User>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> findByBirthday(
            @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam(value = "from") LocalDate fromDate,
            @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam(value = "to") LocalDate toDate) {

        return new ResponseEntity<>(userService.findByBirthday(fromDate, toDate), HttpStatus.FOUND);

    }

    @PostMapping("")
    public ResponseEntity<User> create(@RequestBody @Valid User user) {
        return new ResponseEntity<>(userService.create(user), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> partialUpdate(@PathVariable(value = "id") @Positive Integer id,
                                           @RequestBody UserRequest userUpdated) {
        User user = userService.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with given ID=" + id + " not found"));
        userMapper.partialUpdate(userUpdated, user);
        return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> fullUpdate(@PathVariable(value = "id") @Positive Integer id,
                                       @RequestBody @Valid User userUpdated) {
        User user = userService.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with given ID=" + id + " not found"));
        BeanUtils.copyProperties(userUpdated, user, "id");
        return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") @Positive Integer id) {
        userService.delete(userService.findById(id).orElseThrow(() ->
                new NoSuchElementException("Failed to delete: User not found")));
    }


}
