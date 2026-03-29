package com.ccq.pojo.response;

public class ResChecklistItemDTO {
    private Integer id;
    private String name;
    private Boolean isChecked;
    private Integer position;

    public ResChecklistItemDTO() {}

    public ResChecklistItemDTO(Integer id, String name, Boolean isChecked, Integer position) {
        this.id = id;
        this.name = name;
        this.isChecked = isChecked;
        this.position = position;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Boolean getIsChecked() { return isChecked; }
    public void setIsChecked(Boolean isChecked) { this.isChecked = isChecked; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}
