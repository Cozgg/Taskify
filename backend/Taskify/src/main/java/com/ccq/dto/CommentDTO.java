/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.dto;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class CommentDTO {
    private int id;
    private String content;
    private Date createdDate;
    private int userId;
    private int cardId;

    public CommentDTO(int id, String content, Date createdDate, int userId, int cardId) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
        this.userId = userId;
        this.cardId = cardId;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the cardId
     */
    public int getCardId() {
        return cardId;
    }

    /**
     * @param cardId the cardId to set
     */
    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
    
    
    
}
