package project.bookstore.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import project.bookstore.config.MapperConfiguration;
import project.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import project.bookstore.model.ShoppingCart;

@Mapper(config = MapperConfiguration.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", ignore = true)
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);

    @AfterMapping
    default void setUserId(@MappingTarget ShoppingCartResponseDto shoppingCartResponseDto,
                           ShoppingCart shoppingCart) {
        shoppingCartResponseDto.setUserId(shoppingCart.getUser().getId());
    }
}
