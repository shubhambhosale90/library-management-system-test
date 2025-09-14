package com.smasher.library_management_system.dtos;

import com.smasher.library_management_system.entities.Book;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
public class AuthorDto {

    private Long id;
    private String name;
    private String email;
    private List<Book> book;
}
