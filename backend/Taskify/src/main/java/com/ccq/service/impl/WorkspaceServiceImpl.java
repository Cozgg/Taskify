/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service.impl;

import com.ccq.pojo.Workspace;
import com.ccq.repository.WorkspaceRepository;
import com.ccq.service.WorkspaceService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author nguye
 */
@Service
public class WorkspaceServiceImpl implements WorkspaceService{

    @Autowired
    private WorkspaceRepository workspaceRepo;
    
    @Override
    public Workspace getById(int id) {
        return this.workspaceRepo.getById(id);
    }

    @Override
    public void addOrUpdate(Workspace w) {
        this.workspaceRepo.addOrUpdate(w);
    }

    @Override
    public void delete(int id) {
        this.workspaceRepo.delete(id);
    }

    @Override
    public List<Workspace> getWorkSpace(Map<String, String> params) {
        return this.workspaceRepo.getWorkSpace(params);
    }
    
}
