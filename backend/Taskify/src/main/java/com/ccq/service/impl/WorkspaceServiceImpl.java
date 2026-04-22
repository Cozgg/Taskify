package com.ccq.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ccq.pojo.Board;
import com.ccq.pojo.User;
import com.ccq.pojo.UserWorkspace;
import com.ccq.pojo.Workspace;
import com.ccq.repository.UserRepository;
import com.ccq.repository.WorkspaceRepository;
import com.ccq.service.PermissionService;
import com.ccq.service.WorkspaceService;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private WorkspaceRepository workspaceRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PermissionService permissionService;

    private void initializeWorkspaceCollections(Workspace workspace) {
        if (workspace == null) {
            return;
        }
        if (workspace.getBoardSet() != null) {
            workspace.getBoardSet().size();
        }
        if (workspace.getUserWorkspaceSet() != null) {
            workspace.getUserWorkspaceSet().size();
        }
    }

    private void initializeBoardCollections(Board board) {
        if (board == null) {
            return;
        }
        if (board.getBoardlistSet() != null) {
            board.getBoardlistSet().size();
            board.getBoardlistSet().forEach((bl) -> {
                if (bl != null && bl.getCardSet() != null) {
                    bl.getCardSet().size();
                }
            });
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Workspace getWorkspaceById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Workspace ID phải là số dương, nhận được: " + id);
        }
        Workspace workspace = this.workspaceRepo.getWorkspaceById(id);
        if (workspace != null) {
            permissionService.requireWorkspaceAccess(id);
            initializeWorkspaceCollections(workspace);
        }
        return workspace;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Workspace> getWorkspaceByOwnerId(int ownerId) {
        if (ownerId <= 0) {
            throw new IllegalArgumentException("Owner ID phải là số dương");
        }
        permissionService.requireUserSelfOrAdmin(ownerId);
        List<Workspace> workspaces = this.workspaceRepo.getWorkspaceByOwnerId(ownerId);
        if (workspaces != null) {
            workspaces.forEach(this::initializeWorkspaceCollections);
        }
        return workspaces;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Workspace> getWorkspacesByOwnerId(int ownerId, Map<String, String> params) {
        if (ownerId <= 0) {
            throw new IllegalArgumentException("Owner ID phải là số dương");
        }
        permissionService.requireUserSelfOrAdmin(ownerId);
        List<Workspace> workspaces = this.workspaceRepo.getWorkspacesByOwnerId(ownerId, params);
        if (workspaces != null) {
            workspaces.forEach(this::initializeWorkspaceCollections);
        }
        return workspaces;
    }

    @Override
    @Transactional(readOnly = true)
    public Long countWorkspacesByOwnerId(int ownerId) {
        if (ownerId <= 0) {
            throw new IllegalArgumentException("Owner ID phải là số dương");
        }
        permissionService.requireUserSelfOrAdmin(ownerId);
        return this.workspaceRepo.countWorkspacesByOwnerId(ownerId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasWorkspace(int ownerId) {
        if (ownerId <= 0) {
            throw new IllegalArgumentException("Owner ID phải là số dương");
        }
        return !this.workspaceRepo.getWorkspaceByOwnerId(ownerId).isEmpty();
    }

    @Override
    @Transactional
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
            permissionService.requireWorkspaceOwnerPermission(w.getId());
        }

        boolean isCreate = w.getId() == null;
        this.workspaceRepo.addOrUpdate(w);

        if (isCreate) {
            UserWorkspace uw = new UserWorkspace();
            uw.setUserId(w.getOwnerId());
            uw.setWorkspaceId(w);
            this.workspaceRepo.addUserIntoWorkspace(uw);
        }
    }

    @Override
    @Transactional
    public void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Workspace ID phải là số dương, nhận được: " + id);
        }
        Workspace existing = this.workspaceRepo.getWorkspaceById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Không tìm thấy workspace với ID: " + id);
        }
        permissionService.requireWorkspaceOwnerPermission(id);
        this.workspaceRepo.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Workspace> getWorkspaces(Map<String, String> params) {
        List<Workspace> workspaces = this.workspaceRepo.getWorkspaces(params);
        if (workspaces != null) {
            workspaces.forEach(this::initializeWorkspaceCollections);
        }
        return workspaces;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Board> getBoardsByWorkspaceId(int wsId) {
        if (wsId <= 0) {
            throw new IllegalArgumentException("Workspace ID phải là số dương, nhận được: " + wsId);
        }
        permissionService.requireWorkspaceAccess(wsId);
        List<Board> boards = this.workspaceRepo.getBoardsByWorkspaceId(wsId);
        if (boards != null) {
            boards.forEach(this::initializeBoardCollections);
        }
        return boards;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getMembersByWorkspaceId(int workspaceId) {
        if (workspaceId <= 0) {
            throw new IllegalArgumentException("Workspace ID phải là số dương, nhận được: " + workspaceId);
        }
        permissionService.requireWorkspaceAccess(workspaceId);
        return this.workspaceRepo.getMembersByWorkspaceId(workspaceId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countUserInWorkspace(int workspaceId) {
        if (workspaceId <= 0) {
            throw new IllegalArgumentException("Workspace ID phải là số dương, nhận được: " + workspaceId);
        }
        permissionService.requireWorkspaceAccess(workspaceId);
        return this.workspaceRepo.countMembersByWorkspaceId(workspaceId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countBoardInWorkspace(int workspaceId) {
        if (workspaceId <= 0) {
            throw new IllegalArgumentException("Workspace ID phải là số dương, nhận được: " + workspaceId);
        }
        permissionService.requireWorkspaceAccess(workspaceId);
        return (long) this.workspaceRepo.getBoardsByWorkspaceId(workspaceId).size();
    }

    @Override
    @Transactional
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

    @Override
    @Transactional(readOnly = true)
    public Long countWorkspaces(Map<String, String> params) {
        return this.workspaceRepo.countWorkspaces(params);
    }
}
