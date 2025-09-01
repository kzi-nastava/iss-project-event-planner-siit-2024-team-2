package com.example.eventplanner.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Set;

public class RatingValidator implements ConstraintValidator<ValidRating, Double> {
    private static final Set<Double> RATINGS = new HashSet<>();
    static {
        RATINGS.add(1.0);
        RATINGS.add(1.5);
        RATINGS.add(2.0);
        RATINGS.add(2.5);
        RATINGS.add(3.0);
        RATINGS.add(3.5);
        RATINGS.add(4.0);
        RATINGS.add(4.5);
        RATINGS.add(5.0);
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        return RATINGS.contains(value);
    }
}
