package com.wh.test.rest.user.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AgeValidatorTest {


    @Autowired
    AgeValidator ageValidator;

    @Value("${user.minimum.age}")
    private int minimumAge;

    @Test
    public void testIsValidWithNullDate() {
        boolean result = ageValidator.isValid(null, null);
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"01.01.2000", "01.01.2010", "01.01.2005"})
    void testIsValid(String date) {
        LocalDate birthday = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        boolean result = ageValidator.isValid(birthday, null);
        assertEquals(birthday.plusYears(minimumAge).isBefore(LocalDate.now()), result);
    }
}
