package com.smasher.library_management_system.dtos;

import com.smasher.library_management_system.entities.Author;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookDto {

    private Long id;
    private String title;
    private LocalDate publishDate;
    private String publication;
    private BigDecimal price;
    private AuthorDto author;
}
