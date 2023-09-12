package project.bookstore.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.UserLoginRequestDto;
import project.bookstore.dto.UserLoginResponseDto;
import project.bookstore.dto.UserRegistrationRequestDto;
import project.bookstore.dto.UserRegistrationResponseDto;
import project.bookstore.exception.RegistrationException;
import project.bookstore.security.AuthenticationService;
import project.bookstore.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication management", description = "Endpoints for authentication")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public UserRegistrationResponseDto register(@RequestBody @Valid UserRegistrationRequestDto
                                     request) throws RegistrationException {
        return userService.register(request);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login (@RequestBody UserLoginRequestDto
                                              request) {
        return authenticationService.authenticate(request);
    }
}
