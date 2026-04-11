package com.ccq.utils;

import com.ccq.repository.BoardRepository;
import com.ccq.repository.CardRepository;
import com.ccq.repository.CommentRepository;
import com.ccq.repository.ListRepository;
import com.ccq.repository.UserRepository;
import com.ccq.repository.UserWorkspaceRepository;
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
    private BoardRepository boardRepo;

    @Autowired
    private ListRepository listRepo;

    @Autowired
    private CardRepository cardRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserWorkspaceRepository userWorkspaceRepo;

//    public boolean isCommentOwner(int commentId, String username) {
//        return this.commRepo.isCommentOwner(commentId, username);
//    }

    public boolean canAccessBoard(String username, int boardId) {
        Integer wsId = resolveWorkspaceId("BOARD", boardId);
        return isMemberOfWorkspace(username, wsId);
    }

    public boolean canAccessList(String username, int listId) {
        Integer wsId = resolveWorkspaceId("LIST", listId);
        return isMemberOfWorkspace(username, wsId);
    }

    public boolean canAccessCard(String username, int cardId) {
        Integer wsId = resolveWorkspaceId("CARD", cardId);
        return isMemberOfWorkspace(username, wsId);
    }

    private Integer resolveWorkspaceId(String type, int id) {
        switch (type) {
            case "BOARD":
                return boardRepo.getById(id).getWorkspaceId().getId();
            case "LIST":
                return listRepo.getById(id).getBoardId().getWorkspaceId().getId();
            case "CARD":
                return cardRepo.findCardById(id).getBoardList().getBoardId().getWorkspaceId().getId();
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    private boolean isMemberOfWorkspace(String username, Integer workspaceId) {
        if (workspaceId == null) {
            return false;
        }
        var user = userRepo.getUserByUsername(username);
        if (user == null) {
            return false;
        }
        return userWorkspaceRepo.isUserInWorkspace(user.getId(), workspaceId);
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
