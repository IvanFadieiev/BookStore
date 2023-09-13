package project.bookstore.mapper;

import org.mapstruct.Mapper;
import project.bookstore.config.MapperConfiguration;
import project.bookstore.dto.user.UserRegistrationRequestDto;
import project.bookstore.dto.user.UserRegistrationResponseDto;
import project.bookstore.model.User;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper {
    UserRegistrationResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto userDto);
}
