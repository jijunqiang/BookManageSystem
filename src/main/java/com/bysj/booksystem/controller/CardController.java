package com.bysj.booksystem.controller;

import com.bysj.booksystem.model.JsonFormatUtils;
import com.bysj.booksystem.model.LibraryCard;
import com.bysj.booksystem.model.User;
import com.bysj.booksystem.service.CardService;
import com.bysj.booksystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("libraryCard")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private UserService userService;

    //办理图书证
    @RequestMapping("handle")
    @ResponseBody
    public int handleCard(Integer user_id){
        System.out.println(user_id);
        LibraryCard cardByUserId = cardService.getCardByUserId(user_id);
        if(cardByUserId == null){
            String card_number = UUID.randomUUID().toString();
            LibraryCard libraryCard = new LibraryCard();
            libraryCard.setCard_number(card_number);
            libraryCard.setUser_id(user_id);
            libraryCard.setEffective(1);

            int i = cardService.handleCard(libraryCard);
            return i;
        }else {
            return -1;
        }

    }

    //得到所有图书证
    @RequestMapping("getAllCard")
    @ResponseBody
    public JsonFormatUtils<LibraryCard> getAllCard(HttpServletRequest request){
        Integer page = Integer.parseInt(request.getParameter("page"));
        Integer limit = Integer.parseInt(request.getParameter("limit"));

        List<LibraryCard> allCard = cardService.getAllCard(page,limit);

        Integer cardCount = cardService.getCardCount(null);
        if(cardCount ==null){
            cardCount =0;
        }
        JsonFormatUtils<LibraryCard> libraryCardJsonFormatUtils = new JsonFormatUtils<>();
        libraryCardJsonFormatUtils.setCode(0);
        libraryCardJsonFormatUtils.setMsg(null);
        libraryCardJsonFormatUtils.setCount(cardCount);
        libraryCardJsonFormatUtils.setData(allCard);
        return libraryCardJsonFormatUtils;
    }

    //图书证挂失
    @RequestMapping("delCard")
    @ResponseBody
    public int delCard(String card_number){
        int i = cardService.delCard(card_number);
        if(i > 0){
            return i;
        }else {
            return -1;
        }

    }

    //根据手机号查询图书证
    @RequestMapping("getCardByUserId")
    @ResponseBody
    public JsonFormatUtils<LibraryCard> getCardByUserId(String ids,HttpServletRequest request){
        Integer page = Integer.parseInt(request.getParameter("page"));
        Integer limit = Integer.parseInt(request.getParameter("limit"));

        ArrayList<Integer> id_list = new ArrayList<>();
        String[] split = ids.split(",");
        for (int i = 0 ;i<split.length;i++){
            id_list.add(Integer.parseInt(split[i]));
        }
        List<LibraryCard> cards = cardService.getCards(id_list,page,limit);
        Integer cardCount = cardService.getCardCount(id_list);
        JsonFormatUtils<LibraryCard> libraryCardJsonFormatUtils = new JsonFormatUtils<LibraryCard>();
        libraryCardJsonFormatUtils.setCode(0);
        libraryCardJsonFormatUtils.setMsg(null);
        libraryCardJsonFormatUtils.setCount(cardCount);
        libraryCardJsonFormatUtils.setData(cards);
        return libraryCardJsonFormatUtils;
    }

    //根据图书证编号得到用户信息
    @RequestMapping("getUserInfo")
    @ResponseBody
    public User getUserInfo(String card_number){
        LibraryCard libraryCard = cardService.getCardsByNumber(card_number);
        if(libraryCard != null){
            Integer user_id = libraryCard.getUser_id();
            User usrById = userService.getUsrById(user_id);
            return usrById;
        }else {
            return null;
        }

    }


}
