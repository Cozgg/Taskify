/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service;

import com.ccq.pojo.Comment;
import com.ccq.pojo.response.ResCommentDTO;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface CommentService {

    Comment addComment(Comment c, int userId, int cardId);

    void deleteComment(int id);
    
    List<ResCommentDTO> getComments(int cardId);
}
