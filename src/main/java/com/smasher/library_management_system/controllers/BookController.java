package com.smasher.library_management_system.controllers;

import com.smasher.library_management_system.dtos.BookDto;
import com.smasher.library_management_system.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/book")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookDto> createNewBook(@RequestBody BookDto bookDto) {
        BookDto book = bookService.createNewBook(bookDto);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping(path = "/{bookId}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long bookId) {
        BookDto book = bookService.getBookById(bookId);
        return ResponseEntity.ok(book);
    }

    @PutMapping(path = "/{bookId}")
    public ResponseEntity<BookDto> updateBookById(@PathVariable Long bookId, @RequestBody BookDto bookDto) {
        BookDto book = bookService.updateBookById(bookId, bookDto);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping(path = "/{bookId}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long bookId) {
        bookService.deleteBookById(bookId);
        return ResponseEntity.noContent().build();
    }


}
