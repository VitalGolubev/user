package com.wh.test.rest.user.validator;

import com.wh.test.rest.user.constraint.AgeConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AgeValidator implements ConstraintValidator<AgeConstraint, LocalDate> {
    @Value("${user.minimum.age}")
    private int minimumAge;

    @Override
    public void initialize(AgeConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return true;
        }
        return date.plusYears(minimumAge).isBefore(LocalDate.now());
    }
}
