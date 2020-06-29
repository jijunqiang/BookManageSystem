package com.bysj.booksystem.service;

import com.bysj.booksystem.model.Book;
import com.bysj.booksystem.model.antv_bookData;

import javax.servlet.http.HttpSession;
import java.util.List;


public interface BookService {

    //添加一本书
    public int addBook(Book book);

    //根据书的编号查询
    public Book getBookByNum(String book_number);

    //获得所有书籍
    public List<Book> getAllBook(Integer page, Integer limit,HttpSession session);

    //修改书籍
    public int upadteBook(Book book);

    //删除书籍
    public int deleteBook(List<Integer> list);

    //根据条件查询书籍
    public List<Book> selectBook(Book book,Integer page,Integer limit,HttpSession session);

    //借书，修改书本状态
    public int modifyState(List<String> list);

    //读者端查询书籍
    public List<Book> getBooksFromReader (String key_words,String condition,Integer page,Integer limit);

    //获取总数量
    public Integer getBookCount(Book book);

    //得到借阅书籍的分类数量
    public List<antv_bookData> getBookClassficaCount();


}
