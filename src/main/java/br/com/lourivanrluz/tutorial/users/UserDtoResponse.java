package br.com.lourivanrluz.tutorial.users;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDtoResponse {

    @Id
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    UUID id;

    @Schema(example = "joe da silva")
    @NotNull(message = "Field fullName is requered")
    String fullName;

    @Schema(example = "12345678901")
    @NotNull(message = "Field password is requered")
    Long cpf;

    @Schema(example = "joe@poggers.com")
    @NotNull(message = "Field email is requered")
    String email;

    @Schema(example = "1")
    @NotNull(message = "Field type is requered")
    Integer type;

    public UserDtoResponse userToDto(Users user) {
        return new UserDtoResponse(user.getId(), user.getFullName(), user.getCpf(), user.getEmail(),
                user.getType());
    }
}