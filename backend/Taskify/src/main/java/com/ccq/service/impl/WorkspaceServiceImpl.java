/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccq.pojo.Board;
import com.ccq.pojo.User;
import com.ccq.pojo.Workspace;
import com.ccq.repository.BoardRepository;
import com.ccq.repository.UserRepository;
import com.ccq.repository.WorkspaceRepository;
import com.ccq.service.WorkspaceService;

/**
 *
 * @author quyendz
 */
@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private WorkspaceRepository workspaceRepo;

    @Autowired
    private BoardRepository boardRepo;

    @Autowired UserRepository userRepo;

    @Override
    public Workspace getWorkspaceById(int id) {
        return this.workspaceRepo.getWorkspaceById(id);
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
    public List<Workspace> getWorkspaces(Map<String, String> params) {
        return this.workspaceRepo.getWorkspaces(params);
    }

    @Override
    public List<Board> getBoardsByWorkspaceId(int wsId) {
        return this.workspaceRepo.getBoardsByWorkspaceId(wsId);
    }

    @Override
    public List<User> getMembersByWorkspaceId(int workspaceId) {
        return this.workspaceRepo.getMembersByWorkspaceId(workspaceId);
    }

    @Override
    public Long countUserInWorkspace(int workspaceId) {
        return this.userRepo.count();
    }

    @Override
    public Long countBoardInWorkspace(int workspaceId) {
        return this.boardRepo.count();
    }
}
