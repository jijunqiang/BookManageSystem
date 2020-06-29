package com.bysj.booksystem.service;

import com.bysj.booksystem.model.BorrowRecords;
import com.bysj.booksystem.model.JsonFormatUtils;
import com.bysj.booksystem.model.UserAndBookAndBorrow;
import com.bysj.booksystem.model.antv_bookData;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.List;

public interface BorrowService {

    //插入借阅记录
    public int addBorrowRecords(Integer user_id,List<String> list);

    //根据user_id查询记录
    public List<BorrowRecords> getRecordsByUserId(Integer user_id,String[] bookNumbers);

    //续借===通过user_id和图书编号
    public String renew(Integer user_id,String[] bookNumbers) throws ParseException;

    //归还===通过user_id和图书编号
    public String returnBook(Integer user_id,String[] bookNumbers);

    //查询所有借阅记录
    public List<BorrowRecords> getAllRecords(Integer page,Integer limit,HttpSession session);

    //按条件查询借阅记录
    public List<BorrowRecords> getRecordsByCondition(String phone, String condition,Integer page,Integer limit,HttpSession session) throws ParseException;

    //============读者端======读者的借阅记录========================
    public List<UserAndBookAndBorrow> getRecordsFromReader(HttpSession session, String condition,Integer page,Integer limit);

    //获取借阅记录的数量
    public Integer getRecordsCount(HttpSession session);


}
