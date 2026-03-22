/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service.impl;

import com.ccq.pojo.List;
import com.ccq.repository.ListRepository;
import com.ccq.service.ListService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author nguye
 */
public class ListServiceImpl implements ListService{

    @Autowired
    private ListRepository listRepo;
    
    @Override
    public List getById(int id) {
        return this.listRepo.getById(id);
    }

    @Override
    public void addOrUpdate(List l) {
        this.listRepo.addOrUpdate(l);
    }

    @Override
    public void delete(int id) {
        this.listRepo.delete(id);
    }

    @Override
    public java.util.List<List> getList(Map<String, String> params) {
        return this.listRepo.getList(params);
    }
        
}
