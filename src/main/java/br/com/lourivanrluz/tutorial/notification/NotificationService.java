package br.com.lourivanrluz.tutorial.notification;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationProducer notificationProducer;

    public NotificationService(NotificationProducer notificationProducer) {
        this.notificationProducer = notificationProducer;
    }

    public void notify(String mensagem) {
        notificationProducer.sendNotification(mensagem);
    }
}
