package com.bysj.booksystem.model;

import lombok.Data;

@Data
public class RememberMe {

    /**
     * 主键ID
     */
    private Integer remember_id;
    /**
     * 用户id
     */
    private Integer user_id;
    /**
     * 登录令牌（loginName+user_agent）
     */
    private String token;
    /**
     * 创建时间（从1970年开始到当前时刻地秒数）
     */
    private long create_time;
    /**
     * 存活时间
     */
    private long expires;


}
