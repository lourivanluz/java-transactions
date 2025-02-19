package br.com.lourivanrluz.tutorial.users;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserDto extends UserDtoResponse {
    @Schema(example = "passwordSeguro")
    @NotNull(message = "Field password is requered")
    String password;

    public UserDto(UUID id, String fullName, Long cpf, String email, Integer type, String password) {
        super(id, fullName, cpf, email, type);
        this.password = password;
    }

    public static Users dtoToUser(UserDto userDto) {
        return new Users(null, userDto.getFullName(), userDto.getPassword(), userDto.getCpf(), userDto.getEmail(),
                userDto.getType(), UserRoleType.COMMUM, null);
    }
}