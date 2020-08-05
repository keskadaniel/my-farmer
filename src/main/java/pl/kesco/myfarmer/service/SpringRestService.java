package pl.kesco.myfarmer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sangupta.jerry.constants.HttpStatusCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import pl.kesco.myfarmer.model.dto.image.ImageDto;

import java.io.IOException;

//@AllArgsConstructor
@Service
@Primary
@Slf4j
public class SpringRestService implements ImageService {

    @Value("${image.client.id}")
    private String clientId;

    public static final String POST_URI = "https://api.imgur.com/3/upload";

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SpringRestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void readImage(String imageId) throws IOException, InterruptedException {

    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException, InterruptedException {
        if (file.isEmpty()) {
            return "brak";
        }

        ResponseEntity<String> response = sendHttpRequest(file);

        if (response.getStatusCodeValue() != HttpStatusCode.OK) {
            log.warn("Could not upload image. Response status: {}", response.getStatusCodeValue());
            return "brak";
        }

        log.info("Successfully uploaded file: {}", file.getOriginalFilename());
        String jsonImageData = response.getBody();
        ImageDto image = objectMapper.readValue(jsonImageData, ImageDto.class);

        return image.getData().getLink();
    }


    private ResponseEntity<String> sendHttpRequest(MultipartFile file) throws IOException, InterruptedException {

        byte[] fileBytes = file.getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization", "Client-ID " + clientId);

        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("image")
                .filename("name")
                .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileBytes, fileMap);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", fileEntity);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);


        ResponseEntity<String> response = restTemplate.exchange(
                POST_URI,
                HttpMethod.POST,
                requestEntity,
                String.class);

        return response;
    }
}
