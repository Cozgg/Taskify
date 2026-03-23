package com.ccq.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ccq.pojo.Board;
import com.ccq.pojo.User;
import com.ccq.pojo.Workspace;
import com.ccq.repository.BoardRepository;
import com.ccq.repository.UserRepository;
import com.ccq.repository.WorkspaceRepository;
import com.ccq.service.WorkspaceService;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private WorkspaceRepository workspaceRepo;

    @Autowired
    private BoardRepository boardRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public Workspace getWorkspaceById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Workspace ID phải là số dương, nhận được: " + id);
        }
        return this.workspaceRepo.getWorkspaceById(id);
    }

    @Override
    public Workspace getWorkspaceByOwnerId(int ownerId) {
        if (ownerId <= 0) {
            throw new IllegalArgumentException("Owner ID phải là số dương");
        }
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
            var auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));// chinh role lại
            if (!isAdmin) {
                User currentUser = this.userRepo.getUserByUsername(auth.getName());
                if (currentUser == null || !existing.getOwnerId().getId().equals(currentUser.getId())) {
                    throw new SecurityException("Bạn không có quyền chỉnh sửa workspace này");
                }
            }
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
        
        var auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));// chỉnh role lại
        if (!isAdmin) {
            User currentUser = this.userRepo.getUserByUsername(auth.getName());
            if (currentUser == null || !existing.getOwnerId().getId().equals(currentUser.getId())) {
                throw new SecurityException("Bạn không có quyền xóa workspace này");
            }
        }

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
        return this.workspaceRepo.getBoardsByWorkspaceId(wsId);
    }

    @Override
    public List<User> getMembersByWorkspaceId(int workspaceId) {
        if (workspaceId <= 0) {
            throw new IllegalArgumentException("Workspace ID phải là số dương, nhận được: " + workspaceId);
        }
        return this.workspaceRepo.getMembersByWorkspaceId(workspaceId);
    }

    @Override
    public Long countUserInWorkspace(int workspaceId) {
        if (workspaceId <= 0) {
            throw new IllegalArgumentException("Workspace ID phải là số dương, nhận được: " + workspaceId);
        }
        return this.userRepo.count();
    }

    @Override
    public Long countBoardInWorkspace(int workspaceId) {
        if (workspaceId <= 0) {
            throw new IllegalArgumentException("Workspace ID phải là số dương, nhận được: " + workspaceId);
        }
        return this.boardRepo.count();
    }
}
