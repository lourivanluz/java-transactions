package br.com.lourivanrluz.tutorial.users;

import org.springframework.web.bind.annotation.RestController;

import br.com.lourivanrluz.tutorial.security.TokenService;
import br.com.lourivanrluz.tutorial.transaction.exeptions.ExeptionsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("auth")
@Tag(name = "Auth", description = "Endpoint para gerenciamento de usuario")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;


    public UserController(UserService userService, AuthenticationManager authenticationManager,
            TokenService tokenservice) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenservice;
    }

    @Operation(summary = "Cria um novo usuário", description = "cria e retorna um usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "usuario criado com sucesso"), 
        @ApiResponse(responseCode = "500" , description = "erro interno do servidor",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Requisição inválida", 
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = ExeptionsDto.class),
                examples = @ExampleObject(value = "{ \"message\": \"Campo 'fullName' é obrigatório\" }"))),
    })
    @PostMapping("/register")
    public ResponseEntity<UserDtoResponse> register(@RequestBody @Valid UserDto user) {

        Users userSaved = userService.createUser(user);
        UserDtoResponse userDto = new UserDtoResponse().userToDto(userSaved);
        return ResponseEntity.ok().body(userDto);
    }

    @Operation(summary = "Cria um novo usuário", description = "cria e retorna um usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "usuario criado com sucesso"),
        @ApiResponse(responseCode = "403" , description = "erro interno do servidor",
            content = @Content(mediaType = "application/json")), 
        @ApiResponse(responseCode = "500" , description = "erro interno do servidor",
            content = @Content(mediaType = "application/json")),
    })
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody @Valid UserLoginDto user) {
        var userPassword = new UsernamePasswordAuthenticationToken(user.email(), user.password());
        var auth = authenticationManager.authenticate(userPassword);
        String token = tokenService.generateToken((Users) auth.getPrincipal());
        return ResponseEntity.ok(new UserLoginResponse(token));
    }

}
