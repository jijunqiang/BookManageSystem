package com.bysj.booksystem.service;

import com.bysj.booksystem.model.LibraryCard;
import com.bysj.booksystem.model.User;

import java.util.List;

public interface CardService {

    //办理图书证
    public int handleCard(LibraryCard libraryCard);

    //得到所有图书证
    public List<LibraryCard> getAllCard(Integer page,Integer limit);

    //删除图书证
    public int delCard(String card_number);

    //根据用户id得到图书证
    public LibraryCard getCardByUserId(int user_id);

    //根据用户id(list)得到图书证
    public List<LibraryCard> getCards(List<Integer> list,Integer page,Integer limit);

    //根据图书证编号得到图书证
    public LibraryCard getCardsByNumber(String card_number);

    //获得图书证数量
    public Integer getCardCount(List<Integer> id_list);

}
