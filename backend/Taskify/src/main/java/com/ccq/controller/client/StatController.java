/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.controller.client;

import com.ccq.service.StatService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/api")
public class StatController {
    
    @Autowired
    private StatService statService;
    
    @GetMapping("/stat-board-progress/{boardId}")
    public ResponseEntity<List<Object[]>> statBoardProgress(@ModelAttribute(value = "boardId") int boardId){
        return new ResponseEntity<>(this.statService.getBoardProgressStats(boardId), HttpStatus.OK);
    }
    
    @GetMapping("/stat-member-progress/{boardId}")
    public ResponseEntity<List<Object[]>> statMemberProgress(@ModelAttribute(value = "boardId") int boardId){
        return new ResponseEntity<>(this.statService.getMemberProgress(boardId), HttpStatus.OK);
    }
    
}
