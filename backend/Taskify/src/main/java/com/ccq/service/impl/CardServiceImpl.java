/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service.impl;

import com.ccq.pojo.Boardlist;
import com.ccq.pojo.Card;
import com.ccq.repository.CardRepository;
import com.ccq.repository.ListRepository;
import com.ccq.service.CardService;
import com.ccq.state.CardState;
import com.ccq.state.DoneState;
import com.ccq.state.InProgressState;
import com.ccq.state.ToDoState;
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
        Boardlist list = this.listRepo.getById(listId);
        if (list == null) {
            throw new RuntimeException("Không tìm thấy cột");
        }
        c.setListId(list);
        this.cardRepo.addOrUpdate(c);
    }

    @Override
    public String moveCard(int cardId, int newListId, int newPosition) {
        Card card = this.cardRepo.getById(cardId);
        if (card == null) {
            throw new RuntimeException("Không tìm thấy thẻ cần di chuyển!");
        }
        String msg = "Đang di chuyển thẻ";
        if (card.getListId() == null || card.getListId().getId() != newListId) {
            Boardlist newList = this.listRepo.getById(newListId);
            if (newList == null) {
                throw new RuntimeException("Không tìm thấy Cột đích!");
            }

            CardState newState = switch (newList.getStatus().toString()) {
                case "IN_PROGRESS" ->
                    new InProgressState();
                case "DONE" ->
                    new DoneState();
                default ->
                    new ToDoState();
            };
                    
            msg = card.changeState(newState, newList);
        }

        card.setPosition(newPosition);

        this.cardRepo.addOrUpdate(card);
        return msg;
    }
}
