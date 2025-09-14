package com.smasher.library_management_system.services;

import com.smasher.library_management_system.dtos.AuthorDto;
import com.smasher.library_management_system.entities.Author;
import com.smasher.library_management_system.exceptions.ResourceNotFoundException;
import com.smasher.library_management_system.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    public AuthorDto createNewAuthor(AuthorDto authorDto) {
        log.info("Creating Author with email : {}", authorDto.getEmail());
        List<Author> authors = authorRepository.findByEmail(authorDto.getEmail()).orElse(null);
        if(!authors.isEmpty()) {
            log.error("Author already exists with Email : {}", authorDto.getEmail());
            throw  new RuntimeException("Author already exists with Email : "+authorDto.getEmail());
        }
        Author author = modelMapper.map(authorDto, Author.class);
        Author savedAuthor = authorRepository.saveAndFlush(author);
        log.info("Successfully saved Author with email : {}", savedAuthor.getEmail());
        return modelMapper.map(savedAuthor, AuthorDto.class);
    }

    public List<AuthorDto> getAllAuthors() {
        log.info("Fetching all Authors");
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            log.error("Authors are not found");
            throw new ResourceNotFoundException("Authors are not found");
        }
        log.info("Successfully fetched Authors");
        return authors.stream()
                .map(author -> modelMapper.map(author, AuthorDto.class))
                .toList();
    }

    public AuthorDto getAuthorById(Long authorId) {
        log.info("Fetching author with ID : {}", authorId);
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> {
                    log.error("Author not found with Id : {}", authorId);
                    return new ResourceNotFoundException("Author not found with ID : "+authorId);
                });
        log.info("Successfully fetched author with ID : {}", authorId);
        return modelMapper.map(author, AuthorDto.class);
    }

    public AuthorDto updateAuthorById(Long authorId, AuthorDto authorDto) {
        log.info("Updating Author with ID : {}", authorId);
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> {
                    log.error("Author not found with ID : {}", authorId);
                    return new ResourceNotFoundException("Author not found with ID : "+authorId);
                });
        if(!author.getEmail().equals(authorDto.getEmail())) {
            log.error("Email of Author can not be updated : {}", authorDto.getEmail());
            throw new RuntimeException("Email of Author can not be updated : "+authorDto.getEmail());
        }
        modelMapper.map(authorDto, author);
        author.setId(authorId);
        author = authorRepository.save(author);
        log.info("Successfully updated author with ID : {}", authorId);
        return modelMapper.map(author, AuthorDto.class);
    }

    public void deleteAuthorById(Long authorId) {
        log.info("Deleting Author with ID : {}", authorId);
        if (!authorRepository.existsById(authorId)) {
            log.error("Author not found with ID : {}", authorId);
            throw new ResourceNotFoundException("Author not found with ID : "+authorId);
        }
        authorRepository.deleteById(authorId);
        log.info("Successfully deleted author with id : {}", authorId);
    }
}
