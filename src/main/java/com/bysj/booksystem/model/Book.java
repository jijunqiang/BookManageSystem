package com.bysj.booksystem.model;

import lombok.Data;

import java.util.Date;

@Data
public class Book {
    private Integer book_id;
    private String book_number;
    private String book_name;
    private String author;
    private String publisher;
    private Date publish_time;
    private String classification;
    private Integer state;
    private String introduce;
}
