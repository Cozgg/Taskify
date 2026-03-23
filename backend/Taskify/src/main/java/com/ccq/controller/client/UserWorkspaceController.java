/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.controller.client;

import com.ccq.service.UserWorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
public class UserWorkspaceController {
    @Autowired
    private UserWorkspaceService userWorkSer;
    
    
    @PostMapping("/user-workspaces")
    public ResponseEntity inviteUser(@RequestParam("userId") int userId, @RequestParam("workspaceId") int workspaceId){
        this.userWorkSer.inviteUser(userId, workspaceId);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
