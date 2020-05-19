package pl.kesco.myfarmer.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface ImageService {

    void readImage(String imageId) throws IOException, InterruptedException;
    String uploadImage(MultipartFile file) throws IOException, InterruptedException;

}
