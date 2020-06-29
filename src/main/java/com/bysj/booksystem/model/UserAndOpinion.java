package com.bysj.booksystem.model;

import lombok.Data;

import java.util.Date;
/**
 * @Author:         jjq
 * @CreateDate:     2020/4/9 12:43
 * @Version:        1.0
 * @Description:    将用户和反馈意见封装到一起返回
 */

@Data
public class UserAndOpinion {

    private Integer opinion_id;
    private Integer user_id;
    private String user_name;
    private String phone;
    private String context;
    private Date feedback_time;
    private String handle;
}
