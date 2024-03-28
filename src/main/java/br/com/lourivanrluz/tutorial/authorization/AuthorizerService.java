package br.com.lourivanrluz.tutorial.authorization;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import br.com.lourivanrluz.tutorial.transaction.Transaction;

@Service
public class AuthorizerService {

    private RestTemplate restTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizerService.class);

    public AuthorizerService() {
        this.restTemplate = createRestTemplateWithTimeouts(Duration.ofSeconds(7)); // Timeout de 5 segundos
    }

    public void authorize(Transaction transaction) {
        LOGGER.info("AUTHORIZING TRANSACTION {}", transaction);

        try {
            ResponseEntity<Authorization> response = restTemplate.getForEntity(
                    "http://localhost:8080/mock/authorization",
                    Authorization.class);

            if (response.getStatusCode().isError() || response.getBody().authorized()) {
                throw new unauthorizedException("Unauthorized transaction");
            }

            LOGGER.info("TRANSACTION AUTHORIZED {}", transaction);

        } catch (HttpServerErrorException ex) {
            LOGGER.error("HttpServerErrorException: {}", ex.getMessage());
            throw new unauthorizedException("sistema fora do ar");
        } catch (Exception ex) {
            LOGGER.error("Erro ao acessar o serviço externo: {}", ex.getMessage());
            throw new unauthorizedException("sistema fora do ar");
        }

    }

    private RestTemplate createRestTemplateWithTimeouts(Duration timeout) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout((int) timeout.toMillis());
        requestFactory.setReadTimeout((int) timeout.toMillis());

        return new RestTemplate(requestFactory);
    }
}
