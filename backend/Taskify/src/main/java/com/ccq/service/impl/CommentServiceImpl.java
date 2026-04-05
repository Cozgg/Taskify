package com.ccq.service.impl;

import com.ccq.pojo.Card;
import com.ccq.pojo.Comment;
import com.ccq.pojo.User;
import com.ccq.pojo.Workspace;
import com.ccq.repository.CardRepository;
import com.ccq.repository.CommentRepository;
import com.ccq.repository.UserRepository;
import com.ccq.service.CommentService;
import com.ccq.service.PermissionService;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ccq.pojo.Boardlist;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CardRepository cardRepo;

    @Autowired
    private PermissionService permissionService;

    @Override
    public Comment addComment(Comment c, int userId, int cardId) {
        User u = this.userRepo.findUserById(userId);
        if (u == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy User");
        }

        Card ca = this.cardRepo.findCardById(cardId);
        if (ca == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy Card");
        }

//        int workspaceId = resolveWorkspaceId(ca);
//        permissionService.requireWorkspaceMember(workspaceId);

        c.setUserId(u);
        c.setCardId(ca);
        c.setCreatedDate(new Date());

        this.commRepo.addComment(c);
        return c;
    }

    // truy ngược Card → List → Board → Workspace để lấy workspaceId.
    private int resolveWorkspaceId(Card card) {
        Boardlist boardList = card.getListId();
        if (boardList == null || boardList.getBoardId() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Không xác định được Workspace của Card này");
        }
        Workspace ws = boardList.getBoardId().getWorkspaceId();
        if (ws == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Board không thuộc Workspace nào");
        }
        return ws.getId();
    }

    @Override
    public void deleteComment(int id) {
        this.commRepo.deleteComment(id);
    }
}
