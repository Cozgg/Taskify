/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository;

import com.ccq.pojo.Workspace;

/**
 *
 * @author Admin
 */
public interface WorkspaceRepository {
    Workspace findWorkspaceById(int workspaceId);
}
