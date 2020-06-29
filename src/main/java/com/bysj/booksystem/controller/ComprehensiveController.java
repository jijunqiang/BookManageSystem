package com.bysj.booksystem.controller;

import com.bysj.booksystem.model.*;
import com.bysj.booksystem.service.BookService;
import com.bysj.booksystem.service.ComprehensiveService;
import com.bysj.booksystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("comprehensiveController")
public class ComprehensiveController {

    @Autowired
    private ComprehensiveService comprehensiveService;

    @Autowired
    private BookService bookService;

    //===============读者综合查询================查询所有用户信息（用户统计界面）
    @RequestMapping("getUsersByCondition")
    @ResponseBody
    public JsonFormatUtils<User> getUsersByCondition(String phone, String gender, String condition, HttpServletRequest request, HttpSession session) throws ParseException {
        Integer page = Integer.parseInt(request.getParameter("page"));
        Integer limit = Integer.parseInt(request.getParameter("limit"));
        List<User> usersByCondition = comprehensiveService.getUsersByCondition(phone, gender, condition,page,limit,session);
        List<User> users_List =(List<User>) session.getAttribute("users_List");

        JsonFormatUtils<User> userJsonFormatUtils = new JsonFormatUtils<>();
        userJsonFormatUtils.setCode(0);
        userJsonFormatUtils.setMsg("");
        userJsonFormatUtils.setCount(users_List.size());
        userJsonFormatUtils.setData(usersByCondition);
        return userJsonFormatUtils;
    }

    //=================书籍综合查询===========
    @RequestMapping("getBooksByCondition")
    @ResponseBody
    public JsonFormatUtils<NotRepeatBook> getBooksByCondition(String bookIds,HttpServletRequest request, HttpSession session){
        Integer page = Integer.parseInt(request.getParameter("page"));
        Integer limit = Integer.parseInt(request.getParameter("limit"));

        String book_ids ="";
        if(bookIds == null || bookIds == ""){
            List<Book> allBook = bookService.getAllBook(null, null,session);
            for (int i=0;i<allBook.size();i++){
                if(i == allBook.size()-1){
                    book_ids += allBook.get(i).getBook_id();
                }else {
                    book_ids += allBook.get(i).getBook_id()+",";
                }
            }
        }else {
            book_ids = bookIds;
        }

        List<NotRepeatBook> booksByCondition = comprehensiveService.getBooksByCondition(book_ids,page,limit,session);
        Integer not_repeat_count = (Integer)session.getAttribute("not_repeat_Count");
        if(not_repeat_count ==null){
            not_repeat_count =0;
        }
        JsonFormatUtils<NotRepeatBook> notRepeatBookJsonFormatUtils = new JsonFormatUtils<>();
        notRepeatBookJsonFormatUtils.setCode(0);
        notRepeatBookJsonFormatUtils.setMsg("");
        notRepeatBookJsonFormatUtils.setCount(not_repeat_count);
        notRepeatBookJsonFormatUtils.setData(booksByCondition);
        return notRepeatBookJsonFormatUtils;
    }

    //=================借阅记录综合查询===========
    @RequestMapping("getRecordsByCondition")
    @ResponseBody
    public JsonFormatUtils<UserAndBookAndBorrow> getRecordsByCondition(String phone,String card_number,String condition,HttpServletRequest request, HttpSession session) throws ParseException {
        Integer page = Integer.parseInt(request.getParameter("page"));
        Integer limit = Integer.parseInt(request.getParameter("limit"));

        List<UserAndBookAndBorrow> recordsByCondition = comprehensiveService.getRecordsByCondition(phone, card_number, condition,page,limit,session);
        List<BorrowRecords> borrowRecords_count =(List<BorrowRecords>) session.getAttribute("borrowRecords_count");
        JsonFormatUtils<UserAndBookAndBorrow> userAndBookAndBorrowJsonFormatUtils = new JsonFormatUtils<>();
        userAndBookAndBorrowJsonFormatUtils.setCode(0);
        userAndBookAndBorrowJsonFormatUtils.setMsg("");
        userAndBookAndBorrowJsonFormatUtils.setCount(borrowRecords_count.size());
        userAndBookAndBorrowJsonFormatUtils.setData(recordsByCondition);
        return userAndBookAndBorrowJsonFormatUtils;
    }

    //====================导出读者数据=============================
    @RequestMapping("exportReaderData")
    @ResponseBody
    public List<User> exportReaderData(HttpSession session){
        List<User> users_list =(List<User>) session.getAttribute("users_List");
        return users_list;
    }

    //====================导出书籍数据=============================
    @RequestMapping("exportBookData")
    @ResponseBody
    public List<Object> exportBookData(HttpSession session){
        List<Object> book_list =(List<Object>) session.getAttribute("exportBook");
        return book_list;
    }

    //====================导出借阅记录数据=============================
    @RequestMapping("exportRecordsData")
    @ResponseBody
    public List<UserAndBookAndBorrow> exportRecordsData(HttpSession session){
        List<UserAndBookAndBorrow> borrowRecords_count = (List<UserAndBookAndBorrow>) session.getAttribute("borrowRecords_count");
        return borrowRecords_count;
    }
}
