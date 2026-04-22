package com.ccq.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ccq.pojo.Board;
import com.ccq.pojo.Boardlist;
import com.ccq.pojo.Card;
import com.ccq.pojo.Comment;
import com.ccq.pojo.User;
import com.ccq.pojo.Workspace;
import com.ccq.repository.BoardRepository;
import com.ccq.repository.CardRepository;
import com.ccq.repository.CommentRepository;
import com.ccq.repository.ListRepository;
import com.ccq.repository.UserRepository;
import com.ccq.repository.WorkspaceRepository;


@Service
public class PermissionService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private WorkspaceRepository workspaceRepo;

    @Autowired
    private BoardRepository boardRepo;

    @Autowired
    private ListRepository listRepo;

    @Autowired
    private CardRepository cardRepo;

    @Autowired
    private CommentRepository commentRepo;

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn chưa đăng nhập hoặc phiên đăng nhập đã hết hạn");
        }

        User user = userRepo.getUserByUsername(auth.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không tìm thấy người dùng");
        }
        return user;
    }

    public boolean isCurrentUserAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private boolean isWorkspaceOwner(Workspace workspace, User user) {
        return workspace.getOwnerId() != null
                && workspace.getOwnerId().getId() != null
                && workspace.getOwnerId().getId().equals(user.getId());
    }

    private boolean isWorkspaceMember(int workspaceId, int userId) {
        List<User> members = workspaceRepo.getMembersByWorkspaceId(workspaceId);
        return members.stream().anyMatch(member -> member.getId().equals(userId));
    }

    // được phép truy cập tài nguyên nếu là ADMIN hoặc thuộc workspace đó (owner hoặc member)
    public void requireUserSelfOrAdmin(int userId) {
        if (isCurrentUserAdmin()) {
            return;
        }

        User me = getCurrentUser();
        if (!me.getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không được phép truy cập tài nguyên này");
        }
    }

    // được xem tài nguyên nếu là ADMIN hoặc thuộc workspace đó (owner hoặc member)
    public void requireWorkspaceAccess(int workspaceId) {
        if (isCurrentUserAdmin()) {
            return;
        }

        Workspace workspace = getWorkspaceOrThrow(workspaceId);
        User me = getCurrentUser();
        if (isWorkspaceOwner(workspace, me) || isWorkspaceMember(workspaceId, me.getId())) {
            return;
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không thuộc workspace này");
    }

    public void requireBoardAccess(int boardId) {
        Board board = getBoardOrThrow(boardId);
        requireWorkspaceAccess(getWorkspaceId(board));
    }

    public void requireListAccess(int listId) {
        Boardlist list = getListOrThrow(listId);
        requireWorkspaceAccess(getWorkspaceId(list));
    }

    public void requireCardAccess(int cardId) {
        Card card = getCardOrThrow(cardId);
        requireWorkspaceAccess(getWorkspaceId(card));
    }

    // được chỉnh sửa tài nguyên nếu có quyền truy cập + là ADMIN hoặc owner workspace
    public void requireBoardWritePermission(int boardId) {
        requireBoardAccess(boardId);
    }

    public void requireListWritePermission(int listId) {
        requireListAccess(listId);
    }

    public void requireCardWritePermission(int cardId) {
        requireCardAccess(cardId);
    }

    // chỉ được thực hiện thao tác nếu là ADMIN hoặc owner workspace
    public void requireWorkspaceOwnerPermission(int workspaceId) {
        if (isCurrentUserAdmin()) {
            return;
        }

        Workspace workspace = getWorkspaceOrThrow(workspaceId);
        User me = getCurrentUser();
        if (!isWorkspaceOwner(workspace, me)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chỉ owner workspace mới được thực hiện thao tác này");
        }
    }

    public void requireBoardDeletePermission(int boardId) {
        Board board = getBoardOrThrow(boardId);
        requireWorkspaceOwnerPermission(getWorkspaceId(board));
    }

    public void requireCommentDeletePermission(int commentId) {
        if (isCurrentUserAdmin()) {
            return;
        }

        Comment comment = getCommentOrThrow(commentId);
        User me = getCurrentUser();
        if (comment.getUserId() != null && me.getId().equals(comment.getUserId().getId())) {
            return;
        }

        requireWorkspaceOwnerPermission(getWorkspaceId(comment));
    }

    // lấy tài nguyên và kiểm tra quyền
    private Workspace getWorkspaceOrThrow(int workspaceId) {
        Workspace workspace = workspaceRepo.getWorkspaceById(workspaceId);
        if (workspace == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy workspace");
        }
        return workspace;
    }

    private Board getBoardOrThrow(int boardId) {
        Board board = boardRepo.getById(boardId);
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy board");
        }
        return board;
    }

    private Boardlist getListOrThrow(int listId) {
        Boardlist list = listRepo.getById(listId);
        if (list == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy list");
        }
        return list;
    }

    private Card getCardOrThrow(int cardId) {
        Card card = cardRepo.findCardById(cardId);
        if (card == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy card");
        }
        return card;
    }

    private Comment getCommentOrThrow(int commentId) {
        Comment comment = commentRepo.getCommentById(commentId);
        if (comment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy comment");
        }
        return comment;
    }

    // lấy workspaceId từ board → list → card → comment để kiểm tra quyền truy cập
    private int getWorkspaceId(Board board) {
        if (board.getWorkspaceId() == null || board.getWorkspaceId().getId() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Board chưa gắn workspace");
        }
        return board.getWorkspaceId().getId();
    }

    private int getWorkspaceId(Boardlist list) {
        if (list.getBoardId() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "List chưa gắn board");
        }
        return getWorkspaceId(list.getBoardId());
    }

    private int getWorkspaceId(Card card) {
        if (card.getListId() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Card chưa gắn list");
        }
        return getWorkspaceId(card.getListId());
    }

    private int getWorkspaceId(Comment comment) {
        if (comment.getCardId() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Comment chưa gắn card");
        }
        return getWorkspaceId(comment.getCardId());
    }
}
