/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service;

import com.ccq.pojo.Card;
import com.ccq.pojo.CardUser;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
public interface CardService {
    Card getById(int id);
    void addOrUpdate(Card c);
    void delete(int id);
    List<Card> getCard(Map<String, String> params);
    
    void createCardInList(int listId, Card c);
    public String moveCard(int cardId, int newListId, int newPosition);
    CardUser assignUserForCard(int userId, int cardId);
}
