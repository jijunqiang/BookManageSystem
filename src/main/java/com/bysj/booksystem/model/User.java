package com.bysj.booksystem.model;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer user_id;
    private String user_name;
    private String password;
    private String phone;
    private String gender;
    private String email;
    private Date register_time;
    private String role;

}
