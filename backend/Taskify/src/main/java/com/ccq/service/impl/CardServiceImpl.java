/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service.impl;

import com.ccq.pojo.Card;
import com.ccq.repository.CardRepository;
import com.ccq.service.CardService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Admin
 */
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepo;
    
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
    
}
