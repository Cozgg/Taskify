/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository;

import java.util.List;
import java.util.Map;

import com.ccq.pojo.Board;
import com.ccq.pojo.User;
import com.ccq.pojo.Workspace;

/**
 *
 * @author nguye
 */
public interface WorkspaceRepository {

    void addOrUpdate(Workspace w);

    void delete(int id);

    List<Workspace> getWorkspaces(Map<String, String> params);

    Workspace getWorkspaceById(int workspaceId);

    Workspace getWorkspaceByOwnerId(int ownerId);

    List<User> getMembersByWorkspaceId(int workspaceId);

    List<Board> getBoardsByWorkspaceId(int wsId);

    Long count();
    
    boolean existsByUsernameAndWorkspaceIdAndRole(String username, int workspaceId);
    
    boolean existsByUsernameAndWorkspaceId(String username, int workspaceId);
}
