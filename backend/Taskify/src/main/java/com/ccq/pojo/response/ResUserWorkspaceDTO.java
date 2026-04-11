/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.pojo.response;

/**
 *
 * @author Admin
 */
public class ResUserWorkspaceDTO {
    private int userId;
    private int workspaceId;

    public ResUserWorkspaceDTO(int userId, int workspaceId) {
        this.userId = userId;
        this.workspaceId = workspaceId;
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
     * @return the workspaceId
     */
    public int getWorkspaceId() {
        return workspaceId;
    }

    /**
     * @param workspaceId the workspaceId to set
     */
    public void setWorkspaceId(int workspaceId) {
        this.workspaceId = workspaceId;
    }
    
    
    
}
