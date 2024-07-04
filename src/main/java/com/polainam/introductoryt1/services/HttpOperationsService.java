package com.polainam.introductoryt1.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polainam.introductoryt1.HttpRequestFactory;
import com.polainam.introductoryt1.exceptions.InvalidReturnCodeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class HttpOperationsService {

    private final HttpClient httpClient;

    @Value("${server}")
    private String server;

    public HttpOperationsService() {
        httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
    }

    public List<String> getRoles(HttpRequestFactory httpRequestFactory) throws IOException, InterruptedException, InvalidReturnCodeException {
        HttpRequest httpRequest = httpRequestFactory.createGetHttpRequest(server + "/api/get-roles");
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        handleHttpResponse(httpResponse);
        String json = httpResponse.body();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        List<String> roles = new ArrayList<>();
        for (JsonNode role : jsonNode.get("roles")) {
            roles.add(role.textValue());
        }
        return roles;
    }

    public void signUpCandidate(HttpRequestFactory httpRequestFactory, String candidateJson) throws IOException, InterruptedException, InvalidReturnCodeException {
        String uri = server + "/api/sign-up";
        String contentType = "application/json";
        HttpRequest httpRequest = httpRequestFactory.createPostHttpRequest(uri, contentType, candidateJson);
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        handleHttpResponse(httpResponse);
    }

    public String getCode(HttpRequestFactory httpRequestFactory, String email) throws IOException, InterruptedException, InvalidReturnCodeException {
        String uri = server + "/api/get-code?email=" + email;
        HttpRequest httpRequest = httpRequestFactory.createGetHttpRequest(uri);
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        handleHttpResponse(httpResponse);

        String generatedCode = httpResponse.body();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(generatedCode);
        return jsonNode.textValue();
    }

    public void setStatus(HttpRequestFactory httpRequestFactory, String statusJson) throws IOException, InterruptedException, InvalidReturnCodeException {
        String uri = server + "/api/set-status";
        String contentType = "application/json";
        HttpRequest httpRequest = httpRequestFactory.createPostHttpRequest(uri, contentType, statusJson);
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        handleHttpResponse(httpResponse);
        String responseBody = httpResponse.body();
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.readValue(responseBody, String.class);

        System.out.println(result);
    }

    private void handleHttpResponse(HttpResponse<String> httpResponse) throws InvalidReturnCodeException {
        HttpStatus httpStatus = HttpStatus.valueOf(httpResponse.statusCode());
        if (!httpStatus.is2xxSuccessful()) {
            throw new InvalidReturnCodeException("Unexpected return code: " + httpStatus.value() + ".\n" +
                    "Response body: " + httpResponse.body());
        }
    }
}
