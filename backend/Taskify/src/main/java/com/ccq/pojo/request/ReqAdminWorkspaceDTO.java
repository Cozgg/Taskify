package com.ccq.pojo.request;

public class ReqAdminWorkspaceDTO {

    private String name;

    private Integer ownerId;

    public ReqAdminWorkspaceDTO() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getOwnerId() { return ownerId; }
    public void setOwnerId(Integer ownerId) { this.ownerId = ownerId; }
}
