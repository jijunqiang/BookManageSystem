package com.bysj.booksystem.controller;

import com.bysj.booksystem.dao.FeedBackMapper;
import com.bysj.booksystem.model.User;
import com.bysj.booksystem.service.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("page")
public class JumpPageController {

    @Autowired
    private FeedBackService feedBackService;

    @RequestMapping("main")
    public String loginMain(Model model,HttpSession session){
        Integer integer = feedBackService.notDealWith();
        Object badge_flag = session.getAttribute("badge_flag");//徽章标记，判断是否点击了“意见反馈”
        System.out.println(badge_flag+"=========");
        model.addAttribute("count",integer);
        model.addAttribute("bf",badge_flag);
        return "main";
    }

    @RequestMapping("reader")
    public String loginReader(HttpSession session, Model model){
        User loginUser = (User)session.getAttribute("loginUser");
        model.addAttribute("u",loginUser);
        return "readerPage";
    }

    @RequestMapping("success")
    public String loginSuccess(){
        return "success";
    }

    @RequestMapping("register")
    public String toRegister(){
        return "register";
    }

    @RequestMapping("addBook")
    public String toAddBook(){
        return "bookPage/addBook";
    }

    @RequestMapping("editBook")
    public String toEditBook(){
        return "bookPage/editBook";
    }

    @RequestMapping("handleLibraryCard")
    public String toHandleLibraryCard(){
        return "cardPage/handleLibraryCard";
    }

    @RequestMapping("lossAndHandle")
    public String toLossAndHandle(){
        return "cardPage/lossAndHandle";
    }

    @RequestMapping("borrowBook")
    public String toBorrowBook(){
        return "borrowing/borrowBooks";
    }

    @RequestMapping("renewalAndReturn")
    public String toRenewalAndReturn(){
        return "borrowing/renewalAndReturn";
    }

    @RequestMapping("borrowRecords")
    public String toBorrowRecords(){
        return "borrowing/borrowRecords";
    }

    @RequestMapping("userComprehensive")
    public String toUserComprehensive(){
        return "comprehensive/userComprehensive";
    }

    @RequestMapping("bookComprehensive")
    public String toBookComprehensive(){
        return "comprehensive/bookComprehensive";
    }

    @RequestMapping("borrowComprehensive")
    public String toBorrowComprehensive(){
        return "comprehensive/borrowComprehensive";
    }

    @RequestMapping("feedBack")
    public String toFeedBack(HttpSession session){
        session.setAttribute("badge_flag",1);
        return "feedBackPage/feedBack";
    }
}
