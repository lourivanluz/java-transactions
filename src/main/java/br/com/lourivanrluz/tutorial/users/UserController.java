package br.com.lourivanrluz.tutorial.users;

import org.springframework.web.bind.annotation.RestController;

import br.com.lourivanrluz.tutorial.security.TokenService;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("auth")
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

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserDto user) {

        Users userSaved = userService.createUser(user);
        UserDtoResponse userDto = new UserDtoResponse().userToDto(userSaved);
        return ResponseEntity.ok().body(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserLoginDto user) {
        var userPassword = new UsernamePasswordAuthenticationToken(user.email(), user.password());
        var auth = authenticationManager.authenticate(userPassword);
        String token = tokenService.generateToken((Users) auth.getPrincipal());
        return ResponseEntity.ok(new UserLoginResponse(token));
    }

}
