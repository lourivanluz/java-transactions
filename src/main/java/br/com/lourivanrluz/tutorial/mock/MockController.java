package br.com.lourivanrluz.tutorial.mock;

import java.time.Duration;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lourivanrluz.tutorial.authorization.Authorization;
import br.com.lourivanrluz.tutorial.notification.Notification;

@RestController
public class MockController {

    @GetMapping("/mock/authorization")
    public ResponseEntity<Authorization> getAuthorization() {

        Random random = new Random();
        // Gerar um número aleatório entre 1 e 10 (inclusive)
        int randomNumber = random.nextInt(10) + 1;
        try {
            Thread.sleep(randomNumber * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String message = randomNumber % 2 == 0 ? "Autorizado" : "não autorizado";
        Authorization authorization = new Authorization(message, randomNumber % 2 == 0);
        return ResponseEntity.ok(authorization);
    }

    @GetMapping("/mock/notification")
    public ResponseEntity<Notification> getNotification() {

        Random random = new Random();
        // Gerar um número aleatório entre 1 e 10 (inclusive)
        int randomNumber = random.nextInt(10) + 1;
        try {
            Thread.sleep(randomNumber * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Notification authorization = new Notification(true);
        return ResponseEntity.ok(authorization);
    }
}