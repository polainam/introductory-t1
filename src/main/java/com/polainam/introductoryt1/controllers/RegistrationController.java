package com.polainam.introductoryt1.controllers;

import com.polainam.introductoryt1.models.Candidate;
import com.polainam.introductoryt1.models.Status;
import com.polainam.introductoryt1.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public void registration(Candidate candidate, Status status) {
        registrationService.manageRegistration(candidate, status);
    }
}
