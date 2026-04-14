package com.ccq.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ccq.pojo.Board;
import com.ccq.pojo.Workspace;
import com.ccq.repository.BoardRepository;
import com.ccq.repository.WorkspaceRepository;
import com.ccq.service.BoardService;
import com.ccq.service.PermissionService;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepo;

    @Autowired
    private WorkspaceRepository wsRepo;

    @Autowired
    private PermissionService permissionService;

    @Override
    public Board getById(int id) {
        Board board = this.boardRepo.getById(id);
        if (board != null) {
            permissionService.requireBoardAccess(id);
        }
        return board;
    }

    @Override
    public void addOrUpdate(Board b) {
        if (b == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Board khong duoc de trong");
        }
        if (b.getId() != null) {
            permissionService.requireBoardWritePermission(b.getId());
        } else if (b.getWorkspaceId() != null && b.getWorkspaceId().getId() != null) {
            permissionService.requireWorkspaceAccess(b.getWorkspaceId().getId());
        }
        this.boardRepo.addOrUpdate(b);
    }

    @Override
    public void delete(int id) {
        Board board = this.boardRepo.getById(id);
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Không tìm thấy Board với ID: " + id);
        }

        // Lấy workspace chứa board để kiểm tra quyền xóa
        Workspace ws = board.getWorkspaceId();
        if (ws == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Board không thuộc Workspace nào");
        }

        // Chỉ ADMIN hoặc owner workspace mới được xóa board
        permissionService.requireBoardDeletePermission(id);

        this.boardRepo.delete(id);
    }

    @Override
    public List<Board> getBoards(Map<String, String> params) {
        if (params != null && params.containsKey("workspaceId")) {
            permissionService.requireWorkspaceAccess(Integer.parseInt(params.get("workspaceId")));
        }
        return this.boardRepo.getBoards(params);
    }

    @Override
    public Long countBoards(Map<String, String> params) {
        if (params != null && params.containsKey("workspaceId")) {
            permissionService.requireWorkspaceAccess(Integer.parseInt(params.get("workspaceId")));
        }
        return this.boardRepo.countBoards(params);
    }

    @Override
    public Board createBoardInWorkspace(int workspaceId, Board board) {
        permissionService.requireWorkspaceAccess(workspaceId);
        Workspace ws = this.wsRepo.getWorkspaceById(workspaceId);
        if (ws == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Không tìm thấy Workspace: " + workspaceId);
        }
        board.setWorkspaceId(ws);
        this.boardRepo.addOrUpdate(board);
        return board;
    }
}
