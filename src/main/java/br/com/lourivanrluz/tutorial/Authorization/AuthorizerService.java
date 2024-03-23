package br.com.lourivanrluz.tutorial.Authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import br.com.lourivanrluz.tutorial.transaction.Transaction;

@Service
public class AuthorizerService {

    private RestClient restClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizerService.class);

    public AuthorizerService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc").build();
    }

    public void authorize(Transaction transaction) {
        LOGGER.info("AUTHORIZING TRANSACTION {}", transaction);
        var response = restClient.get().retrieve().toEntity(Authorization.class);

        if (response.getStatusCode().isError() || !response.getBody().isAuthorized()) {
            throw new unauthorizedException("Unauthorized transaction");
        }
        LOGGER.info("TRANSACTION AUTHORIZED {}", transaction);
    }

}
