/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.controller.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccq.service.ActivityService;

/**
 *
 * @author Admin
 */
@RestController
public class ActivityController {
    @Autowired
    private ActivityService actiSer;
    
    @PostMapping("/activity")
    public ResponseEntity assignUserToCard(@RequestParam("userId") int userId, @RequestParam("cardId") int cardId){
        this.actiSer.assignUserForCard(userId, cardId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
