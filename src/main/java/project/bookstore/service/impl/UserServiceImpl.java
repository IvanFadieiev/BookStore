package project.bookstore.service.impl;

import static project.bookstore.roles.RoleName.ROLE_USER;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.bookstore.dto.user.UserRegistrationRequestDto;
import project.bookstore.dto.user.UserRegistrationResponseDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.exception.RegistrationException;
import project.bookstore.mapper.UserMapper;
import project.bookstore.model.Role;
import project.bookstore.model.User;
import project.bookstore.repository.RoleRepository;
import project.bookstore.repository.UserRepository;
import project.bookstore.service.ShoppingCartService;
import project.bookstore.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("User with such credentials already exists");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role role = roleRepository.getRoleByName(ROLE_USER);
        user.setRoles(Set.of(role));
        User savedUser = userRepository.save(user);
        shoppingCartService.createNewShoppingCartForNewUser(savedUser);
        return userMapper.toDto(savedUser);
    }

    @Override
    public User getAuthentificatedUser() {
        return userRepository
                .findByEmail(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(()
                        -> new EntityNotFoundException("User with "
                        + "provided name doesn't exists"));
    }
}
