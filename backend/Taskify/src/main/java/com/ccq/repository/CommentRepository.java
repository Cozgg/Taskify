/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository;

import com.ccq.pojo.Comment;

/**
 *
 * @author Admin
 */
public interface CommentRepository {
    void addComment(Comment c);
    void deleteComment(int id);
    Comment getCommentById(int id);
    boolean isCommentOwner(int commentId, String username);
}
