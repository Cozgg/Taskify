package com.ccq.pojo.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ResUserDTO {
    private Integer id;
    private String username;
    private String email;
    private String avatar;
    private String role;
    @JsonFormat(pattern="yyyy-MM-dd", timezone="Asia/Ho_Chi_Minh")
    private Date createdDate;

    public ResUserDTO() {
    }

    public ResUserDTO(Integer id, String username, String email, String avatar, String role, java.util.Date createdDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.avatar = avatar;
        this.role = role;
        this.createdDate = createdDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(java.util.Date createdDate) {
        this.createdDate = createdDate;
    }
}
