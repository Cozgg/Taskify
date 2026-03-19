/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service.impl;

import com.ccq.pojo.Card;
import com.ccq.pojo.Comment;
import com.ccq.pojo.User;
import com.ccq.repository.CardRepository;
import com.ccq.repository.CommentRepository;
import com.ccq.repository.UserRepository;
import com.ccq.service.CommentService;
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
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentRepository commRepo;
    
    @Autowired 
    private UserRepository userRepo;
    
    @Autowired
    private CardRepository cardRepo;
    
    @Override
    public Comment addComment(Comment c, int userId, int cardId) {
        User u = this.userRepo.findUserById(userId);
        if(u == null){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "User not found");
        }
        Card ca = this.cardRepo.findCardById(cardId);
        if(ca == null){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Card not found");
        }
        c.setUserId(u);
        c.setCardId(ca);
        c.setCreatedDate(new Date());
        
        this.commRepo.addComment(c);
        return c;
    }
    
}
