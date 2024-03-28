package br.com.lourivanrluz.tutorial.notification;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.lourivanrluz.tutorial.mailSend.Email;
import br.com.lourivanrluz.tutorial.mailSend.EmailService;

@Service
@PropertySource("classpath:application.properties")
public class NotificationConsumer {
    private RestTemplate restTemplate;
    private EmailService emailservice;
    @Autowired
    private Environment env;
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationConsumer.class);

    public NotificationConsumer(EmailService emailService) {
        this.emailservice = emailService;
        this.restTemplate = createRestTemplateWithTimeouts(Duration.ofSeconds(8));
    }

    @KafkaListener(topics = "Transaction-notification", groupId = "tutorial-desafio")
    public void resiveNotification(String mensagem) {
        LOGGER.info("Notifying transaction {}...", mensagem);
        try {
            ResponseEntity<Notification> response = restTemplate.getForEntity(
                    "http://localhost:8080/mock/notification",
                    Notification.class);
            if (response.getStatusCode().isError() || !response.getBody().message()) {
                throw new NotificationException("Error seding notification");
            }

            if (env.getProperty("SEND_EMAIL").equals("true")) {
                LOGGER.info("Notifying transaction {}...", mensagem);
                try {
                    emailservice.sendEmail(new Email(mensagem));
                } catch (Exception e) {
                    LOGGER.info("error send email {}", e.getMessage());
                }

            }

            LOGGER.info("nofication has been sent {}", "OK");

        } catch (HttpServerErrorException ex) {
            LOGGER.error("HttpServerErrorException: {}", ex.getMessage());
            throw new NotificationException("notificaçao fora do ar");
        } catch (Exception ex) {
            LOGGER.error("Erro ao acessar o serviço externo: {}", ex.getMessage());
            throw new NotificationException("notificaçao fora do ar");
        }
    }

    private RestTemplate createRestTemplateWithTimeouts(Duration timeout) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout((int) timeout.toMillis());
        requestFactory.setReadTimeout((int) timeout.toMillis());

        return new RestTemplate(requestFactory);
    }
}
