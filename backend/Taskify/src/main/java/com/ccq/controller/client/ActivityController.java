/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.controller.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccq.pojo.Activity;
import com.ccq.pojo.User;
import com.ccq.pojo.response.ResActivityDTO;
import com.ccq.service.ActivityService;
import com.ccq.service.UserService;
import com.ccq.utils.DTOMapper;

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
    public ResponseEntity<?> assignUserToCard(@RequestParam("userId") int userId, @RequestParam("cardId") int cardId) {
        try {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentUser = this.userService.getUserByUsername(currentUsername);
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không tìm thấy user!");
            }
            Activity ac = this.actiSer.assignUserForCard(userId, cardId);
            ResActivityDTO cdto = DTOMapper.toActivityDTO(ac);
            return new ResponseEntity<>(cdto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi gán thành viên làm task " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
