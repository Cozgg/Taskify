/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository;

import com.ccq.pojo.UserWorkspace;

/**
 *
 * @author Admin
 */
public interface UserWorkspaceRepository {
    void saveInviteUser(UserWorkspace uw);
    boolean isUserInWorkspace(int userId, int workspaceId);
}
