package com.bysj.booksystem.service;

import com.bysj.booksystem.model.FeedBack;
import com.bysj.booksystem.model.UserAndOpinion;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.List;

public interface FeedBackService {

    //读者端反馈意见
    public String addOpinion(FeedBack feedBack, HttpSession session);

    //未处理意见数量
    public Integer notDealWith();

    //后台意见反馈
    public List<UserAndOpinion>getOpinions(String rangeDate,String condition,Integer page,Integer limit,HttpSession session) throws ParseException;

    //处理
    public String deal_with(String ids);
}
