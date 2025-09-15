package com.smasher.library_management_system.repositories;

import com.smasher.library_management_system.TestContainerConfiguration;
import com.smasher.library_management_system.entities.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import(TestContainerConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    private Author author;

    @BeforeEach
    void setUp() {
         author = Author.builder()
                .name("Shubham")
                .email("shubham@gmail.com")
                .build();
    }

    @Test
    void testFindByEmail_whenEmailIsPresent_thenReturnAuthorList() {
//        Arrange,  Given
        authorRepository.save(author);

//        Act,  When
        List<Author> authors = authorRepository.findByEmail(author.getEmail()).orElse(null);

//        Assert,  Then
        assertThat(authors).isNotNull();
        assertThat(authors).isNotEmpty();
        assertThat(authors.get(0).getEmail()).isEqualTo(author.getEmail());
    }

    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmptyAuthorList() {
//        Arrange
        String email = "notPresent.123@gmail.com";

//        Act
        List<Author> authors = authorRepository.findByEmail(email).orElse(null);

//        Assert
        assertThat(authors).isNotNull();
        assertThat(authors).isEmpty();

    }
}