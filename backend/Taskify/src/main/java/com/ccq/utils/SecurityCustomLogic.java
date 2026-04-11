/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.utils;

import com.ccq.repository.CardRepository;
import com.ccq.repository.CommentRepository;
import com.ccq.repository.WorkspaceRepository;
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
    
    @Autowired
    private CardRepository cardRepo;
    
    @Autowired
    private WorkspaceRepository workspaceRepo;
    
    public boolean isCommentOwner(int commentId, String username){
        return this.commRepo.isCommentOwner(commentId, username);
    }
    
    public boolean isWorkspaceAdminOfThisComment(int commentId, String username){
        return this.commRepo.isWorkspaceAdminOfThisComment(commentId, username);
    }
    
    public boolean isWorkspaceAdminOfThisCard(int cardId, String username){
        return this.cardRepo.isWorkspaceAdminOfThisCard(cardId, username);
    }
    
    public boolean isAdminOfThisWorkspace(int workspaceId, String username){
        return this.workspaceRepo.isAdminOfThisWorkspace(workspaceId, username);
    }
}
