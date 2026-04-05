/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.utils;

import com.ccq.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Admin
 */
@Component("securityCustom")
public class SecurityCustomLogic {
    @Autowired
    private CommentRepository commRepo;
    
    public boolean isCommentOwner(int commentId, String username){
        return this.commRepo.isCommentOwner(commentId, username);
    }
}
