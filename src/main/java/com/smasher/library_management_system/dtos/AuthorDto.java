package com.smasher.library_management_system.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smasher.library_management_system.entities.Book;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDto {

    private Long id;
    private String name;
    private String email;
    private List<BookDto> book;
}
