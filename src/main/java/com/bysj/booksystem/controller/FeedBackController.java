package com.bysj.booksystem.controller;

import com.bysj.booksystem.model.FeedBack;
import com.bysj.booksystem.model.JsonFormatUtils;
import com.bysj.booksystem.model.UserAndOpinion;
import com.bysj.booksystem.service.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("feedBackController")
public class FeedBackController {

    @Autowired
    private FeedBackService feedBackService;

    //读者端反馈意见
    @RequestMapping("addOpinion")
    @ResponseBody
    public String addOpinion(@RequestBody FeedBack feedBack, HttpSession session){
        String s = feedBackService.addOpinion(feedBack, session);
        return s;
    }

    //后台意见表
    @RequestMapping("getOpinions")
    @ResponseBody
    public JsonFormatUtils<UserAndOpinion> getOpinions(String rangeDate, String condition, HttpServletRequest request,HttpSession session) throws ParseException {
        Integer page = Integer.parseInt(request.getParameter("page"));
        Integer limit = Integer.parseInt(request.getParameter("limit"));

        List<UserAndOpinion> opinions = feedBackService.getOpinions(rangeDate, condition,page,limit,session);
        List<FeedBack> opinions_count = (List<FeedBack>)session.getAttribute("opinions_count");

        JsonFormatUtils<UserAndOpinion> userAndOpinionJsonFormatUtils = new JsonFormatUtils<>();
        userAndOpinionJsonFormatUtils.setCode(0);
        userAndOpinionJsonFormatUtils.setMsg("");
        userAndOpinionJsonFormatUtils.setCount(opinions_count.size());
        userAndOpinionJsonFormatUtils.setData(opinions);
        return userAndOpinionJsonFormatUtils;
    }

    //处理
    @RequestMapping("deal_with")
    @ResponseBody
    public String deal_with(String ids){
        String s = feedBackService.deal_with(ids);
        return s;
    }
}
