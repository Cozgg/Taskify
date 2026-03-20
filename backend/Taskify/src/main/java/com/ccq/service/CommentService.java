/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service;

import com.ccq.pojo.Comment;

/**
 *
 * @author Admin
 */
public interface CommentService {
    public Comment addComment(Comment c, int userId, int cardId);
}
