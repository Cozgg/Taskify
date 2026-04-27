/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository;

import com.ccq.pojo.Comment;
import com.ccq.pojo.response.ResCommentDTO;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface CommentRepository {

    void addComment(Comment c);

    void deleteComment(int id);

    Comment getCommentById(int id);
    
    List<ResCommentDTO> getComments(int cardId);

    // boolean isCommentOwner(int commentId, String username);

    // boolean isWorkspaceAdminOfThisComment(int commentId, String username);
}
