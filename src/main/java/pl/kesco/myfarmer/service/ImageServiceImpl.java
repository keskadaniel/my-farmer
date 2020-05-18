package pl.kesco.myfarmer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sangupta.jerry.constants.HttpStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kesco.myfarmer.model.dto.image.ImageDto;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


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
