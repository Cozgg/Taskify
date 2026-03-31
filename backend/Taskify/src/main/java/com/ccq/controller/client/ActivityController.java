/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.controller.client;

import com.ccq.dto.ActivityDTO;
import com.ccq.pojo.Activity;
import com.ccq.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccq.service.ActivityService;
import com.ccq.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Admin
 */
@RestController
public class ActivityController {

    @Autowired
    private ActivityService actiSer;
    
    @Autowired
    private UserService userService;

    @PostMapping("/activity")
    public ResponseEntity assignUserToCard(@RequestParam("userId") int userId, @RequestParam("cardId") int cardId) {
        try {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentUser = this.userService.getUserByUsername(currentUsername); 
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không tìm thấy user!");
        }
            Activity c = this.actiSer.assignUserForCard(userId, cardId);
            ActivityDTO cdto = new ActivityDTO(c.getId(), 1, cardId, userId);
            return new ResponseEntity<>(cdto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi gán thành viên làm task " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
