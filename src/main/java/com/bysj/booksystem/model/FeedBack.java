package com.bysj.booksystem.model;

import lombok.Data;

import java.util.Date;

/**
 * @Author:         jjq
 * @CreateDate:     2020/4/8 17:27
 * @Version:        1.0
 * @Description:    读者意见反馈
 */
@Data
public class FeedBack {
    private Integer opinion_id;
    private Integer user_id;
    private String context;
    private Date feedback_time;
    private String handle;
}
