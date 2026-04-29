package com.ccq.pojo.message;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationMessage implements Serializable {

    private String eventId = UUID.randomUUID().toString();
    private String type;
    private List<String> recipientEmails = new ArrayList<>();
    private String subject;
    private String body;
    private Integer cardId;
    private Integer workspaceId;
    private String actorName;
    private String createdAt;

    public NotificationMessage() {
    }

    public NotificationMessage(String type, List<String> recipientEmails, String subject, String body,
            Integer cardId, Integer workspaceId, String actorName) {
        this.type = type;
        this.recipientEmails = recipientEmails;
        this.subject = subject;
        this.body = body;
        this.cardId = cardId;
        this.workspaceId = workspaceId;
        this.actorName = actorName;
        this.createdAt = LocalDateTime.now().toString();
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getRecipientEmails() {
        return recipientEmails;
    }

    public void setRecipientEmails(List<String> recipientEmails) {
        this.recipientEmails = recipientEmails;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Integer workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getBodyWithAuditInfo() {
        StringBuilder content = new StringBuilder();
        if (this.body != null) {
            content.append(this.body);
        }
        content.append("\n\n---\n");
        content.append("Thời gian: ").append(this.createdAt != null ? this.createdAt : "N/A").append("\n");
        content.append("Người thực hiện: ")
                .append(this.actorName != null && !this.actorName.isBlank() ? this.actorName : "System");
        return content.toString();
    }
}
