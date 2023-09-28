package com.wh.test.rest.user.generator;

import com.wh.test.rest.user.entity.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserGenerator {
    public static List<User> generateUsers(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> buildUser(i + 1, "f" + i, "l" + i, "email" + i + "@gmail.com", "01.01.200" + i))
                .collect(Collectors.toList());
    }

    private static User buildUser(int id, String firstName, String lastName, String email, String birthday) {
        return User.builder().id(id).firstName(firstName).lastName(lastName).email(email)
                .birthday(LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd.MM.yyyy"))).build();
    }

}
