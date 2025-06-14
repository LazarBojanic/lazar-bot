package com.lazar.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiClient {
    private HttpClient httpClient;
    private String baseUri;
    public ApiClient(String baseUri){
        this.baseUri = baseUri;
        this.httpClient = HttpClient.newHttpClient();
    }
    public HttpResponse<String> get(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUri + endpoint))
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> post(String endpoint, Object object) throws IOException, InterruptedException {
        String objectJsonString = Util.objectToJsonString(object);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUri + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectJsonString))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
