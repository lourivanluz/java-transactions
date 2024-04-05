package br.com.lourivanrluz.tutorial.users;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("cadastro")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity register(@RequestBody @Valid UserDto user) {
        Users userSaved = userService.createUser(user);
        UserDtoResponse userDto = new UserDtoResponse().userToDto(userSaved);
        return ResponseEntity.ok().body(userDto);
    }

}
