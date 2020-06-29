package com.bysj.booksystem.dao;

import com.bysj.booksystem.model.FeedBack;
import com.bysj.booksystem.model.UserAndOpinion;
import org.apache.ibatis.annotations.Param;

import java.beans.IntrospectionException;
import java.sql.Date;
import java.util.List;

public interface FeedBackMapper {

    //读者端反馈意见
    public Integer addOpinion(@Param("feedBack") FeedBack feedBack);

    //未处理意见数量
    public Integer notDealWith();

    //后台意见反馈
    public List<FeedBack> getOpinions(@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("condition") String condition,@Param("index")Integer index,@Param("limit")Integer limit);

    //处理
    public Integer updateHandle(@Param("ids") List<Integer> ids);

}
