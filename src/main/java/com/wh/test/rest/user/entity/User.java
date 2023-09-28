package com.wh.test.rest.user.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.wh.test.rest.user.constraint.AgeConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    Integer id;

    @NotBlank(message = "First name can't be blank")
    String firstName;

    @NotBlank(message = "Last name can't be blank")
    String lastName;

    @Email
    @NotBlank(message = "E-mail can't be blank")
    @NotNull
    String email;

    @AgeConstraint
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate birthday;

    @Builder.Default
    String address = "";

    @Builder.Default
    String phone = "";
}
