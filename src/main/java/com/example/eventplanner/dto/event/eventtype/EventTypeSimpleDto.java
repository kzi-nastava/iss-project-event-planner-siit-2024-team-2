package com.example.eventplanner.dto.event.eventtype;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventTypeSimpleDto {
    private long id;
    private String name;
}
