package practicum.manager.network;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String url;
    private final String API_KEY;

    public KVTaskClient(String url) {
        this.url = url;
        this.API_KEY = register();
    }

    private String register() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/register"))
                .GET()
                .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                System.out.println("Something went wrong. The Server returned a status code: " + response.statusCode());
            }
        } catch (NullPointerException | IOException | InterruptedException e) {
            System.out.println("An error occurred while executing the request.\n" +
                    "Please check the address and try again");
        }
        return null;
    }

    public void put(String key, String json) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/save/" + key + "?API_KEY=" + API_KEY))
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Value for key " + key + " updated successfully!");
            } else {
                System.out.println("Something went wrong. The Server returned a status code: " + response.statusCode());
            }
        } catch (NullPointerException | IOException | InterruptedException e) {
            System.out.println("An error occurred while executing the request.\n" +
                    "Please check the address and try again.");
        }
    }

    public String load(String key) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/load/" + key + "?API_KEY=" + API_KEY))
                .GET()
                .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Value for key " + key + " got successfully");
                return response.body();
            } else {
                System.out.println("Something went wrong. The Server returned a status code: " + response.statusCode());
            }
        } catch (NullPointerException | IOException | InterruptedException e) {
            System.out.println("An error occurred while executing the request.\n" +
                    "Please check the address and try again.");
        }
        return null;
    }
}
