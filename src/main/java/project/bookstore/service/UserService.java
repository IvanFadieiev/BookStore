package project.bookstore.service;

import project.bookstore.dto.user.UserRegistrationRequestDto;
import project.bookstore.dto.user.UserRegistrationResponseDto;
import project.bookstore.exception.RegistrationException;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;
}
