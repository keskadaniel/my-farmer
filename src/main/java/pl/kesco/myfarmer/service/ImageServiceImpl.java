package pl.kesco.myfarmer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sangupta.jerry.constants.HttpStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import pl.kesco.myfarmer.model.dto.image.ImageDto;

import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Value("${image.client.id}")
    private String clientId;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void readImage(String imageId) throws IOException, InterruptedException {

        String clientId = "";

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.imgur.com/3/image/" + imageId))
                .header("Authorization", "Client-ID " + clientId)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        log.info("Status code: {}", response.statusCode());
    }


    @Override
    public String uploadImage(final MultipartFile file) throws IOException, InterruptedException {

        if (file.isEmpty()) {
            return "brak";
        }

        sendTokenRequest();

        HttpResponse<String> response = sendHttpRequest(file);

        if (response.statusCode() != HttpStatusCode.OK) {
            log.warn("Could not upload image. Response status: {}", response.statusCode());
            return "brak";
        }

        log.info("Successfully uploaded file: {}", file.getOriginalFilename()
        );
        String jsonImageData = response.body();
        ImageDto image = objectMapper.readValue(jsonImageData, ImageDto.class);

        return image.getData().getLink();
    }

    private void sendTokenRequest() throws IOException, InterruptedException {

        Map<Object, Object> data = new HashMap<>();
        data.put("refresh_token", "4c48cb7d0014e52d7f3e166cf713275406d6283d");
        data.put("client_id", "fd2a09d73fb6e4e");
        data.put("client_secret", "00266ad679fb14668ef8c904348d62a2befcebbe");
        data.put("grant_type", "refresh_token");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(ofFormData(data))
                .uri(URI.create("https://api.imgur.com/oauth2/token"))
                .build();

        HttpResponse<String> send = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        log.info("T O K E N");

    }

    public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

    private HttpResponse<String> sendHttpRequest(MultipartFile file) throws IOException, InterruptedException {

        byte[] fileBytes = file.getBytes();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofByteArray(fileBytes))
                .uri(URI.create("https://api.imgur.com/3/upload"))
                .header("Authorization", "Client-ID " + clientId)
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }


}
