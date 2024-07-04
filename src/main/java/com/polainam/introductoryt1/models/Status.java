package com.polainam.introductoryt1.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class Status {

    private String token;
    private String status;
}
