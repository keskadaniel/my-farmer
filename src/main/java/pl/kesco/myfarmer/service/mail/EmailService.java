package pl.kesco.myfarmer.service.mail;

public interface EmailService {

    void sendMessage(String to, String subject, String content);
}
