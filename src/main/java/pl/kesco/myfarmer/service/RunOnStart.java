package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RunOnStart {

    private final ImageService imageService;

    @EventListener(ContextRefreshedEvent.class)
    public void someEvent() throws IOException, InterruptedException {

        imageService.uploadImage();

    }
}
