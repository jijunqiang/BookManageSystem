package com.bysj.booksystem.model;

import lombok.Data;

import java.util.Date;
/**
 * @Author:         jjq
 * @CreateDate:     2020/4/2 14:52
 * @Version:        1.0
 * @Description:    无重复查询数据封装
 */
@Data
public class NotRepeatBook {
    private String book_name;
    private String author;
    private String publisher;
    private Date publish_time;
    private String classification;
    private String introduce;

    /**
     * 这种书总共有多少本（如三国演义有3本）
     */
    private Integer totalCount;

    /**
     * 这种书总借出本
     */
    private Integer borrowCount;

    /**
     * 这种书还有多少本未借出
     */
    private Integer notBorrowCount;
}
