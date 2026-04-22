package com.ccq.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ccq.service.PermissionService;

@Component("securityCustom")
public class SecurityCustom {

    @Autowired
    private PermissionService permissionService;

    public boolean canAccessBoard(String username, int boardId) {
        try {
            permissionService.requireBoardAccess(boardId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean canAccessWorkspace(String username, int workspaceId) {
        try {
            permissionService.requireWorkspaceAccess(workspaceId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean canAccessList(String username, int listId) {
        try {
            permissionService.requireListAccess(listId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean canAccessCard(String username, int cardId) {
        try {
            permissionService.requireCardAccess(cardId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
