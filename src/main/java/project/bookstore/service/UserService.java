package project.bookstore.service;

import project.bookstore.dto.UserLoginRequestDto;
import project.bookstore.dto.UserLoginResponseDto;
import project.bookstore.dto.UserRegistrationRequestDto;
import project.bookstore.dto.UserRegistrationResponseDto;
import project.bookstore.exception.RegistrationException;

public interface UserService {
    UserRegistrationResponseDto register (UserRegistrationRequestDto requestDto)
            throws RegistrationException;

    UserLoginResponseDto login (UserLoginRequestDto requestDto);
}
