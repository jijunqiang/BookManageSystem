package com.bysj.booksystem.controller;

import com.bysj.booksystem.model.*;
import com.bysj.booksystem.service.BookService;
import com.bysj.booksystem.service.BorrowService;
import com.bysj.booksystem.service.CardService;
import com.bysj.booksystem.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("borrowController")
public class BorrowController {

    @Autowired
    private CardService cardService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowService borrowService;

    //借书
    @RequestMapping("borrowBook")
    @ResponseBody
    public String BorrowBook(String card_number,String book_number){
        LibraryCard cardsByNumber = cardService.getCardsByNumber(card_number);
        if(cardsByNumber != null){
            ArrayList<String> list = new ArrayList<>();
            String[] number = book_number.split(",");
            for (String n :number ) {
                list.add(n);
            }
            int i = bookService.modifyState(list);
            if(i>0){
                int k = borrowService.addBorrowRecords(cardsByNumber.getUser_id(), list);
                if(k<0){
                    return "插入借阅记录失败";
                }else {
                    return "借阅成功";
                }
            }else {
                return "更新书籍状态失败";
            }
        }else {
            return "请先办理图书证";
        }

    }

    //根据user_id查询借阅记录
    @RequestMapping("getRecordsByUserId")
    @ResponseBody
    public JsonFormatUtils<BorrowRecords> getRecordsByUserId(Integer user_id){
        List<BorrowRecords> recordsByUserId = borrowService.getRecordsByUserId(user_id,null);
        JsonFormatUtils<BorrowRecords> borrowRecordsJsonFormatUtils = new JsonFormatUtils<>();
        borrowRecordsJsonFormatUtils.setCode(0);
        borrowRecordsJsonFormatUtils.setMsg("");
        borrowRecordsJsonFormatUtils.setCount(recordsByUserId.size());
        borrowRecordsJsonFormatUtils.setData(recordsByUserId);
        return borrowRecordsJsonFormatUtils;
    }

    //根据user_id查询借阅记录判断是否逾期
    @RequestMapping("hasOverdue")
    @ResponseBody
    public String hasOverdue(Integer user_id) throws ParseException {
        List<BorrowRecords> recordsByUserId = borrowService.getRecordsByUserId(user_id,null);
        if(recordsByUserId != null){
            for (BorrowRecords records:recordsByUserId ) {
                //判断是否续借过
                if(records.getContinue_date() !=null){
                    //如果续借过，就从续借时间算，有效期30天
                    Date continue_date = records.getContinue_date();
                    Date endTime = DateUtil.getCalendar(continue_date, 30);
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                    String format = sdf.format(new Date());
                    Date nowTime = sdf.parse(format);
                    if (endTime.before(nowTime)) {
                        //已经过期
                        return "-1";
                    }

                }else {
                    //没有续借，就从借出时间算
                    Date out_date = records.getOut_date();
                    Date overTime = DateUtil.getCalendar(out_date, 30);
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                    String format = sdf.format(new Date());
                    Date nowTime = sdf.parse(format);
                    if(overTime.before(nowTime)){
                        //已经过期
                        return "-1";
                    }
                }
            }
            //没有过期的
            return "1";
    }else {
        return "0";
    }

}


    //续借===通过user_id和图书编号
    @RequestMapping("renew")
    @ResponseBody
    public String renew(Integer user_id,String book_number) throws ParseException {
        String[] split = book_number.split(",");
        String msg = borrowService.renew(user_id, split);
        return msg;
    }

    //归还===通过user_id和图书编号
    @RequestMapping("returnBook")
    @ResponseBody
    public String returnBook(Integer user_id,String book_number){
        String[] split = book_number.split(",");
        String s = borrowService.returnBook(user_id, split);
        return s;
    }

    //查询所有借阅记录
    @RequestMapping("getAllRecords")
    @ResponseBody
    public JsonFormatUtils<BorrowRecords> getAllRecords(HttpServletRequest request,HttpSession session){
        Integer page = Integer.parseInt(request.getParameter("page"));
        Integer limit = Integer.parseInt(request.getParameter("limit"));
        List<BorrowRecords> allRecords = borrowService.getAllRecords(page,limit,session);

        Integer recordsCount = borrowService.getRecordsCount(session);

        if(recordsCount ==null){
            recordsCount =0;
        }
        JsonFormatUtils<BorrowRecords> borrowRecordsJsonFormatUtils = new JsonFormatUtils<>();
        borrowRecordsJsonFormatUtils.setCode(0);
        borrowRecordsJsonFormatUtils.setMsg("");
        borrowRecordsJsonFormatUtils.setCount(recordsCount);
        borrowRecordsJsonFormatUtils.setData(allRecords);
        return borrowRecordsJsonFormatUtils;
    }

    //按条件查询借阅记录
    @RequestMapping("getRecordsByCondition")
    @ResponseBody
    public JsonFormatUtils<BorrowRecords> getRecordsByCondition(HttpServletRequest request,String phone,String condition,HttpSession session) throws ParseException {
        Integer page = Integer.parseInt(request.getParameter("page"));
        Integer limit = Integer.parseInt(request.getParameter("limit"));
        List<BorrowRecords> recordsByCondition = borrowService.getRecordsByCondition(phone, condition,page,limit,session);

        Integer recordsCount = borrowService.getRecordsCount(session);
        if(recordsCount == null){
            recordsCount =0;
        }
        JsonFormatUtils<BorrowRecords> borrowRecordsJsonFormatUtils = new JsonFormatUtils<>();
        borrowRecordsJsonFormatUtils.setCode(0);
        borrowRecordsJsonFormatUtils.setMsg("");
        borrowRecordsJsonFormatUtils.setCount(recordsCount);
        borrowRecordsJsonFormatUtils.setData(recordsByCondition);
        return borrowRecordsJsonFormatUtils;
    }

    //============读者端======读者的借阅记录========================
    @RequestMapping("getRecordsFromReader")
    @ResponseBody
    public JsonFormatUtils<UserAndBookAndBorrow> getRecordsFromReader(HttpSession session,String condition,HttpServletRequest request){
        Integer page = Integer.parseInt(request.getParameter("page"));
        Integer limit = Integer.parseInt(request.getParameter("limit"));

        List<UserAndBookAndBorrow> recordsFromReader = borrowService.getRecordsFromReader(session, condition,page,limit);
        List<BorrowRecords> recordsFromReader_count =(List<BorrowRecords>) session.getAttribute("recordsFromReader_count");
        JsonFormatUtils<UserAndBookAndBorrow> userAndBookAndBorrowJsonFormatUtils = new JsonFormatUtils<>();
        userAndBookAndBorrowJsonFormatUtils.setCode(0);
        userAndBookAndBorrowJsonFormatUtils.setMsg("");
        userAndBookAndBorrowJsonFormatUtils.setCount(recordsFromReader_count.size());
        userAndBookAndBorrowJsonFormatUtils.setData(recordsFromReader);
        return userAndBookAndBorrowJsonFormatUtils;

    }


}
