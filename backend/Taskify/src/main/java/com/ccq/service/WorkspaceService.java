/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service;

import java.util.List;
import java.util.Map;

import com.ccq.pojo.Board;
import com.ccq.pojo.User;
import com.ccq.pojo.Workspace;

/**
 *
 * @author nguye
 */
public interface WorkspaceService {

    void addOrUpdate(Workspace w);

    void delete(int id);

    Workspace getWorkspaceById(int workspaceId);

    List<Workspace> getWorkspaces(Map<String, String> params);

    List<Board> getBoardsByWorkspaceId(int wsId);

    List<User> getMembersByWorkspaceId(int workspaceId);

    Long countUserInWorkspace(int workspaceId);

    Long countBoardInWorkspace(int workspaceId);
}
