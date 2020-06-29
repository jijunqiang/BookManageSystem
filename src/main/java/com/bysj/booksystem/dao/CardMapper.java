package com.bysj.booksystem.dao;

import com.bysj.booksystem.model.LibraryCard;
import com.bysj.booksystem.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CardMapper {

    //办理图书证
    public int insertCard(@Param("libraryCard") LibraryCard libraryCard);

    //得到所有图书证
    public List<LibraryCard> selectAll(@Param("index") Integer index,@Param("limit") Integer limit);

    //删除图书证
    public int delCard(@Param("card_number") String card_number);

    //根据用户id得到图书证
    public LibraryCard getCardByUserId(@Param("user_id")int user_id);

    //根据用户id(list)得到图书证
    public List<LibraryCard> selectCards(List<Integer> list,@Param("index") Integer index,@Param("limit") Integer limit);

    //根据图书证编号得到图书证
    public LibraryCard selectCardsByNumber(@Param("card_number") String card_number);

    //获得图书证数量
    public Integer getCardCount(@Param("id_list") List<Integer> id_list);

}
