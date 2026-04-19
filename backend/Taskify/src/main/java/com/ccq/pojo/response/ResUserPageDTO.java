package com.ccq.pojo.response;

import java.util.List;

public class ResUserPageDTO {

    private List<ResUserDTO> items;
    private Long totalItems;
    private Integer page;
    private Integer pageSize;

    public ResUserPageDTO() {
    }

    public ResUserPageDTO(List<ResUserDTO> items, Long totalItems, Integer page, Integer pageSize) {
        this.items = items;
        this.totalItems = totalItems;
        this.page = page;
        this.pageSize = pageSize;
    }

    public List<ResUserDTO> getItems() {
        return items;
    }

    public void setItems(List<ResUserDTO> items) {
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
