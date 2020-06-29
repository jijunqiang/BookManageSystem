package com.bysj.booksystem.service;

import com.bysj.booksystem.model.JsonFormatUtils;
import com.bysj.booksystem.model.NotRepeatBook;
import com.bysj.booksystem.model.User;
import com.bysj.booksystem.model.UserAndBookAndBorrow;
import jdk.internal.org.objectweb.asm.tree.IincInsnNode;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.List;

public interface ComprehensiveService {

    //查询所有用户(用户统计界面)
    public List<User> getUsersByCondition(String phone, String gender, String condition, Integer page, Integer limit, HttpSession session) throws ParseException;

    //无重复书籍查询
    public List<NotRepeatBook> getBooksByCondition (String bookIds,Integer page,Integer limit, HttpSession session);

    //借阅记录综合查询
    public List<UserAndBookAndBorrow> getRecordsByCondition(String phone,String card_number,String condition,Integer page,Integer limit, HttpSession session) throws ParseException;
}
