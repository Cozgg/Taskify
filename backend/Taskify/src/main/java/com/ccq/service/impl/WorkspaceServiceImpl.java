package com.ccq.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ccq.pojo.Board;
import com.ccq.pojo.User;
import com.ccq.pojo.UserWorkspace;
import com.ccq.pojo.Workspace;
import com.ccq.pojo.message.NotificationMessage;
import com.ccq.repository.UserRepository;
import com.ccq.repository.WorkspaceRepository;
import com.ccq.service.NotificationProducer;
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

    @Autowired
    private NotificationProducer notificationProducer;

    private String getCurrentActorName() {
        return SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "System";
    }

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
    public List<Workspace> getAccessibleWorkspaces() {
        User u = this.userRepo.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (u.getId() <= 0) {
            throw new IllegalArgumentException("User ID phải là số dương");
        }
        permissionService.requireUserSelfOrAdmin(u.getId());
        List<Workspace> workspaces = this.workspaceRepo.getAccessibleWorkspaces(u.getId());
        if (workspaces != null) {
            workspaces.forEach(this::initializeWorkspaceCollections);
        }
        return workspaces;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Workspace> getAccessibleWorkspaces(Map<String, String> params) {
        User u = this.userRepo.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (u.getId() <= 0) {
            throw new IllegalArgumentException("User ID phải là số dương");
        }
        permissionService.requireUserSelfOrAdmin(u.getId());
        List<Workspace> workspaces = this.workspaceRepo.getAccessibleWorkspaces(u.getId(), params);
        if (workspaces != null) {
            workspaces.forEach(this::initializeWorkspaceCollections);
        }
        return workspaces;
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAccessibleWorkspaces() {
        User u = this.userRepo.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (u.getId() <= 0) {
            throw new IllegalArgumentException("User ID phải là số dương");
        }
        permissionService.requireUserSelfOrAdmin(u.getId());
        return this.workspaceRepo.countAccessibleWorkspaces(u.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasWorkspace(int ownerId) {
        if (ownerId <= 0) {
            throw new IllegalArgumentException("User ID phải là số dương");
        }
        return !this.workspaceRepo.getAccessibleWorkspaces(ownerId).isEmpty();
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
        if (w == null) {
            throw new IllegalArgumentException("Workspace ko tồn tại");
        }
        User u = userRepo.findUserById(userId);

        if (u == null) {
            throw new UsernameNotFoundException("User ko tìm thấy");
        }

        if (this.workspaceRepo.isUserExistInWorkspace(workspaceId, userId)) {
            throw new IllegalArgumentException("User đã tồn tại");
        }

        uw.setUserId(u);
        uw.setWorkspaceId(w);

        this.workspaceRepo.addUserIntoWorkspace(uw);
        this.notificationProducer.sendEmailNotification(new NotificationMessage(
                "WORKSPACE_MEMBER_ADDED",
                u.getEmail() == null || u.getEmail().isBlank() ? List.of() : List.of(u.getEmail()),
                "Bạn được thêm vào workspace: " + w.getName(),
                "Xin chào " + u.getUsername() + ",\n\n"
                + "Bạn vừa được thêm vào workspace \"" + w.getName() + "\" trên Taskify.\n"
                + "Vui lòng đăng nhập hệ thống để xem chi tiết.",
                null,
                w.getId(),
                getCurrentActorName()
        ));
        return uw;
    }

    @Override
    @Transactional(readOnly = true)
    public Long countWorkspaces(Map<String, String> params) {
        return this.workspaceRepo.countWorkspaces(params);
    }
}
