/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.controller.client;

import com.ccq.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Admin
 */
@Component("workspaceAuth")
public class WorkspaceAuth {
    @Autowired
    private WorkspaceRepository memberRepo;

    // Check quyền Owner (Người quản lý/Sở hữu)
    public boolean isOwner(String username, int workspaceId) {
        // Viết logic DB: Trả về true nếu username này có role 'OWNER' tại workspaceId
        return memberRepo.existsByUsernameAndWorkspaceIdAndRole(username, workspaceId);
    }

    // Check quyền Member (Thành viên được phép thao tác cơ bản)
    public boolean isMemberOrOwner(String username, int workspaceId) {
        // Viết logic DB: Trả về true nếu username này tồn tại trong workspaceId
        return memberRepo.existsByUsernameAndWorkspaceId(username, workspaceId);
    }
}
