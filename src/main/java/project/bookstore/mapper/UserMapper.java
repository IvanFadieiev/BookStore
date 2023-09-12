package project.bookstore.mapper;

import org.mapstruct.Mapper;
import project.bookstore.config.MapperConfiguration;
import project.bookstore.dto.UserRegistrationResponseDto;
import project.bookstore.dto.UserRegistrationRequestDto;
import project.bookstore.model.User;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper {
    UserRegistrationResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto userDto);
}
