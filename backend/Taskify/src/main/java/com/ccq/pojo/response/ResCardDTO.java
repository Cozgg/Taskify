package com.ccq.pojo.response;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ResCardDTO {
    private Integer id;
    private String name;
    private String description;
    @JsonFormat(pattern="yyyy-MM-dd", timezone="Asia/Ho_Chi_Minh")
    private Date createdDate;
    private Boolean isActive;
    @JsonFormat(pattern="yyyy-MM-dd", timezone="Asia/Ho_Chi_Minh")
    private Date dueDate;
    @JsonFormat(pattern="yyyy-MM-dd", timezone="Asia/Ho_Chi_Minh")
    private Date reminderDate;
    private Integer position;
    
    private List<ResChecklistItemDTO> checklists;
    private List<ResActivityDTO> activities;
    private List<ResAttachmentDTO> attachments;
    private List<ResCommentDTO> comments;
    private List<ResUserDTO> assignees;

    public ResCardDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    public Date getReminderDate() { return reminderDate; }
    public void setReminderDate(Date reminderDate) { this.reminderDate = reminderDate; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }

    public List<ResChecklistItemDTO> getChecklists() { return checklists; }
    public void setChecklists(List<ResChecklistItemDTO> checklists) { this.checklists = checklists; }
    public List<ResActivityDTO> getActivities() { return activities; }
    public void setActivities(List<ResActivityDTO> activities) { this.activities = activities; }
    public List<ResAttachmentDTO> getAttachments() { return attachments; }
    public void setAttachments(List<ResAttachmentDTO> attachments) { this.attachments = attachments; }
    public List<ResCommentDTO> getComments() { return comments; }
    public void setComments(List<ResCommentDTO> comments) { this.comments = comments; }
    public List<ResUserDTO> getAssignees() { return assignees; }
    public void setAssignees(List<ResUserDTO> assignees) { this.assignees = assignees; }
}
