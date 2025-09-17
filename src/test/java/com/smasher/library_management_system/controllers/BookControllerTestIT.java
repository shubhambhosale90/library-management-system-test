package com.smasher.library_management_system.controllers;

import com.smasher.library_management_system.TestContainerConfiguration;
import com.smasher.library_management_system.advice.ApiResponse;
import com.smasher.library_management_system.dtos.BookDto;
import com.smasher.library_management_system.entities.Book;
import com.smasher.library_management_system.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@Import(TestContainerConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTestIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BookRepository bookRepository;

    private Book testBook;
    private BookDto testBookDto;

    @BeforeEach
    void setUp() {
        testBook = Book.builder()
                .price(BigDecimal.valueOf(2000))
                .title("Atomic Habits")
                .publishDate(LocalDate.of(2025, 2, 10))
                .publication("AH Publications")
                .build();

        testBookDto = BookDto.builder()
                .id(1L)
                .price(BigDecimal.valueOf(2000))
                .title("Atomic Habits")
                .publishDate(LocalDate.of(2025, 2, 10))
                .publication("AH Publications")
                .build();

        bookRepository.deleteAll();
    }

    @Test
    void testCreateNewBook_success() {
        webTestClient.post()
                .uri("/book")
                .bodyValue(testBook)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class);

    }

    @Test
    void testGetAllBooks_whenBooksArePresent_thenReturnBooks() {
        bookRepository.save(testBook);
        webTestClient.get()
                .uri("/book")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ApiResponse<List<BookDto>>>() {})
                .value(response -> {
                    List<BookDto> books = response.getData();
                    assertThat(books).isNotNull();
                    assertThat(books.get(0).getTitle()).isEqualTo(testBook.getTitle());

                });
    }

    @Test
    void testGetAllBooks_whenBooksAreNotPresent_thenThrowException() {
        webTestClient.get()
                .uri("/book")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetBookById_whenBookIsPresent_thenReturnBookDto() {
        testBook = bookRepository.save(testBook);
        Long id = testBook.getId();
        webTestClient.get()
                .uri("/book/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ApiResponse<BookDto>>() {
                })
                .value(response -> {
                    BookDto book = response.getData();
                    assertThat(book).isNotNull();
                    assertThat(book.getTitle()).isEqualTo(testBook.getTitle());
                });
    }

    @Test
    void testGetBookById_whenBookIsNotPresent_thenThrowException() {
        Long id = 1L;

        webTestClient.get()
                .uri("/book/{id}", id)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateBookById_whenBookIsPresent_thenUpdateBookAndReturnBookDto() {
        Book savedBook = bookRepository.save(testBook);
        Long id = savedBook.getId();

        Book updatedRequest = new Book();
        updatedRequest.setTitle("Demo");
        updatedRequest.setPublication("Demo Publication");

        webTestClient.put()
                .uri("/book/{id}", id)
                .bodyValue(updatedRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ApiResponse<BookDto>>() {
                })
                .value(response -> {
                    BookDto book = response.getData();
                    assertThat(book).isNotNull();
                    assertThat(book.getTitle()).isNotEqualTo(savedBook.getTitle());
                    assertThat(book.getPublication()).isNotEqualTo(savedBook.getPublication());
                });
    }

    @Test
    void testUpdateBookById_whenBookIdIsNotPresent_thenThrowException() {
        Long id = 1L;

        webTestClient.put()
                .uri("/book/{id}", id)
                .bodyValue(testBook)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteBookById_whenBookIdIsPresent_thenDeleteBook() {
        Book savedBook = bookRepository.save(testBook);
        Long id = savedBook.getId();

        webTestClient.delete()
                .uri("/book/{id}", id)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteBookById_whenBookIdIsNotPresent_thenThrowException() {
        Long id = 1L;

        webTestClient.delete()
                .uri("/book/{id}", id)
                .exchange()
                .expectStatus().isNotFound();
    }


}