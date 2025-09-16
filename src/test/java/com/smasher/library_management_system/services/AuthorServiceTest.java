package com.smasher.library_management_system.services;

import com.smasher.library_management_system.dtos.AuthorDto;
import com.smasher.library_management_system.entities.Author;
import com.smasher.library_management_system.exceptions.ResourceNotFoundException;
import com.smasher.library_management_system.repositories.AuthorRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @Spy
    private ModelMapper modelMapper;

    private Author mockAuthor;
    private AuthorDto mockAuthorDto;

    @BeforeEach
    void setUp() {
        mockAuthor = Author.builder()
                .id(1L)
                .name("Shubham")
                .email("shubham@gmail.com")
                .build();

        mockAuthorDto = modelMapper.map(mockAuthor, AuthorDto.class);
    }

    @Test
    void testCreateNewAuthor_whenAuthorIsValid_thenReturnAuthor() {
//        Arrange
        when(authorRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(authorRepository.save(any(Author.class))).thenReturn(mockAuthor);

//        Act
        AuthorDto authorDto = authorService.createNewAuthor(mockAuthorDto);

//        Assert
        assertThat(authorDto).isNotNull();
        assertThat(authorDto.getEmail()).isEqualTo(mockAuthor.getEmail());

        ArgumentCaptor<Author> authorArgumentCaptor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepository).save(authorArgumentCaptor.capture());
        Author capturedAuthor = authorArgumentCaptor.getValue();
        assertThat(authorDto.getEmail()).isEqualTo(capturedAuthor.getEmail());
    }

    @Test
    void testCreateNewAuthor_whenAuthorEmailIsAlreadyPresent_thenThrowException() {
//        Arrange
        when(authorRepository.findByEmail(anyString())).thenReturn(Optional.of(List.of(mockAuthor)));

//        Act and Assert
        Assertions.assertThatThrownBy(() -> authorService.createNewAuthor(mockAuthorDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Author already exists with Email : "+mockAuthorDto.getEmail());

    }

    @Test
    void testGetAllAuthors_whenAuthorsArePresent_thenReturnAuthorList() {
//        Assign
        when(authorRepository.findAll()).thenReturn(List.of(mockAuthor));

//        Act
        List<AuthorDto> authors = authorService.getAllAuthors();

//        Assert
        assertThat(authors).isNotEmpty()
                .isNotNull();
    }

    @Test
    void testGetAllAuthors_whenAuthorsAreNotPresent_thenThrowException() {
//        Assign
        when(authorRepository.findAll()).thenReturn(List.of());

//        Act and Assert
        assertThatThrownBy(() -> authorService.getAllAuthors()).
                isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Authors are not found");
    }

    @Test
    void testGetAuthorById_whenAuthorIdIsPresent_thenReturnAuthor() {
//        Assign
        when(authorRepository.findById(mockAuthor.getId())).thenReturn(Optional.of(mockAuthor));

//        Act
        AuthorDto authorDto = authorService.getAuthorById(mockAuthor.getId());

//        Assert
        assertThat(authorDto).isNotNull();
        assertThat(authorDto.getId()).isEqualTo(mockAuthor.getId());
        assertThat(authorDto.getEmail()).isEqualTo(mockAuthor.getEmail());
        verify(authorRepository, only()).findById(mockAuthor.getId());

    }

    @Test
    void testGetAuthorById_whenAuthorIdIsNotPresent_thenThrowException() {
//        Assign
        Long id = 2L;
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

//        Act and Assert
        assertThatThrownBy(() -> authorService.getAuthorById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found with ID : "+id);
        verify(authorRepository).findById(anyLong());
    }

    @Test
    void testUpdateAuthorById_whenAuthorIsValid_thenReturnAuthorDto() {
//        Assign
        Long id = 1L;
        AuthorDto updatableAuthorDto = modelMapper.map(mockAuthor, AuthorDto.class);
        updatableAuthorDto.setName("Demo");
        when(authorRepository.findById(id)).thenReturn(Optional.of(mockAuthor));
        mockAuthor.setName("Demo");
        when(authorRepository.save(any(Author.class))).thenReturn(mockAuthor);

//        Act
        AuthorDto authorDto = authorService.updateAuthorById(id, updatableAuthorDto);

//        Assert
        assertThat(authorDto).isNotNull();
        assertThat(authorDto.getEmail()).isEqualTo(mockAuthor.getEmail());
        assertThat(authorDto.getName()).isNotEqualTo(mockAuthorDto.getName());

    }

    @Test
    void testUpdateAuthorById_whenAuthorIdIsNotPresent_thenThrowException() {
//        Assign
        Long id = 2L;
        when(authorRepository.findById(2L)).thenReturn(Optional.empty());

//        Act and Assert
        assertThatThrownBy(() -> authorService.updateAuthorById(id, any(AuthorDto.class)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found with ID : "+id);

    }

    @Test
    void testUpdateAuthorById_whenAuthorEmailIsAlreadyPresent_thenThrowException() {
//        Assign
        Long id = mockAuthorDto.getId();
        AuthorDto updatableAuthor = modelMapper.map(mockAuthor, AuthorDto.class);
        updatableAuthor.setEmail("demo@gmail.com");
        when(authorRepository.findById(id)).thenReturn(Optional.of(mockAuthor));

//        Act and Assert
        assertThatThrownBy(() -> authorService.updateAuthorById(id, updatableAuthor))
                .hasMessage("Email of Author can not be updated : "+updatableAuthor.getEmail())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testDeleteAuthorById_whenAuthorIdIsPresent_thenDeleteAuthor() {
//        Assign
        Long id = 2L;
        when(authorRepository.existsById(id)).thenReturn(true);
        doNothing().when(authorRepository).deleteById(id);

//        Act
        authorService.deleteAuthorById(id);

//        Assert
        verify(authorRepository, atMostOnce()).deleteById(id);
    }

    @Test
    void testDeleteAuthorById_whenAuthorIdIsNotPresent_thenThrowException() {
//        Assign
        Long id = 2L;
        when(authorRepository.existsById(id)).thenReturn(false);

//        Act and Assert
        assertThatThrownBy(() -> authorService.deleteAuthorById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found with ID : "+id);
    }
}