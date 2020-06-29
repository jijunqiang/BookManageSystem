package com.bysj.booksystem.controller;

import com.bysj.booksystem.model.Book;
import com.bysj.booksystem.model.JsonFormatUtils;
import com.bysj.booksystem.model.antv_bookData;
import com.bysj.booksystem.service.BookService;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.BooleanLiteral;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("bookController")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("addBook")
    @ResponseBody
    public String addBook(@RequestBody Book book){
//        System.out.println("========controller============");
//        System.out.println(book);
        String book_number = book.getBook_number();
        Book b = bookService.getBookByNum(book_number);
        if(b !=null){
            //如果已经存在这本书
            return "1";
        }else {
            //如果不存在
            int i = bookService.addBook(book);
            if (i > 0 ){
                //添加成功
                return "0";
            }else {
                //添加失败
                return "-1";
            }
        }
    }

    @RequestMapping("allBook")
    @ResponseBody
    public JsonFormatUtils<Book> bookList(Model model, HttpServletRequest request, HttpSession session){
        /**
         * 当前页
         */
        Integer page = Integer.parseInt(request.getParameter("page"));
        /**
         * 每页的数量
         */
        Integer limit = Integer.parseInt(request.getParameter("limit"));
        List<Book> allBook = bookService.getAllBook(page,limit,session);
        /**
         * 获取总数量
         */
        Integer bookCount = bookService.getBookCount(null);
        if(bookCount == null){
            bookCount =0;
        }
        JsonFormatUtils<Book> bookJsonFormatUtils = new JsonFormatUtils<>();
        bookJsonFormatUtils.setCode(0);
        bookJsonFormatUtils.setMsg("");
        bookJsonFormatUtils.setCount(bookCount);
        bookJsonFormatUtils.setData(allBook);
        //model.addAttribute("bookList",allBook);
        return bookJsonFormatUtils;
    }


    //修改书籍信息
    @RequestMapping("updateBook")
    @ResponseBody
    public String updateBook(@RequestBody Book book){
        Book bookByNum = null;
        if(!(book.getBook_number().equals("book_number"))){
            String book_number = book.getBook_number();
            bookByNum = bookService.getBookByNum(book_number);
        }
        if (bookByNum == null){
            int i = bookService.upadteBook(book);
            System.out.println(i);
            if(i>0){
                //修改成功
                return "0";
            }else {
                //修改失败
                return "-1";
            }
        }else{
            //图书编号已存在
            return "1";
        }
    }

    //删除书籍
    @RequestMapping("deleteBook")
    @ResponseBody
    public int deleteBook(String ids){
        List<Integer> list = new ArrayList<>();
        String[] split = ids.split(",");
        for (String id :split ) {
            int i = Integer.parseInt(id);
            list.add(i);
        }
        int i = bookService.deleteBook(list);
        return i;
    }

    //按条件查询书籍
    @RequestMapping("selectBook")
    @ResponseBody
    public JsonFormatUtils<Book> selectBook( Book book,HttpServletRequest request,HttpSession session){
        String parameter_1 = request.getParameter("page");
        String parameter_2 =request.getParameter("limit");
        List<Book> books = null;
        if(parameter_1 == null && parameter_2 ==null){
            books = bookService.selectBook(book,null,null,session);
        }else {
            Integer page = Integer.parseInt(parameter_1);
            Integer limit = Integer.parseInt(parameter_2);
           books = bookService.selectBook(book,page,limit,session);
        }

        Integer bookCount = bookService.getBookCount(book);
        if(bookCount == null){
            bookCount=0;
        }
        JsonFormatUtils<Book> bookJsonFormatUtils = new JsonFormatUtils<>();
        bookJsonFormatUtils.setCode(0);
        bookJsonFormatUtils.setMsg("");
        bookJsonFormatUtils.setCount(bookCount);
        bookJsonFormatUtils.setData(books);
        return bookJsonFormatUtils;
    }

    //根据图书编号查询书籍
    @RequestMapping("getBookByNumber")
    @ResponseBody
    public Book getBookByNumber(String book_number){
        Book book = bookService.getBookByNum(book_number);
        return book;
    }

    //读者端查询书籍
    @RequestMapping("getBooksFromReader")
    @ResponseBody
    public List<Book> getBooksFromReader(String key_words,String condition,String page, String limit){
        Integer page_1 = Integer.parseInt(page);
        Integer limit_1 = Integer.parseInt(limit);
        List<Book> booksFromReader = bookService.getBooksFromReader(key_words, condition,page_1,limit_1);
        return booksFromReader;
    }

    //根据图书编号查询书籍
    @RequestMapping("getBookInfo")
    public String getBookInfo(String book_number,Model model){
        Book book = bookService.getBookByNum(book_number);
        model.addAttribute("bookInfo",book);
        return "bookPage/bookInfo";
    }

    //获取书籍
    @RequestMapping(value = "getBooks")
    @ResponseBody
    public List<Book> getBooks(String book_number,String book_name,String author,String classification,String publisher,Integer state,HttpSession session){
        Book book = new Book();
        book.setBook_number(book_number);
        book.setBook_name(book_name);
        book.setAuthor(author);
        book.setClassification(classification);
        book.setPublisher(publisher);
        book.setState(state);
        List<Book> books = bookService.selectBook(book, null, null,session);
        return  books;
    }

    //读者端查询书籍,获取书籍数量
    @RequestMapping(value = "getBooksFromReaderCount")
    @ResponseBody
    public Integer getBooksFromReaderCount(String key_words,String condition){
        List<Book> booksFromReader = bookService.getBooksFromReader(key_words, condition,null,null);
        int size = booksFromReader.size();
        return size;

    }

    //得到借阅书籍的分类数量
    @RequestMapping(value = "getBookClassficaCount")
    @ResponseBody
    public List<antv_bookData> getBookClassficaCount(){
        List<antv_bookData> bookClassficaCount = bookService.getBookClassficaCount();
        return bookClassficaCount;
    }


}
