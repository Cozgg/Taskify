/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository.impl;

import com.ccq.pojo.Card;
import com.ccq.repository.CardRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Admin
 */
@Repository
@Transactional
public class CardRepositoryImpl implements CardRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Card findCardById(int cardId) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Card.class, cardId);
    }

    @Override
    public void updateCardLabel(int cardId, int labelId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}
