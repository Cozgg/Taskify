package com.ccq.pojo.response;

public class ResRegisterDTO {
    private Integer userId;
    private String username;
    private String role;

    public ResRegisterDTO() {
    }

    public ResRegisterDTO(Integer userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
