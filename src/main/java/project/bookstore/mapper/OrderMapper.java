package project.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.bookstore.config.MapperConfiguration;
import project.bookstore.dto.order.OrderResponseDto;
import project.bookstore.model.Order;

@Mapper(config = MapperConfiguration.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "order.user.id", target = "userId")
    OrderResponseDto toDto(Order order);
}
