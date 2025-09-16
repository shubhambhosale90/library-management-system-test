package com.smasher.library_management_system.services;

import com.smasher.library_management_system.dtos.BookDto;
import com.smasher.library_management_system.entities.Book;
import com.smasher.library_management_system.exceptions.ResourceNotFoundException;
import com.smasher.library_management_system.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Spy
    private ModelMapper modelMapper;

    private Book mockBook;
    private BookDto mockBookDto;

    @BeforeEach
    void setUp() {
        mockBook = Book.builder()
                .id(1L)
                .title("Morning Habits")
                .price(BigDecimal.valueOf(780))
                .publication("MB Publications")
                .publishDate(LocalDate.of(2025, 2, 6))
                .build();

        mockBookDto = modelMapper.map(mockBook, BookDto.class);
    }

    @Test
    void testCreateNewBook_whenBookIsPassed_thenReturnBookDto() {
//        Assign
        when(bookRepository.save(any(Book.class))).thenReturn(mockBook);

//        Act
        BookDto bookDto = bookService.createNewBook(mockBookDto);

//        Assert
        assertThat(bookDto).isNotNull();
        assertThat(bookDto.getTitle()).isEqualTo(mockBook.getTitle());

        verify(bookRepository, only()).save(any(Book.class));
    }

    @Test
    void testGetAllBooks_whenBooksArePresent_thenReturnBooksList() {
//        Assign
        when(bookRepository.findAll()).thenReturn(List.of(mockBook));

//        Act
        List<BookDto> books = bookService.getAllBooks();

//        Assert
        assertThat(books).isNotNull()
                .isNotEmpty();
    }

    @Test
    void testGetAllBooks_whenBooksAreNotPresent_thenThrowException() {
//        Assign
        when(bookRepository.findAll()).thenReturn(List.of());

//        Act and Assert
        assertThatThrownBy(() -> bookService.getAllBooks())
                .hasMessage("Books are not found")
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testGetBookById_whenBookIdIsPresent_thenReturnAuthorDto() {
//        Assign
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.of(mockBook));

//        Act
        BookDto bookDto = bookService.getBookById(id);

//        Assert
        assertThat(bookDto).isNotNull();
        assertThat(bookDto.getTitle()).isEqualTo(mockBook.getTitle());

        verify(bookRepository, only()).findById(id);
    }

    @Test
    void testGetBookById_whenBookIdIsNotPresent_thenThrowException() {
//        Assign
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

//        Act and Assert
        assertThatThrownBy(() -> bookService.getBookById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Book not found with ID : "+id);
    }

    @Test
    void testUpdateBookById_whenBookIdIsPresent_thenUpdateBookAndReturnBookDto() {
//        Assign
        Long id = 1L;
        BookDto updatableBook = modelMapper.map(mockBook, BookDto.class);
        updatableBook.setPrice(BigDecimal.valueOf(200));
        updatableBook.setTitle("New Title");
        when(bookRepository.findById(id)).thenReturn(Optional.of(mockBook));
        mockBook.setPrice(BigDecimal.valueOf(200));
        mockBook.setTitle("New Title");
        when(bookRepository.save(any(Book.class))).thenReturn(mockBook);

//        Act
        BookDto bookDto = bookService.updateBookById(id, updatableBook);

//        Assert
        assertThat(bookDto).isNotNull();
        assertThat(bookDto.getId()).isEqualTo(mockBookDto.getId());
        assertThat(bookDto.getTitle()).isNotEqualTo(mockBookDto.getTitle());
        assertThat(bookDto.getPrice()).isNotEqualTo(mockBookDto.getPrice());
    }

    @Test
    void testUpdateBookById_whenBookIdIsNotPresent_thenThrowException() {
//        Assign
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

//        Act and Assert
        assertThatThrownBy(() -> bookService.updateBookById(id, any(BookDto.class)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Book not found with ID : "+id);
    }

    @Test
    void testDeleteBookById_whenBookIdIsPresent_thenDeleteBook() {
//        Assign
        Long id = 1L;
        when(bookRepository.existsById(id)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(id);

//        Act
        bookService.deleteBookById(id);

//        Assert
        verify(bookRepository, atMostOnce()).deleteById(id);
    }

    @Test
    void testDeleteBookById_whenBookIdIsNotPresent_thenThrowException() {
//        Assign
        Long id = 1L;
        when(bookRepository.existsById(id)).thenReturn(false);

//        Act
        assertThatThrownBy(() -> bookService.deleteBookById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Book not found with ID : "+id);

    }
}