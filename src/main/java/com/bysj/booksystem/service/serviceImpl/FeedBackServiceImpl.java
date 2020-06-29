package com.bysj.booksystem.service.serviceImpl;

import com.bysj.booksystem.dao.FeedBackMapper;
import com.bysj.booksystem.dao.UserMapper;
import com.bysj.booksystem.model.FeedBack;
import com.bysj.booksystem.model.User;
import com.bysj.booksystem.model.UserAndOpinion;
import com.bysj.booksystem.service.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class FeedBackServiceImpl implements FeedBackService {

    @Autowired(required = false)
    private FeedBackMapper feedBackMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Override
    public String addOpinion(FeedBack feedBack, HttpSession session) {
        User loginUser = (User)session.getAttribute("loginUser");
        Date date = new Date();
        java.sql.Date nowDate= new java.sql.Date(date.getTime());
        feedBack.setUser_id(loginUser.getUser_id());
        feedBack.setFeedback_time(nowDate);
        feedBack.setHandle("未处理");
        Integer integer = feedBackMapper.addOpinion(feedBack);
        if(integer >0){
            return "提交成功";
        }else {
            return "提交失败";
        }
    }

    @Override
    public Integer notDealWith() {
        Integer integer = feedBackMapper.notDealWith();
        return integer;
    }

    @Override
    public List<UserAndOpinion> getOpinions(String rangeDate,String condition,Integer page,Integer limit ,HttpSession session) throws ParseException {
        Integer index = 0;
        if(page > 1){
            index = (page - 1) * limit;
        }
        if("".equals(rangeDate) || rangeDate == null){
            List<UserAndOpinion> opinionFunction = getOpinionFunction(null, null, condition,index,limit,session);
            return opinionFunction;
        }
        String[] split = rangeDate.split(" - ");
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
//        Date date_1 = simpleDateFormat.parse(split[0]);
//        Date date_2= simpleDateFormat.parse(split[1]);
//        java.sql.Date startDate = new java.sql.Date(date_1.getTime());
//        java.sql.Date endDate = new java.sql.Date(date_2.getTime());
        List<UserAndOpinion> opinionFunction = getOpinionFunction(split[0], split[1], condition,index,limit,session);
        return opinionFunction;

    }

    @Override
    public String deal_with(String ids) {
        String[] split = ids.split(",");
        ArrayList<Integer> list = new ArrayList<>();
        for (String id :split ) {
            list.add(Integer.parseInt(id));
        }
        Integer integer = feedBackMapper.updateHandle(list);
        if(integer >0){
            return "已处理";
        }
        return "处理失败";
    }

    //================================获得意见反馈方法===============================
    public List<UserAndOpinion> getOpinionFunction(String startDate,String endDate,String condition,Integer index,Integer limit,HttpSession session){
        List<FeedBack> opinions_1 = feedBackMapper.getOpinions(startDate, endDate, condition,null,null);
        session.setAttribute("opinions_count",opinions_1);
        List<FeedBack> opinions_2 = feedBackMapper.getOpinions(startDate, endDate, condition,index,limit);
        if(opinions_2 != null && opinions_2.size() >0){
            ArrayList<Integer> list = new ArrayList<>();
            ArrayList<UserAndOpinion> userAndOpinionList = new ArrayList<>();
            for (FeedBack feedBack :opinions_2 ) {
                list.add(feedBack.getUser_id());
                UserAndOpinion userAndOpinion = new UserAndOpinion();
                userAndOpinion.setOpinion_id(feedBack.getOpinion_id());
                userAndOpinion.setUser_id(feedBack.getUser_id());
                userAndOpinion.setFeedback_time(feedBack.getFeedback_time());
                userAndOpinion.setContext(feedBack.getContext());
                userAndOpinion.setHandle(feedBack.getHandle());
                userAndOpinionList.add(userAndOpinion);
            }
            List<User> users = userMapper.selectUserByIdList(list);
            for (UserAndOpinion userAndOpinion :userAndOpinionList ) {
                for (User user : users ) {
                    if(userAndOpinion.getUser_id() == user.getUser_id()){
                        userAndOpinion.setUser_name(user.getUser_name());
                        userAndOpinion.setPhone(user.getPhone());
                    }
                }
            }
            return userAndOpinionList;
        }else {
            return null;
        }
    }


}
