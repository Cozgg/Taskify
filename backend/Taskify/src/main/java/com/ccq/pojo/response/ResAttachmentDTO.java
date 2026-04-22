package com.ccq.pojo.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ResAttachmentDTO {
    private Integer id;
    private String filename;
    private String url;
    @JsonFormat(pattern="yyyy-MM-dd", timezone="Asia/Ho_Chi_Minh")
    private Date updateDate;

    public ResAttachmentDTO() {}

    public ResAttachmentDTO(Integer id, String filename, String url, Date updateDate) {
        this.id = id;
        this.filename = filename;
        this.url = url;
        this.updateDate = updateDate;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
}
