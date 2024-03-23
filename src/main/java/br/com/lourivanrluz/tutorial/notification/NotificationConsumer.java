package br.com.lourivanrluz.tutorial.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import br.com.lourivanrluz.tutorial.transaction.Transaction;

@Service
public class NotificationConsumer {
    private RestClient restClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationConsumer.class);

    public NotificationConsumer(RestClient.Builder restclint) {
        this.restClient = restclint.baseUrl("https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6").build();
    }

    @KafkaListener(topics = "Transaction-notification", groupId = "tutorial-desafio")
    public void resiveNotification(Transaction transaction) {
        LOGGER.info("Notifying transaction {}...", transaction);
        var response = restClient.get().retrieve().toEntity(Notification.class);
        if (response.getStatusCode().isError() || !response.getBody().message()) {
            throw new NotificationException("Error seding notification");
        }
        LOGGER.info("nofication has been sent {}", response.getBody());

    }
}
