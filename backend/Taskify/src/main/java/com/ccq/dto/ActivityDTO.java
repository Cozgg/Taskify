/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.dto;

/**
 *
 * @author Admin
 */
public class ActivityDTO {
    private Integer id;
    private Integer activity;
    private Integer cardId;
    private Integer userId;

    public ActivityDTO() {
    }

    public ActivityDTO(Integer id, Integer activity, Integer cardId, Integer userId) {
        this.id = id;
        this.activity = activity;
        this.cardId = cardId;
        this.userId = userId;
    }
    
    
    
    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the activity
     */
    public Integer getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(Integer activity) {
        this.activity = activity;
    }

    /**
     * @return the cardId
     */
    public Integer getCardId() {
        return cardId;
    }

    /**
     * @param cardId the cardId to set
     */
    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    
}
