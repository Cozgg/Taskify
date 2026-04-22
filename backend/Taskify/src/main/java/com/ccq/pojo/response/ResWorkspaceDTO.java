package com.ccq.pojo.response;

public class ResWorkspaceDTO {
    private Integer id;
    private String name;
    private ResUserDTO owner;
    private Integer boardCount;
    private Integer memberCount;

    public ResWorkspaceDTO() {
    }

    public ResWorkspaceDTO(Integer id, String name, ResUserDTO owner, Integer boardCount, Integer memberCount) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.boardCount = boardCount;
        this.memberCount = memberCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResUserDTO getOwner() {
        return owner;
    }

    public void setOwner(ResUserDTO owner) {
        this.owner = owner;
    }

    public Integer getBoardCount() {
        return boardCount;
    }

    public void setBoardCount(Integer boardCount) {
        this.boardCount = boardCount;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }
}
