package br.com.lourivanrluz.tutorial.authorization;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.lourivanrluz.tutorial.transaction.Transaction;

@Service
public class AuthorizerService {

    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizerService.class);

    public AuthorizerService() {
        this.restTemplate = createRestTemplateWithTimeouts(Duration.ofSeconds(11)); // Timeout de 5 segundos
    }

    public void authorize(Transaction transaction) {
        LOGGER.info("AUTHORIZING TRANSACTION");

        try {
            String API_URL_UNSTABLE = env.getProperty("API_UNSTABLE");
            LOGGER.info(API_URL_UNSTABLE + "/authorization");
            ResponseEntity<Authorization> response = restTemplate.getForEntity(
            API_URL_UNSTABLE + "/authorization",
            Authorization.class);
            Authorization authorization = response.getBody();
            if (authorization == null || response.getStatusCode().isError() || !authorization.isAuthorized()) {
            throw new unauthorizedException("Unauthorized transaction");
            }

            LOGGER.info("TRANSACTION AUTHORIZED");

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
