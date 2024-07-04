package com.polainam.introductoryt1;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

@Component
public class HttpRequestFactory {
    public HttpRequest createGetHttpRequest(String uri) {
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();
    }

    public HttpRequest createPostHttpRequest(String uri, String contentType, String content) {
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .headers("Content-Type", contentType)
                .version(HttpClient.Version.HTTP_1_1)
                .POST(HttpRequest.BodyPublishers.ofString(content))
                .build();
    }
}
