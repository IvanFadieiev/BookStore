package project.bookstore.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.book.BookRequestDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.mapper.BookMapper;
import project.bookstore.model.Book;
import project.bookstore.model.Category;
import project.bookstore.repository.BookRepository;
import project.bookstore.service.impl.BookServiceImpl;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final int ZERO_INDEX = 0;
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = -100L;
    private static final Pageable PAGEABLE = PageRequest.of(0, 10);
    private static final BookRequestDto bookRequestDto = new BookRequestDto()
            .setTitle("Harry Potter")
            .setAuthor("J.K.Rolling")
            .setIsbn("0-2936-9647-0")
            .setPrice(BigDecimal.valueOf(1000))
            .setCategoriesIds(Set.of(1L));
    private static final Book book = new Book()
            .setTitle("Harry Potter")
            .setAuthor("J.K.Rolling")
            .setIsbn("0-2936-9647-0")
            .setPrice(BigDecimal.valueOf(1000))
            .setCategories(Set.of(new Category(1L)));
    private static final BookDto bookDto = new BookDto()
            .setTitle("Harry Potter")
            .setAuthor("J.K.Rolling")
            .setIsbn("0-2936-9647-0")
            .setPrice(BigDecimal.valueOf(1000))
            .setCategoriesId(Set.of(1L));

    private static final List<Book> books = List.of(book);
    private static final Page<Book> bookPage = new PageImpl<>(books, PAGEABLE, books.size());

    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @Test
    @DisplayName("Verify create() method works")
    public void create_ValidBookRequestDto_ReturnsBookDto() {
        Mockito.when(bookMapper.toModel(bookRequestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto createdBookDto = bookService.create(bookRequestDto);

        assertThat(createdBookDto).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Verify findById() method works")
    public void findById_ValidId_ReturnsValidBookDto() {
        Mockito.when(bookRepository.findById(VALID_ID)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto bookDtoFromSearch = bookService.findById(VALID_ID);

        assertThat(bookDtoFromSearch).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Verify findById() method throws exception with invalid parameter")
    public void findById_InValidId_ThrowsException() {
        Mockito.when(bookRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(INVALID_ID));

        String expected = "Book by provided id = " + INVALID_ID + " doesn't exists";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify update() method works")
    public void update_ValidBookRequestDto_ReturnsBookDto() {
        Mockito.when(bookMapper.toModel(bookRequestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto updateBookDto = bookService.update(VALID_ID, bookRequestDto);

        BigDecimal expected = bookRequestDto.getPrice();
        BigDecimal actual = updateBookDto.getPrice();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify findAll() method words")
    public void findAll_findAllPageableRequest_ReturnsBookDtoList() {
        Mockito.when(bookRepository.findAll(PAGEABLE)).thenReturn(bookPage);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> bookDtoList = bookService.findAll(PAGEABLE);

        assertEquals(bookDtoList.size(), books.size());
    }

    @Test
    @DisplayName("Verify findAllByCategoryId() method words")
    public void findAllByCategoryId_ValidCategoryId_ReturnsBookList() {
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds
                = new BookDtoWithoutCategoryIds("Harry Potter",
                "J.K.Rolling",
                "0-2936-9647-0",
                BigDecimal.valueOf(1000),
                "Description",
                "Cover Image");
        Mockito.when(bookRepository.findAllByCategoryId(VALID_ID)).thenReturn(books);
        Mockito.when(bookMapper.toDtoWithoutCategories(book))
                .thenReturn(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> bookDtoList
                = bookService.findAllByCategoryId(VALID_ID);

        assertThat(bookDtoList.get(ZERO_INDEX)).isEqualTo(bookDtoWithoutCategoryIds);
    }
}
