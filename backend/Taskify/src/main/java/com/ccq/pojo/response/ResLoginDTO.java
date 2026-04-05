package com.ccq.pojo.response;

public class ResLoginDTO {
    private String token;
    private String refreshToken;
    private Integer userId;
    private String username;

    public ResLoginDTO() {}

    public ResLoginDTO(String token, Integer userId, String username) {
        this.token = token;
        this.userId = userId;
        this.username = username;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}

