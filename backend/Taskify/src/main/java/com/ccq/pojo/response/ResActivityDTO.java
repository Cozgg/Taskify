package com.ccq.pojo.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ResActivityDTO {
    private Integer id;
    private Integer activity;
    @JsonFormat(pattern="yyyy-MM-dd", timezone="Asia/Ho_Chi_Minh")
    private Date createdDate;
    private ResUserDTO user;

    public ResActivityDTO() {}

    public ResActivityDTO(Integer id, Integer activity, Date createdDate, ResUserDTO user) {
        this.id = id;
        this.activity = activity;
        this.createdDate = createdDate;
        this.user = user;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getActivity() { return activity; }
    public void setActivity(Integer activity) { this.activity = activity; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public ResUserDTO getUser() { return user; }
    public void setUser(ResUserDTO user) { this.user = user; }
}
