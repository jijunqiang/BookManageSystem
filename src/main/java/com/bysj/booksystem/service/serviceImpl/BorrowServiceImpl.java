package com.bysj.booksystem.service.serviceImpl;

import com.bysj.booksystem.dao.BookMapper;
import com.bysj.booksystem.dao.BorrowMapper;
import com.bysj.booksystem.dao.UserMapper;
import com.bysj.booksystem.model.*;
import com.bysj.booksystem.service.BorrowService;
import com.bysj.booksystem.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BorrowServiceImpl implements BorrowService {

    @Autowired(required = false)
    private BorrowMapper borrowMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private BookMapper bookMapper;

    @Override
    public int addBorrowRecords(Integer user_id, List<String> list) {
        List<BorrowRecords> borrowRecords_list = new ArrayList<>();
        Date date = new Date();
        java.sql.Date out_date = new java.sql.Date(date.getTime());
        for (String book_number:list){
            BorrowRecords borrowRecords = new BorrowRecords();
            borrowRecords.setUser_id(user_id);
            borrowRecords.setBook_number(book_number);
            borrowRecords.setOut_date(out_date);
            borrowRecords.setEffective_time(30);
            borrowRecords.setReturn_state("未归还");
            borrowRecords.setContinue_num(2);
            borrowRecords_list.add(borrowRecords);
        }
        int i = borrowMapper.insertBorrowRecords(borrowRecords_list);
        return i;
    }

    @Override
    public List<BorrowRecords> getRecordsByUserId(Integer user_id,String[] bookNumbers) {
        List<BorrowRecords> recordsByUserId = borrowMapper.getRecordsByUserId(user_id,bookNumbers);
        return recordsByUserId;
    }

    @Override
    public String renew(Integer user_id, String[] bookNumbers) throws ParseException {
        //根据user_id和图书编号查询，判断是否能续借
        List<BorrowRecords> recordsByUserId = borrowMapper.getRecordsByUserId(user_id,bookNumbers);
        if(recordsByUserId != null){
                for (BorrowRecords br: recordsByUserId) {
                    if(br.getContinue_num()<=0){
                        return br.getBook_number()+"已经续借2次，不能续借";
                    }
                    if(br.getContinue_date() !=null){
                        //如果续借过，就从续借时间算，有效期30天
                        Date continue_date = br.getContinue_date();
                        Date endTime = DateUtil.getCalendar(continue_date, 30);
                        Date nowDate = DateUtil.getNowDate();
                        if (endTime.before(nowDate)) {
                            //已经过期
                            return br.getBook_number()+"已经逾期，不能续借，请先归还";
                        }

                    }else {
                        //没有续借，就从借出时间算
                        Date out_date = br.getOut_date();
                        Date overTime = DateUtil.getCalendar(out_date, 30);
                        Date nowDate = DateUtil.getNowDate();
                        if(overTime.before(nowDate)){
                            //已经过期
                            return br.getBook_number()+"已经逾期，不能续借，请先归还";
                        }
                    }
                }
            Date date = new Date();
            java.sql.Date renewDate = new java.sql.Date(date.getTime());
            int renew = borrowMapper.renew(user_id, bookNumbers, renewDate);
            if(renew > 0){
                return "续借成功";
            }else {
                return "续借失败";
            }

        }else {
            return "找不到记录";
        }
    }

    @Override
    @Transactional
    public String returnBook(Integer user_id, String[] bookNumbers) {
        Date date = new Date();
        java.sql.Date return_date = new java.sql.Date(date.getTime());

        int i = borrowMapper.returnBook(user_id, bookNumbers, return_date);
        if(i>0){
            Integer integer = bookMapper.updateBookState(bookNumbers);
            if(integer > 0){
                return "还书成功";
            }else {
                return "还书失败";
            }
        }else {
            return "还书失败";
        }

    }

    @Override
    public List<BorrowRecords> getAllRecords(Integer page,Integer limit,HttpSession session) {
        Integer index = 0;
        if(page > 1){
            index = (page - 1) * limit;
        }
        List<BorrowRecords> borrowRecords = borrowMapper.selectAllRecords(null,null);
        session.setAttribute("borrowRecords",borrowRecords);
        List<BorrowRecords> borrowRecordsList = borrowMapper.selectAllRecords(index,limit);
        return borrowRecordsList;
    }

    //按条件查询借阅记录
    @Override
    public List<BorrowRecords> getRecordsByCondition(String phone, String condition,Integer page,Integer limit,HttpSession session) throws ParseException {
        Integer index = 0;
        if(page > 1){
            index = (page - 1) * limit;
        }
        if(phone != null && phone != ""){
            //phone不为空
            ArrayList<Integer> list = new ArrayList<>();
            List<User> users = userMapper.queryUser(phone);
            if(users.size()==0){
                return null;
            }
            for (User user : users ) {
                list.add(user.getUser_id());
            }
            List<BorrowRecords> records = getRecords(list, condition,index,limit,session);
                return records;
        }else {
            List<BorrowRecords> records = getRecords(null, condition,index,limit,session);
            return records;
        }
    }

    @Override
    public List<UserAndBookAndBorrow> getRecordsFromReader(HttpSession session, String condition,Integer page,Integer limit) {
        Integer index = 0;
        if(page > 1){
            index = (page - 1) * limit;
        }
        User loginUser = (User)session.getAttribute("loginUser");
        List<BorrowRecords> recordsFromReader_1 = borrowMapper.getRecordsFromReader(loginUser.getUser_id(), condition,null,null);
        session.setAttribute("recordsFromReader_count",recordsFromReader_1);
        List<BorrowRecords> recordsFromReader_2 = borrowMapper.getRecordsFromReader(loginUser.getUser_id(), condition,index,limit);
            if(recordsFromReader_2 == null || recordsFromReader_2.size()<=0){
                return null;
            }
            ArrayList<String> list = new ArrayList<>();
            ArrayList<UserAndBookAndBorrow> userAndBookAndBorrows = new ArrayList<>();
            for (BorrowRecords borrowRecord :recordsFromReader_2 ) {
                list.add(borrowRecord.getBook_number());
                UserAndBookAndBorrow userAndBookAndBorrow = new UserAndBookAndBorrow();
                userAndBookAndBorrow.setUser_id(borrowRecord.getUser_id());
                userAndBookAndBorrow.setBook_number(borrowRecord.getBook_number());
                userAndBookAndBorrow.setContinue_date(borrowRecord.getContinue_date());
                userAndBookAndBorrow.setRecord_id(borrowRecord.getRecord_id());
                userAndBookAndBorrow.setEffective_time(borrowRecord.getEffective_time());
                userAndBookAndBorrow.setOut_date(borrowRecord.getOut_date());
                userAndBookAndBorrow.setReturn_date(borrowRecord.getReturn_date());
                userAndBookAndBorrow.setReturn_state(borrowRecord.getReturn_state());
                userAndBookAndBorrow.setContinue_num(borrowRecord.getContinue_num());
                userAndBookAndBorrows.add(userAndBookAndBorrow);
            }
            List<Book> books = bookMapper.selectBooksByNumberList(list);
            for (Book book :books ) {
                for (UserAndBookAndBorrow uabab :userAndBookAndBorrows ) {
                    if(book.getBook_number().equals(uabab.getBook_number())){
                       uabab.setBook_name(book.getBook_name());
                    }
                }
            }
            return userAndBookAndBorrows;
    }

    @Override
    public Integer getRecordsCount(HttpSession session) {
       List<BorrowRecords>  borrowRecords = (List<BorrowRecords>)session.getAttribute("borrowRecords");
        ArrayList<Integer> list = new ArrayList<>();
        for (BorrowRecords borrowRecord : borrowRecords) {
            list.add(borrowRecord.getRecord_id());
        }
        if(list.size()>0){
            Integer recordsCount = borrowMapper.getRecordsCount(list);
            return recordsCount;
        }
        return null;
    }




    //===================按条件查询借阅记录======方法============================
    public List<BorrowRecords> getRecords(List<Integer> user_id,String condition,Integer index,Integer limit,HttpSession session) throws ParseException {
        if (condition.equals("1")) {
            //phone不为空且condition为已归还
            List<BorrowRecords> borrowRecords = borrowMapper.selectRecordsByUserIdList(user_id, "1",null,null);
            session.setAttribute("borrowRecords",borrowRecords);
            List<BorrowRecords> borrowRecordsList = borrowMapper.selectRecordsByUserIdList(user_id, "1",index,limit);
            return borrowRecordsList;
        } else if (condition.equals("0")) {
            //phone不为空且condition为未归还
            List<BorrowRecords> borrowRecords = borrowMapper.selectRecordsByUserIdList(user_id, "0",null,null);
            session.setAttribute("borrowRecords",borrowRecords);
            List<BorrowRecords> borrowRecordsList = borrowMapper.selectRecordsByUserIdList(user_id, "0",index,limit);
            return borrowRecordsList;
        } else if (condition.equals("-1")) {
            //phone不为空且condition为逾期未归还
            List<BorrowRecords> borrowRecords = borrowMapper.selectRecordsByUserIdList(user_id, "0",null,null);
            List<Integer> Records_ids_List = new ArrayList<>();
            for (BorrowRecords borrowRecord : borrowRecords) {
                if (borrowRecord.getContinue_date() != null) {
                    Date continue_date = borrowRecord.getContinue_date();
                    Date endTime = DateUtil.getCalendar(continue_date, 30);
                    Date nowDate = DateUtil.getNowDate();
                    if (endTime.before(nowDate)) {
                        //已经过期
                        Records_ids_List.add(borrowRecord.getRecord_id());
                    }
                } else {
                    Date out_date = borrowRecord.getOut_date();
                    Date overTime = DateUtil.getCalendar(out_date, 30);
                    Date nowDate = DateUtil.getNowDate();
                    if (overTime.before(nowDate)) {
                        //已经过期
                        Records_ids_List.add(borrowRecord.getRecord_id());
                    }
                }
            }
            if(Records_ids_List.size()==0){
                return null;
            }
            List<BorrowRecords> borrowRecordsList_1 = borrowMapper.selectRecordsByRecordIdList(Records_ids_List,null,null);
            session.setAttribute("borrowRecords",borrowRecordsList_1);
            List<BorrowRecords> borrowRecordsList_2 = borrowMapper.selectRecordsByRecordIdList(Records_ids_List,index,limit);

            return borrowRecordsList_2;
        }else {
            List<BorrowRecords> borrowRecords = borrowMapper.selectRecordsByUserIdList(user_id,condition,null,null);
            session.setAttribute("borrowRecords",borrowRecords);
            List<BorrowRecords> borrowRecordsList = borrowMapper.selectRecordsByUserIdList(user_id,condition,index,limit);
            return borrowRecordsList;
        }

    }



}
