/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service.impl;

import com.ccq.pojo.Activity;
import com.ccq.pojo.Card;
import com.ccq.pojo.User;
import com.ccq.repository.ActivityRepository;
import com.ccq.repository.CardRepository;
import com.ccq.repository.UserRepository;
import com.ccq.service.ActivityService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author Admin
 */
@Service
public class ActivityServiceImpl implements ActivityService{
    @Autowired
    private ActivityRepository actiRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private CardRepository cardRepo;
    
    @Override
    public Activity assignUserForCard(int userId, int cardId) {
        User u = this.userRepo.findUserById(userId);
        if(u == null){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "User not found");
        }
        Card c = this.cardRepo.findCardById(cardId);
        if(c == null){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Card not found");
        }
        
        boolean isUserValid = this.actiRepo.isUserInCard(userId, cardId);
        if(isUserValid){
            throw  new ResponseStatusException(HttpStatusCode.valueOf(409), "User đã tồn tại trong workspace");
        }
        Activity ac = new Activity();
        ac.setCardId(c);
        ac.setUserId(u);
        ac.setActivity(1);
        ac.setCreatedDate(new Date());
        this.actiRepo.assignUserForCard(ac);
        return ac;
    }
    
}
