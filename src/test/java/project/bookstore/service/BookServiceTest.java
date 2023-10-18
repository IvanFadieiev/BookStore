package project.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.book.BookRequestDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.mapper.BookMapper;
import project.bookstore.model.Book;
import project.bookstore.model.Category;
import project.bookstore.repository.BookRepository;
import project.bookstore.service.impl.BookServiceImpl;
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

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final int ZERO_INDEX = 0;
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = -100L;
    private static final Pageable PAGEABLE = PageRequest.of(0, 10);
    private static final BookRequestDto BOOK_REQUEST_DTO = new BookRequestDto()
            .setTitle("Harry Potter")
            .setAuthor("J.K.Rolling")
            .setIsbn("0-2936-9647-0")
            .setPrice(BigDecimal.valueOf(1000))
            .setCategoriesIds(Set.of(1L));
    private static final Book BOOK = new Book()
            .setTitle("Harry Potter")
            .setAuthor("J.K.Rolling")
            .setIsbn("0-2936-9647-0")
            .setPrice(BigDecimal.valueOf(1000))
            .setCategories(Set.of(new Category(1L)));
    private static final BookDto BOOK_DTO = new BookDto()
                .setTitle("Harry Potter")
                .setAuthor("J.K.Rolling")
                .setIsbn("0-2936-9647-0")
                .setPrice(BigDecimal.valueOf(1000))
                .setCategoriesId(Set.of(1L));

    private static final List<Book> BOOKS = List.of(BOOK);
    private static final Page<Book> BOOK_PAGE = new PageImpl<>(BOOKS, PAGEABLE, BOOKS.size());

    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @Test
    @DisplayName("Verify create() method works")
    public void create_ValidBookRequestDto_ReturnsBookDto() {
        Mockito.when(bookMapper.toModel(BOOK_REQUEST_DTO)).thenReturn(BOOK);
        Mockito.when(bookRepository.save(BOOK)).thenReturn(BOOK);
        Mockito.when(bookMapper.toDto(BOOK)).thenReturn(BOOK_DTO);

        BookDto createdBookDto = bookService.create(BOOK_REQUEST_DTO);

        assertThat(createdBookDto).isEqualTo(BOOK_DTO);
    }

    @Test
    @DisplayName("Verify findById() method works")
    public void findById_ValidId_ReturnsValidBookDto() {
        Mockito.when(bookRepository.findById(VALID_ID)).thenReturn(Optional.of(BOOK));
        Mockito.when(bookMapper.toDto(BOOK)).thenReturn(BOOK_DTO);

        BookDto bookDtoFromSearch = bookService.findById(VALID_ID);

        assertThat(bookDtoFromSearch).isEqualTo(BOOK_DTO);
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
        Mockito.when(bookMapper.toModel(BOOK_REQUEST_DTO)).thenReturn(BOOK);
        Mockito.when(bookRepository.save(BOOK)).thenReturn(BOOK);
        Mockito.when(bookMapper.toDto(BOOK)).thenReturn(BOOK_DTO);

        BookDto updateBookDto = bookService.update(VALID_ID, BOOK_REQUEST_DTO);

        BigDecimal expected = BOOK_REQUEST_DTO.getPrice();
        BigDecimal actual = updateBookDto.getPrice();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify findAll() method words")
    public void findAll_findAllPageableRequest_ReturnsBookDtoList() {
        Mockito.when(bookRepository.findAll(PAGEABLE)).thenReturn(BOOK_PAGE);
        Mockito.when(bookMapper.toDto(BOOK)).thenReturn(BOOK_DTO);

        List<BookDto> bookDtoList = bookService.findAll(PAGEABLE);

        assertEquals(bookDtoList.size(), BOOKS.size());
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
        Mockito.when(bookRepository.findAllByCategoryId(VALID_ID)).thenReturn(BOOKS);
        Mockito.when(bookMapper.toDtoWithoutCategories(BOOK))
                .thenReturn(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> bookDtoList
                = bookService.findAllByCategoryId(VALID_ID);

        assertThat(bookDtoList.get(ZERO_INDEX)).isEqualTo(bookDtoWithoutCategoryIds);
    }
}
