package com.wh.test.rest.user.mapper;

import com.wh.test.rest.user.entity.User;
import com.wh.test.rest.user.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User partialUpdate(UserDto userDto, User user) {
        if (userDto.getFirstName() != null && !userDto.getFirstName().isEmpty()) {
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null && !userDto.getLastName().isEmpty()) {
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getBirthday() != null) {
            user.setBirthday(userDto.getBirthday());
        }
        if (userDto.getAddress() != null) {
            user.setAddress(userDto.getAddress());
        }
        if (userDto.getPhone() != null) {
            user.setPhone(userDto.getPhone());
        }

        return user;
    }

}
