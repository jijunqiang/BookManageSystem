package com.bysj.booksystem.model;

import lombok.Data;

@Data
public class LibraryCard {
    private String card_number;
    private Integer user_id;
    private Integer effective;
}
