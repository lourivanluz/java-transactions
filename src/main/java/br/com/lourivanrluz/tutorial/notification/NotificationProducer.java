package br.com.lourivanrluz.tutorial.notification;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import br.com.lourivanrluz.tutorial.transaction.Transaction;

@Service
public class NotificationProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public NotificationProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(String mensagem) {
        kafkaTemplate.send("Transaction-notification", mensagem);
    }
}
