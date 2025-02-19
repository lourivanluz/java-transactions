package br.com.lourivanrluz.tutorial.users;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserLoginDto(
    @Schema(example = "joe@poggers.com")
    String email, 
    
    @Schema(example = "passwordSeguro")
    String password) {

}
