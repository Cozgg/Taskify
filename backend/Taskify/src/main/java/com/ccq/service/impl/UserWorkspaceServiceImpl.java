/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service.impl;

import com.ccq.pojo.User;
import com.ccq.pojo.UserWorkspace;
import com.ccq.pojo.Workspace;
import com.ccq.repository.UserRepository;
import com.ccq.repository.UserWorkspaceRepository;
import com.ccq.repository.WorkspaceRepository;
import com.ccq.service.UserWorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author Admin
 */
@Service
public class UserWorkspaceServiceImpl implements UserWorkspaceService{

    @Autowired
    private UserWorkspaceRepository userWorkRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired 
    private WorkspaceRepository workspaceRepo;
    
    @Override
    public void inviteUser(int userId, int workspaceId) {
        User u = this.userRepo.findUserById(userId);
        if(u == null){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "User not found");
        }
        Workspace w = this.workspaceRepo.getWorkspaceById(workspaceId);
        if(w == null){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Workspace not found");
        }
        
        boolean isUserValid = this.userWorkRepo.isUserInWorkspace(userId, workspaceId);
        if(!isUserValid){
            throw  new ResponseStatusException(HttpStatusCode.valueOf(409), "User đã tồn tại trong workspace");
        }
        UserWorkspace uw = new UserWorkspace();
        uw.setUserId(u);
        uw.setWorkspaceId(w);
        this.userWorkRepo.saveInviteUser(uw);
    }
    
}
