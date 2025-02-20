package br.com.lourivanrluz.tutorial.mailSend;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(Email email) {

        var message = new SimpleMailMessage();
        message.setTo(email.getTo());
        message.setFrom("noreplay@email.com");
        message.setSubject(email.getSubject());
        message.setText(email.getBody());

        mailSender.send(message);
    }
}
