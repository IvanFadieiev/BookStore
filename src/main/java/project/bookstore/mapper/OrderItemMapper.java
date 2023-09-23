package project.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.bookstore.config.MapperConfiguration;
import project.bookstore.dto.order.OrderItemResponseDto;
import project.bookstore.model.OrderItem;

@Mapper(config = MapperConfiguration.class)
public interface OrderItemMapper {
    @Mapping(source = "orderItem.book.id", target = "bookId")
    OrderItemResponseDto toDto(OrderItem orderItem);
}
