package com.bysj.booksystem.service.serviceImpl;

import com.bysj.booksystem.dao.BookMapper;
import com.bysj.booksystem.dao.BorrowMapper;
import com.bysj.booksystem.dao.CardMapper;
import com.bysj.booksystem.dao.UserMapper;
import com.bysj.booksystem.model.*;
import com.bysj.booksystem.service.ComprehensiveService;
import com.bysj.booksystem.utils.DateUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class ComprehensiveServiceImpl implements ComprehensiveService {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private BorrowMapper borrowMapper;

    @Autowired(required = false)
    private BookMapper bookMapper;

    @Autowired(required = false)
    private CardMapper cardMapper;

    /**
     * @author      jjq
     * params
     * @return
     * @date        2020/4/2 17:31
     * 方法实现说明   读者综合查询
     */
    @Override
    public List<User> getUsersByCondition(String phone, String gender, String condition,Integer page,Integer limit, HttpSession session) throws ParseException {
        Integer index = 0;
        if(page > 1){
            index = (page - 1) * limit;
        }
        ArrayList<Integer> list = new ArrayList<>();
        if(condition.equals("good_reader")) {
            if(phone!=null && phone != ""){
                List<User> users = userMapper.queryUser(phone);
                for (User user :users ) {
                    list.add(user.getUser_id());
                }
                List<BorrowRecords> borrowRecordsList = borrowMapper.queryRecordsByUserIds(list);
                List<User> userByCondition = getUserByCondition(borrowRecordsList, gender,condition,index,limit,session);
                return userByCondition;
            }else {
                List<BorrowRecords> borrowRecordsList = borrowMapper.selectAllRecords(null,null);
                List<User> userByCondition = getUserByCondition(borrowRecordsList, gender, condition,index,limit,session);
                return userByCondition;
            }

        }else {
            if(phone!=null && phone != ""){
                List<User> users = userMapper.queryUser(phone);
                for (User user :users ) {
                    list.add(user.getUser_id());
                }
                List<User> users_List = userMapper.selectUsersByCondition(list, gender, condition,null,null);
                session.setAttribute("users_List",users_List);
                List<User> usersList = userMapper.selectUsersByCondition(list, gender, condition,index,limit);
                return usersList;
            }else {
                List<User> users_List = userMapper.selectUsersByCondition(null, gender, condition,null,null);
                session.setAttribute("users_List",users_List);
                List<User> userList = userMapper.selectUsersByCondition(null, gender, condition,index,limit);
                return userList;
            }

        }
    }

    /**
     * @author      jjq
     * params
     * @return
     * @date        2020/4/2 17:32
     * 方法实现说明   书籍综合查询
     */
    @Override
    public List<NotRepeatBook> getBooksByCondition(String bookIds, Integer page, Integer limit, HttpSession session) {
        Integer index = 0;
        if(page > 1){
            index = (page - 1) * limit;
        }
        String[] split = bookIds.split(",");
        ArrayList<Integer> list = new ArrayList<>();
        if(split.length == 0 || bookIds == null){
            return null;
        }
        for (String bookId:split ) {
            int book_id = Integer.parseInt(bookId);
            list.add(book_id);
        }
        List<Book> books = bookMapper.selectBooksByIds(list);

        //用linkedHashset去重
        LinkedHashSet<String> strings = new LinkedHashSet<>();
       for (Book bk :books ) {
           strings.add(bk.getBook_name());
       }

       //满足条件的无重复名字书籍的数量
        session.setAttribute("not_repeat_Count",strings.size());

       //获取要导出的数据
        List<Book> booksByDistinct_1 = bookMapper.getBooksByDistinct(list,null,null);
        List<NotRepeatBook> notRepeatBooks1 = notRepeatFunction(booksByDistinct_1, books);
        session.setAttribute("exportBook",notRepeatBooks1);
        //查询无重复书名的数据，用于分页
        List<Book> booksByDistinct = bookMapper.getBooksByDistinct(list,index,limit);
        List<NotRepeatBook> notRepeatBooks = notRepeatFunction(booksByDistinct, books);
//        ArrayList<NotRepeatBook> notRepeatBooks = new ArrayList<>();
//       //先将不同书名的数据创建好，方便后面加数据
//       for (Book b:booksByDistinct) {
//           NotRepeatBook book = new NotRepeatBook();
//           book.setBook_name(b.getBook_name());
//           notRepeatBooks.add(book);
//       }
//
//        for (NotRepeatBook notRepeatBook :notRepeatBooks ) {
//            for (Book b :books ) {
//               if(b.getBook_name().equals(notRepeatBook.getBook_name())) {
//                   if(notRepeatBook.getTotalCount()==null){
//                       notRepeatBook.setTotalCount(1);
//                       notRepeatBook.setBook_name(b.getBook_name());
//                       notRepeatBook.setAuthor(b.getAuthor());
//                       notRepeatBook.setPublisher(b.getPublisher());
//                       notRepeatBook.setPublish_time(b.getPublish_time());
//                       notRepeatBook.setClassification(b.getClassification());
//                       notRepeatBook.setIntroduce(b.getIntroduce());
//                   }else {
//                       int i = notRepeatBook.getTotalCount() + 1;
//                       notRepeatBook.setTotalCount(i);
//                   }
//                   if(b.getState() == 1){
//                       //未借出
//                       if(notRepeatBook.getNotBorrowCount()==null){
//                           notRepeatBook.setNotBorrowCount(1);
//                       }else {
//                           notRepeatBook.setNotBorrowCount(notRepeatBook.getNotBorrowCount()+1);
//                       }
//                   }else {
//                       if(notRepeatBook.getBorrowCount() == null){
//                           notRepeatBook.setBorrowCount(1);
//                       }else {
//                           notRepeatBook.setBorrowCount(notRepeatBook.getBorrowCount()+1);
//                       }
//                   }
//               }
//
//
//            }
//        }

        return notRepeatBooks;
    }

    /**
     * @Author:         jjq
     * @CreateDate:     2020/4/3 15:22
     * @Version:        1.0
     * @Description:    借阅记录综合查询
     */
    @Override
    public List<UserAndBookAndBorrow> getRecordsByCondition(String phone, String card_number, String condition,Integer page,Integer limit,HttpSession session) throws ParseException {
        Integer index = 0;
        if(page > 1){
            index = (page - 1) * limit;
        }
        if((phone == null || phone == "" )&& (card_number==null ||card_number=="")){
            //得到所有记录
            //List<BorrowRecords> borrowRecordsList = borrowMapper.selectRecordsByUserIdList(null, condition);
            List<BorrowRecords> borrowRecordsList_1 = getRecords(null, condition,null,null,null, null,session);
//            System.out.println("borrowRecordsList:"+borrowRecordsList);
            if(borrowRecordsList_1 == null){
                return null;
            }
            ArrayList<Integer> user_id_list = new ArrayList<>();
            ArrayList<String> book_number_list = new ArrayList<>();
            for (BorrowRecords br :borrowRecordsList_1 ) {
                user_id_list.add(br.getUser_id());
                book_number_list.add(br.getBook_number());
            }
            List<BorrowRecords> borrowRecordsList_2 = getRecords(null, condition,index,limit,user_id_list, book_number_list,session);
            List<UserAndBookAndBorrow> borrowByCondition = getBorrowByCondition(borrowRecordsList_2, user_id_list, book_number_list);
            return borrowByCondition;
        }else {
            ArrayList<Integer> user_id_list = new ArrayList<>();
            ArrayList<String> book_number_list = new ArrayList<>();
            //如果通过phone得到用户
            if(phone != null && phone != ""){
                List<User> users = userMapper.queryUser(phone);
                if(users.size()==0){
                    return null;
                }
                for (User user: users ) {
                    user_id_list.add(user.getUser_id());
                }

            }else {
                //如果通过图书证编号得到用户
                LibraryCard libraryCard = cardMapper.selectCardsByNumber(card_number);
                if(libraryCard == null){
                    return null;
                }
                User user = userMapper.selectUserById(libraryCard.getUser_id());
                user_id_list.add(user.getUser_id());
            }
            //得到记录
            //List<BorrowRecords> borrowRecordsList = borrowMapper.selectRecordsByUserIdList(user_id_list, condition);
            List<BorrowRecords> borrowRecordsList_1 = getRecords(user_id_list, condition,null,null,null,null,session);
            if(borrowRecordsList_1 == null||borrowRecordsList_1.size()==0){
                return null;
            }
            for (BorrowRecords br :borrowRecordsList_1 ) {
                book_number_list.add(br.getBook_number());
            }
            List<BorrowRecords> borrowRecordsList_2 = getRecords(user_id_list, condition,index,limit,user_id_list, book_number_list,session);
            List<UserAndBookAndBorrow> borrowByCondition = getBorrowByCondition(borrowRecordsList_2, user_id_list, book_number_list);
            return borrowByCondition;
        }

    }

//========================================优秀读者处理方法======================================
    public List<User> getUserByCondition(List<BorrowRecords> borrowRecordsList, String gender, String condition, Integer index, Integer limit, HttpSession session) throws ParseException {
        ArrayList<Integer> list = new ArrayList<>();
        for (BorrowRecords borrowRecord : borrowRecordsList) {
            if (borrowRecord.getContinue_date() != null) {
                Date continue_date = borrowRecord.getContinue_date();
                Date endTime = DateUtil.getCalendar(continue_date, 30);
                Date nowDate = DateUtil.getNowDate();
                if (endTime.before(nowDate)) {
                    //已经过期
                    list.add(borrowRecord.getUser_id());
                }
            } else {
                Date out_date = borrowRecord.getOut_date();
                Date overTime = DateUtil.getCalendar(out_date, 30);
                Date nowDate = DateUtil.getNowDate();
                if (overTime.before(nowDate)) {
                    //已经过期
                    list.add(borrowRecord.getUser_id());
                }
            }
        }

        List<Integer> user_id_list = new ArrayList<>();
        if(borrowRecordsList.size() == 0){
            //如果无借书记录，这返回null
            List<User> users =new ArrayList<>();
            return users;
        }

        //获取所有的记录的user_id
        for (BorrowRecords borrowRecord : borrowRecordsList) {
            user_id_list.add(borrowRecord.getUser_id());
        }
        if(list.size()==0){
            //如果无过期记录
            LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet<>(user_id_list);
            ArrayList<Integer> userIds = new ArrayList<>(linkedHashSet);
            //查找筛选出来的读者
            List<User> users_List = userMapper.selectUsersByCondition(userIds, gender, condition,null,null);
            session.setAttribute("users_List",users_List);
            List<User> users_2 = userMapper.selectUsersByCondition(userIds, gender, condition,index,limit);
            return users_2;
        }else {
            //如果有过期记录
            //利用LinkedHashSet去重
            LinkedHashSet<Integer> linkedHashSet_1 = new LinkedHashSet<>(user_id_list);//所有的记录的user_id
            LinkedHashSet<Integer> linkedHashSet_2 = new LinkedHashSet<>(list);//又过期记录的user_id

            ArrayList<Integer> userIds = new ArrayList<>(linkedHashSet_1);
            ArrayList<Integer> overdue_list = new ArrayList<>(linkedHashSet_2);
            for(int i =0 ;i<userIds.size();i++){
                for (int j=0 ;j<overdue_list.size();j++){
                    if(userIds.get(i) == overdue_list.get(j)){
                        userIds.remove(i);
                    }
                }
            }
            //查找筛选出来的读者
                if(userIds.size()==0){
                    //如果筛选出来的读者都有过期记录，这返回null
                    List<User> users =new ArrayList<>();
                    return users;
                }else {
                    List<User> users_List = userMapper.selectUsersByCondition(userIds, gender, condition,null,null);
                    session.setAttribute("users_List",users_List);
                    List<User> users_2 = userMapper.selectUsersByCondition(userIds, gender, condition,index,limit);
                    return users_2;
                }
        }
    }


//==================借阅记录综合查询======数据封装================================

    public List<UserAndBookAndBorrow> getBorrowByCondition(List<BorrowRecords> borrowRecordsList,
                                                           List<Integer> user_id_list ,
                                                           List<String> book_id_list ){
        ArrayList<UserAndBookAndBorrow> userAndBookAndBorrows = new ArrayList<>();
        for (BorrowRecords br :borrowRecordsList ) {
            UserAndBookAndBorrow userAndBookAndBorrow = new UserAndBookAndBorrow();
            userAndBookAndBorrow.setUser_id(br.getUser_id());
            userAndBookAndBorrow.setBook_number(br.getBook_number());
            userAndBookAndBorrow.setContinue_date(br.getContinue_date());
            userAndBookAndBorrow.setRecord_id(br.getRecord_id());
            userAndBookAndBorrow.setEffective_time(br.getEffective_time());
            userAndBookAndBorrow.setOut_date(br.getOut_date());
            userAndBookAndBorrow.setReturn_date(br.getReturn_date());
            userAndBookAndBorrow.setReturn_state(br.getReturn_state());
            userAndBookAndBorrow.setContinue_num(br.getContinue_num());
            userAndBookAndBorrows.add(userAndBookAndBorrow);
        }
        //得到用户
        List<User> users = userMapper.selectUserByIdList(user_id_list);
        //得到书籍
        List<Book> books = bookMapper.selectBooksByNumberList(book_id_list);

        for (UserAndBookAndBorrow uabab:userAndBookAndBorrows ) {
            for (User u:users ) {
                if(uabab.getUser_id() == u.getUser_id()){
                    uabab.setUser_name(u.getUser_name());
                    uabab.setPhone(u.getPhone());
                }
            }
        }

        for (UserAndBookAndBorrow uabab:userAndBookAndBorrows) {
            for (Book b :books ) {
                if(uabab.getBook_number().equals(b.getBook_number())){
                    uabab.setBook_name(b.getBook_name());
                }
            }
        }
        return userAndBookAndBorrows;
    }

    //===================按条件查询借阅记录======方法============================
    public List<BorrowRecords> getRecords(List<Integer> user_id,String condition,Integer index,Integer limit,
                                          List<Integer> user_id_list , List<String> book_id_list ,HttpSession session) throws ParseException {
        if ("1".equals(condition)) {
            //phone不为空且condition为已归还
            List<BorrowRecords> borrowRecords_1 = borrowMapper.selectRecordsByUserIdList(user_id, "1",null,null);
            if(user_id_list != null && book_id_list !=null){
                List<UserAndBookAndBorrow> borrowByCondition = getBorrowByCondition(borrowRecords_1, user_id_list, book_id_list);
                session.setAttribute("borrowRecords_count",borrowByCondition);
            }
            List<BorrowRecords> borrowRecords_2 = borrowMapper.selectRecordsByUserIdList(user_id, "1",index,limit);
            return borrowRecords_2;
        } else if ("0".equals(condition)) {
            //phone不为空且condition为未归还
            List<BorrowRecords> borrowRecords_1 = borrowMapper.selectRecordsByUserIdList(user_id, "0",null,null);
            if(user_id_list != null && book_id_list !=null){
                List<UserAndBookAndBorrow> borrowByCondition = getBorrowByCondition(borrowRecords_1, user_id_list, book_id_list);
                session.setAttribute("borrowRecords_count",borrowByCondition);
            }
            List<BorrowRecords> borrowRecords_2 = borrowMapper.selectRecordsByUserIdList(user_id, "0",index,limit);
            return borrowRecords_2;
        } else if ("-1".equals(condition)) {
            //phone不为空且condition为逾期未归还
            List<BorrowRecords> borrowRecords = borrowMapper.selectRecordsByUserIdList(user_id, "0",null,null);
            if(borrowRecords == null || borrowRecords.size() == 0){
                return null;
            }
            List<Integer> borrowRecords_ids_List = new ArrayList<>();
            for (BorrowRecords borrowRecord : borrowRecords) {
                if (borrowRecord.getContinue_date() != null) {
                    Date continue_date = borrowRecord.getContinue_date();
                    Date endTime = DateUtil.getCalendar(continue_date, 30);
                    Date nowDate = DateUtil.getNowDate();
                    if (endTime.before(nowDate)) {
                        //已经过期
                        borrowRecords_ids_List.add(borrowRecord.getRecord_id());
                    }
                } else {
                    Date out_date = borrowRecord.getOut_date();
                    Date overTime = DateUtil.getCalendar(out_date, 30);
                    Date nowDate = DateUtil.getNowDate();
                    if (overTime.before(nowDate)) {
                        //已经过期
                        borrowRecords_ids_List.add(borrowRecord.getRecord_id());
                    }
                }
            }
            List<BorrowRecords> borrowRecordsList_1 = borrowMapper.selectRecordsByRecordIdList(borrowRecords_ids_List,null,null);
            if(user_id_list != null && book_id_list !=null){
                List<UserAndBookAndBorrow> borrowByCondition = getBorrowByCondition(borrowRecordsList_1, user_id_list, book_id_list);
                session.setAttribute("borrowRecords_count",borrowByCondition);
            }
            List<BorrowRecords> borrowRecordsList_2 = borrowMapper.selectRecordsByRecordIdList(borrowRecords_ids_List,index,limit);
            return borrowRecordsList_2;
        }else {
            List<BorrowRecords> borrowRecords_1 = borrowMapper.selectRecordsByUserIdList(user_id,condition,null,null);
            if(user_id_list != null && book_id_list !=null){
                List<UserAndBookAndBorrow> borrowByCondition = getBorrowByCondition(borrowRecords_1, user_id_list, book_id_list);
                session.setAttribute("borrowRecords_count",borrowByCondition);
            }
            List<BorrowRecords> borrowRecords_2 = borrowMapper.selectRecordsByUserIdList(user_id,condition,index,limit);
            return borrowRecords_2;
        }

    }

    //========================书籍综合查询的数据封装方法=============================
    public List<NotRepeatBook>  notRepeatFunction(List<Book> booksByDistinct,List<Book> books){
        ArrayList<NotRepeatBook> notRepeatBooks = new ArrayList<>();
        //先将不同书名的数据创建好，方便后面加数据
        for (Book b:booksByDistinct) {
            NotRepeatBook book = new NotRepeatBook();
            book.setBook_name(b.getBook_name());
            notRepeatBooks.add(book);
        }

        for (NotRepeatBook notRepeatBook :notRepeatBooks ) {
            for (Book b :books ) {
                if(b.getBook_name().equals(notRepeatBook.getBook_name())) {
                    if(notRepeatBook.getTotalCount()==null){
                        notRepeatBook.setTotalCount(1);
                        notRepeatBook.setBook_name(b.getBook_name());
                        notRepeatBook.setAuthor(b.getAuthor());
                        notRepeatBook.setPublisher(b.getPublisher());
                        notRepeatBook.setPublish_time(b.getPublish_time());
                        notRepeatBook.setClassification(b.getClassification());
                        notRepeatBook.setIntroduce(b.getIntroduce());
                    }else {
                        int i = notRepeatBook.getTotalCount() + 1;
                        notRepeatBook.setTotalCount(i);
                    }
                    if(b.getState() == 1){
                        //未借出
                        if(notRepeatBook.getNotBorrowCount()==null){
                            notRepeatBook.setNotBorrowCount(1);
                        }else {
                            notRepeatBook.setNotBorrowCount(notRepeatBook.getNotBorrowCount()+1);
                        }
                    }else {
                        if(notRepeatBook.getBorrowCount() == null){
                            notRepeatBook.setBorrowCount(1);
                        }else {
                            notRepeatBook.setBorrowCount(notRepeatBook.getBorrowCount()+1);
                        }
                    }
                }


            }
        }
        return notRepeatBooks;

    }


}
