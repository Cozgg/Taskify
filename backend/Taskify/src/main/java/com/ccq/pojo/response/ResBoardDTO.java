package com.ccq.pojo.response;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ResBoardDTO {
    private Integer id;
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd", timezone="Asia/Ho_Chi_Minh")
    private Date createdDate;
    private Boolean isPublic;
    private List<ResListDTO> lists;

    public ResBoardDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
    public List<ResListDTO> getLists() { return lists; }
    public void setLists(List<ResListDTO> lists) { this.lists = lists; }
}
