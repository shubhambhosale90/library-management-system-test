package com.smasher.library_management_system.controllers;

import com.smasher.library_management_system.dtos.AuthorDto;
import com.smasher.library_management_system.services.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/author")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<AuthorDto> createNewAuthor(@RequestBody AuthorDto authorDto) {
        AuthorDto author = authorService.createNewAuthor(authorDto);
        return ResponseEntity.ok(author);
    }

    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        List<AuthorDto> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @GetMapping(path = "/{authorId}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable Long authorId) {
        AuthorDto author = authorService.getAuthorById(authorId);
        return ResponseEntity.ok(author);
    }

    @PutMapping(path = "/{authorId}")
    public ResponseEntity<AuthorDto> updateAuthorById(@PathVariable Long authorId, @RequestBody AuthorDto authorDto) {
        AuthorDto author = authorService.updateAuthorById(authorId, authorDto);
        return ResponseEntity.ok(author);
    }

    @DeleteMapping(path = "/{authorId}")
    public ResponseEntity<Void> deleteAuthorById(@PathVariable Long authorId) {
        authorService.deleteAuthorById(authorId);
        return ResponseEntity.noContent().build();
    }
}
