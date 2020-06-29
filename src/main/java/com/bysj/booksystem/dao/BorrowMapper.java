package com.bysj.booksystem.dao;

import com.bysj.booksystem.model.BorrowRecords;
import com.bysj.booksystem.model.UserAndBookAndBorrow;
import com.bysj.booksystem.model.antv_bookData;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.List;

public interface BorrowMapper {

    //插入借阅记录
    public int insertBorrowRecords(@Param("list") List<BorrowRecords> list);

    //根据user_id查询记录
    public List<BorrowRecords> getRecordsByUserId(@Param("user_id") Integer user_id,@Param("array") String[] array);

    //续借===通过user_id和图书编号
    public int renew(@Param("user_id") Integer user_id, @Param("array") String[] array,@Param("renewDate") Date renewDate);

    //归还===通过user_id和图书编号
    public int returnBook(@Param("user_id") Integer user_id,@Param("array") String[] bookNumbers,@Param("returnDate") Date returnDate);

    //查询所有借阅记录
    public List<BorrowRecords> selectAllRecords(@Param("index") Integer index,@Param("limit") Integer limit);

    //根据user_id查询记录(button按钮/借阅记录页面)
    public List<BorrowRecords> selectRecordsByUserIdList(@Param("user_ids") List<Integer> user_ids,@Param("condition") String condition,@Param("index") Integer index,@Param("limit") Integer limit);

    //通过user_id（List<Integer>）查询记录
    public List<BorrowRecords> queryRecordsByUserIds(@Param("ids") List<Integer> ids);

    //通过图书编号查询记录
    public List<BorrowRecords> selectRecordsByBookNumber(@Param("list") List<String> list);

    //============读者端======读者的借阅记录========================
    public List<BorrowRecords> getRecordsFromReader(@Param("user_id") Integer user_id, @Param("condition") String condition, @Param("index")Integer index, @Param("limit")Integer limit);

    //获取借阅记录的数量
    public Integer getRecordsCount(@Param("record_ids") List<Integer> record_ids);

    //通过record_id（List<Integer>）查询记录
    public List<BorrowRecords> selectRecordsByRecordIdList(@Param("record_ids") List<Integer> record_ids,@Param("index")Integer index,@Param("limit")Integer limit);

    //得到借阅书籍的分类数量
    public List<BorrowRecords> getBookRecords();
}
