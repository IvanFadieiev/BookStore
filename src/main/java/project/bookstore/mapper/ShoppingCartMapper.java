package project.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.bookstore.config.MapperConfiguration;
import project.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import project.bookstore.model.ShoppingCart;

@Mapper(config = MapperConfiguration.class)
public interface ShoppingCartMapper {
    @Mapping(source = "shoppingCart.user.id", target = "userId")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);
}
