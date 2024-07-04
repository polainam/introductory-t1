package com.polainam.introductoryt1.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.polainam.introductoryt1.HttpRequestFactory;
import com.polainam.introductoryt1.exceptions.InvalidReturnCodeException;
import com.polainam.introductoryt1.models.Candidate;
import com.polainam.introductoryt1.models.Status;
import com.polainam.introductoryt1.utils.JsonConverter;
import com.polainam.introductoryt1.validation.CandidateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.*;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final JsonConverter jsonConverter;
    private final InputValidationService inputValidationService;
    private final HttpOperationsService httpOperationsService;
    private final HttpClient httpClient;
    private final HttpRequestFactory httpRequestFactory;
    private final CandidateValidator candidateValidator;

    @Autowired
    public RegistrationServiceImpl(JsonConverter jsonConverter,
                                   InputValidationService inputValidationService,
                                   HttpOperationsService httpOperationsService,
                                   HttpRequestFactory httpRequestFactory,
                                   CandidateValidator candidateValidator) {
        httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        this.jsonConverter = jsonConverter;
        this.inputValidationService = inputValidationService;
        this.httpOperationsService = httpOperationsService;
        this.httpRequestFactory = httpRequestFactory;
        this.candidateValidator = candidateValidator;
    }

    public void manageRegistration(Candidate candidate, Status status) {
        try {
            String candidateJson = createCandidate(candidate);
            signUpCandidate(candidateJson);
            String generatedCode = getCode(candidate);
            String email = candidate.getEmail();
            String token = createToken(email, generatedCode);
            String statusJson = createStatusJson(status, token);
            setStatus(statusJson);
        } catch (InvalidReturnCodeException e) {
            e.printStackTrace();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getRoles() throws IOException, InterruptedException, InvalidReturnCodeException {
        return httpOperationsService.getRoles(httpClient, httpRequestFactory);
    }

    private String createCandidate(Candidate candidate) throws IOException, InterruptedException, InvalidReturnCodeException {
        List<String> roles = getRoles();
        for (String role : roles) {
            System.out.println(role);
        }
        Scanner scanner = new Scanner(System.in);

        String msg = "Введите роль: ";
        candidate.setRole(inputValidationService.promptUserForValidInput(scanner, msg, input -> candidateValidator.isValidRole(roles, input)));

        msg = "Введите имя: ";
        candidate.setFirstName(inputValidationService.promptUserForValidInput(scanner, msg, candidateValidator::isValidName));

        msg = "Введите фамилию: ";
        candidate.setLastName(inputValidationService.promptUserForValidInput(scanner, msg, candidateValidator::isValidName));

        msg = "Введите адрес эл. почты: ";
        candidate.setEmail(inputValidationService.promptUserForValidInput(scanner, msg, candidateValidator::isValidEmail));

        return jsonConverter.serialize(candidate);
    }

    private void signUpCandidate(String candidateJson) throws IOException, InterruptedException, InvalidReturnCodeException {
        httpOperationsService.signUpCandidate(httpClient, httpRequestFactory, candidateJson);
    }

    private String getCode(Candidate candidate) throws IOException, InterruptedException, InvalidReturnCodeException {
        return httpOperationsService.getCode(httpClient, httpRequestFactory, candidate.getEmail());
    }

    private String createToken(String email, String generatedCode) {
        return Base64.getEncoder().encodeToString((email + ":" + generatedCode).getBytes());
    }

    private String createStatusJson(Status status, String token) throws JsonProcessingException {
        return jsonConverter.createStatusJson(status, token);
    }

    private void setStatus(String statusJson) throws IOException, InterruptedException, InvalidReturnCodeException {
        httpOperationsService.setStatus(httpClient, httpRequestFactory, statusJson);
    }
}
