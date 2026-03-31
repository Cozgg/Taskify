/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class BoardDTO {
    private int id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createDate;
    private boolean isPublic;
    private int workspaceId;

    public BoardDTO(int id, String name, Date createDate, boolean isPublic) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.isPublic = isPublic;
    }

    public BoardDTO(int id, String name, Date createDate, boolean isPublic, int workspaceId) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.isPublic = isPublic;
        this.workspaceId = workspaceId;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the isPublic
     */
    public boolean isIsPublic() {
        return isPublic;
    }

    /**
     * @param isPublic the isPublic to set
     */
    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
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
