package com.bysj.booksystem.model;

import lombok.Data;

import java.util.Date;
/**
 * @Author:         jjq
 * @CreateDate:     2020/4/3 15:21
 * @Version:        1.0
 * @Description:    借阅记录综合查询返回封装的数据格式
 */
@Data
public class UserAndBookAndBorrow {
    private Integer record_id;
    private Integer user_id;
    private String user_name;
    private String phone;
    private String book_number;
    private String book_name;
    private Date out_date;
    private Integer effective_time;
    private String return_state;
    private Date return_date;
    private Integer continue_num;
    private Date continue_date;
}
