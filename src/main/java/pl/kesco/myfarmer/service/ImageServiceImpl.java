package pl.kesco.myfarmer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kesco.myfarmer.model.dto.image.ImageDto;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService{


    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    @Override
    public void readImage() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.imgur.com/3/image/3bwvs6d"))
                .header("Authorization", "Client-ID fd2a09d73fb6e4e")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        HttpHeaders headers = response.headers();
        headers.map().forEach((k, v) -> System.out.println(k + " : " + v));

        System.out.println(response.statusCode());

        System.out.println(response.body());

    }


    @Override
    public String uploadImage(MultipartFile file) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofByteArray(file.getBytes()))
                .uri(URI.create("https://api.imgur.com/3/upload"))
                .header("Authorization", "Client-ID fd2a09d73fb6e4e")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        HttpHeaders headers = response.headers();
        headers.map().forEach((k, v) -> System.out.println(k + " : " + v));

        System.out.println(response.statusCode());

        String jsonImageData = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        ImageDto image = objectMapper.readValue(jsonImageData, ImageDto.class);

        return image.getData().getLink();

    }


}
