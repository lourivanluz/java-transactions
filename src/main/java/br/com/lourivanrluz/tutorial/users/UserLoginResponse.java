package br.com.lourivanrluz.tutorial.users;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserLoginResponse(
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2VAZG9lLmNvbSIsImV4cCI6MTYyMzUwNzQwMH0.7")
    String token) {

}
