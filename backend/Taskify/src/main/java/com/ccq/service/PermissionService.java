package com.ccq.service;

import com.ccq.pojo.User;
import com.ccq.pojo.Workspace;
import com.ccq.repository.UserRepository;
import com.ccq.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service tập trung xử lý kiểm tra quyền (Authorization).
 * Được inject vào các ServiceImpl cần guard trước khi thực hiện thao tác nhạy cảm.
 */
@Service
public class PermissionService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private WorkspaceRepository workspaceRepo;


    // ─── Helpers ─────────────────────────────────────────────────────────────

    /** Lấy User đang đăng nhập từ SecurityContext. Ném 401 nếu chưa xác thực. */
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn chưa đăng nhập");
        }
        User user = userRepo.getUserByUsername(auth.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không tìm thấy người dùng");
        }
        return user;
    }

    /** Kiểm tra role hiện tại có phải ADMIN không. */
    public boolean isCurrentUserAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    // ─── Guard: xóa Workspace ─────────────────────────────────────────────

    /**
     * Chỉ ADMIN hoặc owner của workspace mới được xóa.
     * Ném 403 nếu vi phạm.
     */
    public void requireDeleteWorkspacePermission(int workspaceId) {
        if (isCurrentUserAdmin()) return;

        Workspace ws = workspaceRepo.getWorkspaceById(workspaceId);
        if (ws == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Workspace không tồn tại: " + workspaceId);
        }

        User me = getCurrentUser();
        if (!ws.getOwnerId().getId().equals(me.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Bạn không có quyền xóa Workspace này");
        }
    }

    // ─── Guard: xóa Board ─────────────────────────────────────────────────

    /**
     * Chỉ ADMIN hoặc owner của workspace chứa board mới được xóa board.
     * @param workspaceId ID workspace chứa board cần xóa.
     */
    public void requireDeleteBoardPermission(int workspaceId) {
        if (isCurrentUserAdmin()) return;

        Workspace ws = workspaceRepo.getWorkspaceById(workspaceId);
        if (ws == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Workspace không tồn tại: " + workspaceId);
        }

        User me = getCurrentUser();
        if (!ws.getOwnerId().getId().equals(me.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Chỉ owner của Workspace mới được xóa Board");
        }
    }

    // ─── Guard: thao tác trong Workspace (Card, Comment) ─────────────────

    /**
     * Người dùng hiện tại phải là thành viên của workspace.
     * ADMIN bỏ qua kiểm tra này.
     * @param workspaceId ID workspace cần kiểm tra tư cách thành viên.
     */
//    public void requireWorkspaceMember(int workspaceId) {
//        if (isCurrentUserAdmin()) return;
//
//        User me = getCurrentUser();
//        boolean isMember = userWorkspaceRepo.isUserInWorkspace(me.getId(), workspaceId);
//        if (!isMember) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
//                    "Bạn không phải thành viên của Workspace này");
//        }
//    }
}
