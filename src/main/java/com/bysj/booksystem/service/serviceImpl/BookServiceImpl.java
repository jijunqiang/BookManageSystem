package com.bysj.booksystem.service.serviceImpl;

import com.bysj.booksystem.dao.BookMapper;
import com.bysj.booksystem.dao.BorrowMapper;
import com.bysj.booksystem.model.Book;
import com.bysj.booksystem.model.BorrowRecords;
import com.bysj.booksystem.model.antv_bookData;
import com.bysj.booksystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired(required = false)
    private BookMapper bookMapper;

    @Autowired(required = false)
    private BorrowMapper borrowMapper;

    //添加书籍
    @Override
    public int addBook(Book book) {
//        System.out.println("========service============");
//        System.out.println(book);
        int i = bookMapper.insetBook(book);
        return i;
    }

    //根据图书编号查询书籍
    @Override
    public Book getBookByNum(String book_number) {
        return bookMapper.selectBookByNum(book_number);
    }

    //得到所有书籍
    @Override
    public List<Book> getAllBook(Integer page, Integer limit,HttpSession session) {
        Integer index = 0;
        if(page == null && limit == null){
            List<Book> books = bookMapper.selectAllBook(null,null);
            return books;
        }
        if(page > 1){
            index = (page - 1) * limit;
        }
        List<Book> books_1 = bookMapper.selectAllBook(null,null);
        session.setAttribute("exportBook",books_1);
        List<Book> books = bookMapper.selectAllBook(index,limit);
        return books;
    }

    //修改书籍信息
    @Override
    public int upadteBook(Book book) {
        int i = bookMapper.updateBook(book);
        return i;
    }

    @Override
    public int deleteBook(List<Integer> list) {
        List<Book> books = bookMapper.selectBooksByIds(list);
        for (Book book : books) {
            if(book.getState()==0){
                return -1;
            }
        }
        int i = bookMapper.deleteBook(list);
        return i;
    }

    @Override
    public List<Book> selectBook(Book book,Integer page,Integer limit,HttpSession session) {
        Integer index = 0;
        if(page == null && limit ==null){
            List<Book> books = bookMapper.selectBook(book,null,null);
            return books;
        }
        if(page > 1){
            index = (page - 1) * limit;
        }
        List<Book> books_1 = bookMapper.selectBook(book,null,null);
        session.setAttribute("exportBook",books_1);
        List<Book> books = bookMapper.selectBook(book,index,limit);
        return books;
    }

    @Override
    public int modifyState(List<String> list) {
        int i = bookMapper.updateState(list);
        return i;
    }

    @Override
    public List<Book> getBooksFromReader(String key_words, String condition,Integer page,Integer limit) {
        Integer index = 0;
        if(page == null && limit == null){
            index = null;
        }else if(page > 1){
            index = (page - 1) * limit;
        }
        List<Book> books = bookMapper.selectBooksFromReader(key_words, condition,index,limit);
        return books;
    }

    @Override
    public Integer getBookCount(Book book) {

        Integer bookCount = bookMapper.getBookCount(book);
        return bookCount;
    }

    @Override
    public List<antv_bookData> getBookClassficaCount() {

        String arr[] = {"古典","小说","科学","历史","教材","儿童","哲学","法律","经济","体育"};
        List<BorrowRecords> bookRecords = borrowMapper.getBookRecords();
        if(bookRecords == null || bookRecords.size()<=0){
            return null;
        }
        ArrayList<antv_bookData> antv_bookDataList = new ArrayList<>();
        for (String s :arr ) {
            antv_bookData antv_bookData = new antv_bookData();
            antv_bookData.setClassfica(s);
            antv_bookData.setCount(0);
            antv_bookDataList.add(antv_bookData);
        }

        ArrayList<String> list = new ArrayList<>();
        for (BorrowRecords b :bookRecords ) {
            list.add(b.getBook_number());
        }

        List<Book> books = bookMapper.selectBooksByNumberList(list);
        for (Book book :books ) {
            for (antv_bookData al :antv_bookDataList ) {
                if(book.getClassification().contains(al.getClassfica())){
                    al.setCount(al.getCount()+1);
                }
            }

        }

        return antv_bookDataList;
    }

}
