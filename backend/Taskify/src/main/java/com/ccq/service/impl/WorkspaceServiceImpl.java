package com.ccq.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccq.pojo.Board;
import com.ccq.pojo.User;
import com.ccq.pojo.UserWorkspace;
import com.ccq.pojo.Workspace;
import com.ccq.repository.UserRepository;
import com.ccq.repository.WorkspaceRepository;
import com.ccq.service.PermissionService;
import com.ccq.service.WorkspaceService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private WorkspaceRepository workspaceRepo;
    
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PermissionService permissionService;

    @Override
    public Workspace getWorkspaceById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Workspace ID phải là số dương, nhận được: " + id);
        }
        Workspace workspace = this.workspaceRepo.getWorkspaceById(id);
        if (workspace != null) {
            permissionService.requireWorkspaceAccess(id);
        }
        return workspace;
    }

    @Override
    @Transactional
    public Workspace getWorkspaceByOwnerId(int ownerId) {
        if (ownerId <= 0) {
            throw new IllegalArgumentException("Owner ID phải là số dương");
        }
        permissionService.requireUserSelfOrAdmin(ownerId);
        return this.workspaceRepo.getWorkspaceByOwnerId(ownerId);
    }

    @Override
    public boolean hasWorkspace(int ownerId) {
        if (ownerId <= 0) {
            throw new IllegalArgumentException("Owner ID phải là số dương");
        }
        return this.workspaceRepo.getWorkspaceByOwnerId(ownerId) != null;
    }

    @Override
    public void addOrUpdate(Workspace w) {
        if (w == null) {
            throw new IllegalArgumentException("Workspace không được null");
        }
        if (w.getName() == null || w.getName().isBlank()) {
            throw new IllegalArgumentException("Tên workspace không được để trống");
        }
        if (w.getName().length() > 100) {
            throw new IllegalArgumentException("Tên workspace không được vượt quá 100 ký tự");
        }
        if (w.getOwnerId() == null) {
            throw new IllegalArgumentException("Workspace phải có owner");
        }
        if (w.getId() != null) {
            Workspace existing = this.workspaceRepo.getWorkspaceById(w.getId());
            if (existing == null) {
                throw new IllegalArgumentException("Không tìm thấy workspace với ID: " + w.getId());
            }
            // Chỉ ADMIN hoặc owner workspace mới được chỉnh sửa
            permissionService.requireWorkspaceOwnerPermission(w.getId());
        }
        this.workspaceRepo.addOrUpdate(w);
    }

    @Override
    public void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Workspace ID phải là số dương, nhận được: " + id);
        }
        Workspace existing = this.workspaceRepo.getWorkspaceById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Không tìm thấy workspace với ID: " + id);
        }
        
        // Chỉ ADMIN hoặc owner workspace mới được xóa
        permissionService.requireWorkspaceOwnerPermission(id);

        this.workspaceRepo.delete(id);
    }

    @Override
    public List<Workspace> getWorkspaces(Map<String, String> params) {
        return this.workspaceRepo.getWorkspaces(params);
    }

    @Override
    public List<Board> getBoardsByWorkspaceId(int wsId) {
        if (wsId <= 0) {
            throw new IllegalArgumentException("Workspace ID phải là số dương, nhận được: " + wsId);
        }
        permissionService.requireWorkspaceAccess(wsId);
        return this.workspaceRepo.getBoardsByWorkspaceId(wsId);
    }

    @Override
    public List<User> getMembersByWorkspaceId(int workspaceId) {
        if (workspaceId <= 0) {
            throw new IllegalArgumentException("Workspace ID phải là số dương, nhận được: " + workspaceId);
        }
        permissionService.requireWorkspaceAccess(workspaceId);
        return this.workspaceRepo.getMembersByWorkspaceId(workspaceId);
    }

    @Override
    public Long countUserInWorkspace(int workspaceId) {
        if (workspaceId <= 0) {
            throw new IllegalArgumentException("Workspace ID phải là số dương, nhận được: " + workspaceId);
        }
        permissionService.requireWorkspaceAccess(workspaceId);
        return this.workspaceRepo.countMembersByWorkspaceId(workspaceId);
    }

    @Override
    public Long countBoardInWorkspace(int workspaceId) {
        if (workspaceId <= 0) {
            throw new IllegalArgumentException("Workspace ID phải là số dương, nhận được: " + workspaceId);
        }
        permissionService.requireWorkspaceAccess(workspaceId);
        return (long) this.workspaceRepo.getBoardsByWorkspaceId(workspaceId).size();
    }

    @Override
    public UserWorkspace addUserIntoWorkspace(int workspaceId, int userId) {
        if (workspaceId <= 0 || userId <= 0) {
            throw new IllegalArgumentException("workspaceId vA userId phai la so duong");
        }
        permissionService.requireWorkspaceOwnerPermission(workspaceId);

        UserWorkspace uw = new UserWorkspace();
        Workspace w = workspaceRepo.getWorkspaceById(workspaceId);
        User u = userRepo.findUserById(userId);

        if (w == null) {
            throw new IllegalArgumentException("Khong tim thay workspace voi ID: " + workspaceId);
        }
        if (u == null) {
            throw new IllegalArgumentException("Khong tim thay user voi ID: " + userId);
        }
        
        uw.setUserId(u);
        uw.setWorkspaceId(w);
        
        this.workspaceRepo.addUserIntoWorkspace(uw);
        return uw;
    }
}
