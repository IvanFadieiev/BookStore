package project.bookstore.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import project.bookstore.dto.UserLoginRequestDto;
import project.bookstore.dto.UserLoginResponseDto;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private JwtUtil jwtUtil;
    private AuthenticationManager manager;
    public UserLoginResponseDto authenticate (UserLoginRequestDto request) {
        final Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }
}
