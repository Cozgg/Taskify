package com.ccq.pojo.response;

import java.util.List;

public class ResWorkspacePageDTO {

    private List<ResWorkspaceDTO> items;
    private Long totalItems;
    private Integer page;
    private Integer pageSize;

    public ResWorkspacePageDTO() {
    }

    public ResWorkspacePageDTO(List<ResWorkspaceDTO> items, Long totalItems, Integer page, Integer pageSize) {
        this.items = items;
        this.totalItems = totalItems;
        this.page = page;
        this.pageSize = pageSize;
    }

    public List<ResWorkspaceDTO> getItems() {
        return items;
    }

    public void setItems(List<ResWorkspaceDTO> items) {
        this.items = items;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
