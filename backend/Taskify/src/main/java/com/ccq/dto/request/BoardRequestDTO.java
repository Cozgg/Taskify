/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.dto.request;

/**
 *
 * @author Admin
 */
public class BoardRequestDTO {
    private String name;
    private Boolean isPublic;
    

    public BoardRequestDTO() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
}
