package com.polainam.introductoryt1.models;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class Candidate {

    @JsonSetter("first_name")
    private String firstName;

    @JsonSetter("last_name")
    private String lastName;

    private String email;
    private String role;
}
