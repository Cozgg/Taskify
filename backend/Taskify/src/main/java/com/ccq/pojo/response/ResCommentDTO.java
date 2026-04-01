package com.ccq.pojo.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ResCommentDTO {
    private Integer id;
    private String comment;
    @JsonFormat(pattern="yyyy-MM-dd", timezone="Asia/Ho_Chi_Minh")
    private Date createdDate;
    private ResUserDTO user;

    public ResCommentDTO() {}

    public ResCommentDTO(Integer id, String comment, Date createdDate, ResUserDTO user) {
        this.id = id;
        this.comment = comment;
        this.createdDate = createdDate;
        this.user = user;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public ResUserDTO getUser() { return user; }
    public void setUser(ResUserDTO user) { this.user = user; }
}
