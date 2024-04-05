package br.com.lourivanrluz.tutorial.users;

import java.util.UUID;

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
    UUID id;
    @NotNull(message = "Field fullName is requered")
    String fullName;
    @NotNull(message = "Field password is requered")
    Long cpf;
    @NotNull(message = "Field email is requered")
    String email;
    @NotNull(message = "Field type is requered")
    Integer type;

    public UserDtoResponse userToDto(Users user) {
        return new UserDtoResponse(user.getId(), user.getFullName(), user.getCpf(), user.getEmail(),
                user.getType());
    }
}