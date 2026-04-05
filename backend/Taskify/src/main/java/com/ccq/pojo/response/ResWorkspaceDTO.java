package com.ccq.pojo.response;

public class ResWorkspaceDTO {
    private Integer id;
    private String name;
    private ResUserDTO owner;

    public ResWorkspaceDTO() {
    }

    public ResWorkspaceDTO(Integer id, String name, ResUserDTO owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
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
}
