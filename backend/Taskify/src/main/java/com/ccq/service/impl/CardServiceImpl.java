/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service.impl;

import com.ccq.pojo.Card;
import com.ccq.repository.CardRepository;
import com.ccq.repository.ListRepository;
import com.ccq.service.CardService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepo;
    @Autowired
    private ListRepository listRepo;
    
    @Override
    public Card getById(int id) {
        return this.cardRepo.getById(id);
    }

    @Override
    public void addOrUpdate(Card c) {
        this.cardRepo.addOrUpdate(c);
    }

    @Override
    public void delete(int id) {
        this.cardRepo.delete(id);
    }

    @Override
    public List<Card> getCard(Map<String, String> params) {
        return this.cardRepo.getCard(params);
    }

    @Override
    public void createCardInList(int listId, Card c) {
        com.ccq.pojo.List list = this.listRepo.getById(listId);
        if(list == null){
            throw new RuntimeException("Không tìm thấy cột");
        }
        c.setListId(list);
        this.cardRepo.addOrUpdate(c);
    }
    
    @Override
    public void moveCard(int cardId, int newListId, int newPosition) {
        Card card = this.cardRepo.getById(cardId);
        if (card == null) {
            throw new RuntimeException("Không tìm thấy thẻ cần di chuyển!");
        }

        if (card.getListId() == null || card.getListId().getId() != newListId) {
            com.ccq.pojo.List newList = this.listRepo.getById(newListId);
            if (newList == null) {
                throw new RuntimeException("Không tìm thấy Cột đích!");
            }
            card.setListId(newList);
        }
        card.setPosition(newPosition);

        this.cardRepo.addOrUpdate(card);
    }
}
    