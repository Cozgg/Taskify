/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository;
import com.ccq.pojo.Workspace;
import java.util.List;
import java.util.Map;


/**
 *
 * @author nguye
 */

public interface WorkspaceRepository {
    void addOrUpdate(Workspace w);
    void delete(int id);
    List<Workspace> getWorkSpace(Map<String, String> params);
    Workspace getWorkspaceById(int workspaceId);
}

