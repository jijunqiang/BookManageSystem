package com.bysj.booksystem.dao;

import com.bysj.booksystem.model.RememberMe;
import org.apache.ibatis.annotations.Param;

public interface RememberMapper {

    //记住密码
    public void insetRemember(@Param("rem") RememberMe rememberMe);

    //删除重复的记住密码记录
    public void deleteRememberMe(int user_id);

    //查询一条记录返回
    public RememberMe selectRememberByToken(String token);
}
