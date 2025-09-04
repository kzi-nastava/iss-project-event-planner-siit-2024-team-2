package com.example.eventplanner.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class StatusPair<T> {
    private T value;
    private HttpStatus status;
}
