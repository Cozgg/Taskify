/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository;

import com.ccq.pojo.Activity;
import com.ccq.pojo.Card;
import com.ccq.pojo.CardUser;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
public interface CardRepository {
    Card getById(int id);
    void addOrUpdate(Card c);
    void delete(int id);
    List<Card> getCard(Map<String, String> params);
    Card findCardById(int cardId);
    void assignUserForCard(CardUser ac);
    boolean isUserInCard(int userId, int cardId);
}
