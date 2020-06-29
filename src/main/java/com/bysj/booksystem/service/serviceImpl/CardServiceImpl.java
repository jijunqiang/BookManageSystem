package com.bysj.booksystem.service.serviceImpl;

import com.bysj.booksystem.dao.CardMapper;
import com.bysj.booksystem.model.LibraryCard;
import com.bysj.booksystem.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    @Autowired(required = false)
    private CardMapper cardMapper;

    @Override
    public int handleCard(LibraryCard libraryCard) {
        int i = cardMapper.insertCard(libraryCard);
        return i;
    }

    @Override
    public List<LibraryCard> getAllCard(Integer page,Integer limit) {
        Integer index = 0;
        if(page > 1){
            index = (page - 1) * limit;
        }
        List<LibraryCard> libraryCards = cardMapper.selectAll(index,limit);
        return libraryCards;
    }

    @Override
    public int delCard(String card_number) {
        int i = cardMapper.delCard(card_number);
        return i;
    }

    @Override
    public LibraryCard getCardByUserId(int user_id) {

        return cardMapper.getCardByUserId(user_id);
    }

    @Override
    public List<LibraryCard> getCards(List<Integer> list,Integer page,Integer limit) {
        Integer index = 0;
        if(page > 1){
            index = (page - 1) * limit;
        }
        List<LibraryCard> libraryCards = cardMapper.selectCards(list,index,limit);
        return libraryCards;
    }

    @Override
    public LibraryCard getCardsByNumber(String card_number) {
        LibraryCard libraryCard = cardMapper.selectCardsByNumber(card_number);
        return libraryCard;
    }

    @Override
    public Integer getCardCount(List<Integer> id_list) {
        Integer cardCount = cardMapper.getCardCount(id_list);
        return cardCount;
    }


}
