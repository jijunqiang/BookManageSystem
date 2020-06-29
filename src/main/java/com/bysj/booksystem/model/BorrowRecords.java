package com.bysj.booksystem.model;

import lombok.Data;

import java.util.Date;

@Data
public class BorrowRecords {

    private Integer record_id;
    private Integer user_id;
    private String book_number;
    private Date out_date;
    private Integer effective_time;
    private String return_state;
    private Date return_date;
    private Integer continue_num;
    private Date continue_date;


}
