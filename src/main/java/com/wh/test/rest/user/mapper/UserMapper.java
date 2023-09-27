package com.wh.test.rest.user.mapper;

import com.wh.test.rest.user.entity.User;
import com.wh.test.rest.user.request.UserRequest;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User partialUpdate(UserRequest userRequest, User user) {
        if (userRequest.getFirstName() != null && !userRequest.getFirstName().isEmpty()) {
            user.setFirstName(userRequest.getFirstName());
        }
        if (userRequest.getLastName() != null && !userRequest.getLastName().isEmpty()) {
            user.setLastName(userRequest.getLastName());
        }
        if (userRequest.getEmail() != null && !userRequest.getEmail().isEmpty()) {
            user.setEmail(userRequest.getEmail());
        }
        if (userRequest.getBirthday() != null) {
            user.setBirthday(userRequest.getBirthday());
        }
        if (userRequest.getAddress() != null) {
            user.setAddress(userRequest.getAddress());
        }
        if (userRequest.getPhone() != null) {
            user.setPhone(userRequest.getPhone());
        }

        return user;
    }

}
