package com.bysj.booksystem.dao;

import com.bysj.booksystem.model.Book;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookMapper {

    //根据图书编号查询书籍
    public Book selectBookByNum(String book_number);

    //添加书籍
    public int insetBook(@Param("book") Book book);

    //得到所有书籍
    public List<Book> selectAllBook(@Param("index") Integer index,@Param("limit") Integer limit);

    //修改书籍信息
    public int updateBook(@Param("book") Book book);

    //删除书籍
    public int deleteBook(@Param("list") List<Integer> list);

    //根据条件查询书籍
    public List<Book> selectBook(@Param("book") Book book,@Param("index") Integer index,@Param("limit") Integer limit);

    //借书，修改书本状态
    public int updateState(@Param("list") List<String> list);

    //根据书籍id查找书籍
    public List<Book> selectBooksByIds(@Param("bookIds") List<Integer> bookIds);

    //根据书籍编号查找书籍
    public List<Book> selectBooksByNumberList(@Param("bookNumberList") List<String> bookNumberList);

    //图书名查询图书
    public List<Book> selectBookByBookName(@Param("book_name") String book_name);

    //读者端查询书籍
    public List<Book> selectBooksFromReader (@Param("key_words") String key_words,@Param("condition") String condition,@Param("index")Integer index,@Param("limit")Integer limit);

    //还书=====修改书籍借出状态
    public Integer updateBookState(@Param("bookNumbers") String[] bookNumbers);

    //获取总数量
    public Integer getBookCount(@Param("book") Book book);

    //查询无重复书名的数据，用于分页（综合查询-图书查询）
    public List<Book> getBooksByDistinct(@Param("book_Ids") List<Integer> book_Ids,@Param("index") Integer index,@Param("limit") Integer limit);
}
