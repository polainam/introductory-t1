package com.polainam.introductoryt1.services;

import com.polainam.introductoryt1.models.Candidate;
import com.polainam.introductoryt1.models.Status;

public interface RegistrationService {
    void manageRegistration(Candidate candidate, Status status);
}
