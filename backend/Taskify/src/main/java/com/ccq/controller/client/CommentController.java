/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.controller.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccq.pojo.Comment;
import com.ccq.pojo.User;
import com.ccq.pojo.response.ResCommentDTO;
import com.ccq.service.CommentService;
import com.ccq.service.UserService;
import com.ccq.utils.DTOMapper;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class CommentController {

    @Autowired
    private CommentService commSer;

    @Autowired
    private UserService userSer;
    
    @GetMapping("/cards/{cardId}/comments")
    public ResponseEntity<List<ResCommentDTO>> getComments(@PathVariable("cardId") int cardId){
        return new ResponseEntity<>(this.commSer.getComments(cardId), HttpStatus.OK);
    }

    @PostMapping("/cards/{cardId}/comments")
    public ResponseEntity<?> addComment(@PathVariable("cardId") int cardId, @RequestBody Map<String, String> params) {
        try {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentUser = this.userSer.getUserByUsername(currentUsername);
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tìm thấy user!");
            }
            Comment c = new Comment(params.get("content"));
            Comment saveComment = this.commSer.addComment(c, currentUser.getId(), cardId);
            ResCommentDTO cdto = DTOMapper.toCommentDTO(saveComment);
            return new ResponseEntity<>(cdto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi thêm comment " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") int commentId){
        this.commSer.deleteComment(commentId);
        return new ResponseEntity<>("Xóa thành công", HttpStatus.NO_CONTENT);
    }

}
