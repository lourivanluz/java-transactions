package br.com.lourivanrluz.tutorial.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.lourivanrluz.tutorial.users.Users;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Autowired
    private Environment env;

    public String generateToken(Users user) {
        String security = env.getProperty("SECRET.GENERETETOKEN.SALT");
        try {
            Algorithm algorithm = Algorithm.HMAC256(security);
            String token = JWT.create()
                    .withIssuer("transaction-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(genereteExpiration())
                    .sign(algorithm);
            return token;

        } catch (JWTCreationException e) {
            throw new TokenException("Create token error");
        }
    }

    public String validateToken(String token) {
        try {
            String security = env.getProperty("SECRET.GENERETETOKEN.SALT");
            Algorithm algorithm = Algorithm.HMAC256(security);
            String email = JWT.require(algorithm)
                    .withIssuer("transaction-api")
                    .build()
                    .verify(token)
                    .getSubject();
            return email;

        } catch (JWTVerificationException e) {
            return "";
        }
    }

    private Instant genereteExpiration() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
