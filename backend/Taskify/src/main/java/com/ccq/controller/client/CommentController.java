/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.controller.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ccq.pojo.Comment;
import com.ccq.service.CommentService;

/**
 *
 * @author Admin
 */
@RestController
public class CommentController {
    @Autowired
    private CommentService commSer;
    
    @PostMapping("/cards/{cardId}/comments")
    public ResponseEntity addComment(@PathVariable("cardId") int cardId,@RequestBody Comment c){
        int userId = c.getUserId().getId();
        Comment saveComment = this.commSer.addComment(c, userId, cardId);
        return new ResponseEntity(saveComment, HttpStatus.CREATED);
    }
    
}
