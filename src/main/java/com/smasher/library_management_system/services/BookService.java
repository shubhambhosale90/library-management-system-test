package com.smasher.library_management_system.services;

import com.smasher.library_management_system.dtos.BookDto;
import com.smasher.library_management_system.entities.Book;
import com.smasher.library_management_system.exceptions.ResourceNotFoundException;
import com.smasher.library_management_system.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public BookDto createNewBook(BookDto bookDto) {
        log.info("Creating new Book with title : {}", bookDto.getTitle());
        Book book = modelMapper.map(bookDto, Book.class);
        Book savedBook = bookRepository.save(book);
        log.info("Successfully created new Book with title : {}", bookDto.getTitle());
        return modelMapper.map(savedBook, BookDto.class);
    }

    public List<BookDto> getAllBooks() {
        log.info("Fetching all books");
        List<Book> books = bookRepository.findAll();
        if(books.isEmpty()) {
            log.error("Books are not found");
            throw new ResourceNotFoundException("Books are not found");
        }
        log.info("Successfully fetched books");
        return books.stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .toList();
    }

    public BookDto getBookById(Long bookId) {
        log.info("Fetching Book with ID : {}", bookId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                   log.info("Book not found with ID : {}", bookId);
                   return new ResourceNotFoundException("Book not found with ID : "+bookId);
                });
        log.info("Successfully fetched book with ID : {}", bookId);
        return modelMapper.map(book, BookDto.class);
    }

    public BookDto updateBookById(Long bookId, BookDto bookDto) {
        log.info("Updating Book with ID : {}", bookId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    log.info("Book not found with ID : {}", bookId);
                    return new ResourceNotFoundException("Book not found with ID : "+bookId);
                });
        modelMapper.map(bookDto, book);
        book.setId(bookId);
        book = bookRepository.save(book);
        log.info("Successfully updated book with with ID : {}", bookId);
        return modelMapper.map(book, BookDto.class);
    }

    public void deleteBookById(Long bookId) {
        log.info("Deleting book with ID : {}", bookId);
        if (!bookRepository.existsById(bookId)) {
            log.error("Book not found with ID : {}", bookId);
            throw new ResourceNotFoundException("Book not found with ID : "+bookId);
        }
        bookRepository.deleteById(bookId);
        log.info("Successfully deleted book with ID : {}", bookId);
    }
}
