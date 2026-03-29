package com.ccq.service.impl;

import com.ccq.pojo.Card;
import com.ccq.pojo.Workspace;
import com.ccq.repository.CardRepository;
import com.ccq.repository.ListRepository;
import com.ccq.service.CardService;
import com.ccq.service.PermissionService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepo;

    @Autowired
    private ListRepository listRepo;

    @Autowired
    private PermissionService permissionService;

    @Override
    public Card getById(int id) {
        return this.cardRepo.getById(id);
    }

    @Override
    public void addOrUpdate(Card c) {
        this.cardRepo.addOrUpdate(c);
    }

    @Override
    public void delete(int id) {
        this.cardRepo.delete(id);
    }

    @Override
    public List<Card> getCard(Map<String, String> params) {
        return this.cardRepo.getCard(params);
    }

    @Override
    public void createCardInList(int listId, Card c) {
        com.ccq.pojo.List list = this.listRepo.getById(listId);
        if (list == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy cột");
        }
        c.setListId(list);
        this.cardRepo.addOrUpdate(c);
    }

    @Override
    public void moveCard(int cardId, int newListId, int newPosition) {
        Card card = this.cardRepo.getById(cardId);
        if (card == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Không tìm thấy thẻ cần di chuyển!");
        }

        // Truy ngược: Card → List → Board → Workspace để kiểm tra tư cách thành viên
        int workspaceId = resolveWorkspaceId(card);
        permissionService.requireWorkspaceMember(workspaceId);

        if (card.getListId() == null || card.getListId().getId() != newListId) {
            com.ccq.pojo.List newList = this.listRepo.getById(newListId);
            if (newList == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy cột đích!");
            }
            card.setListId(newList);
        }
        card.setPosition(newPosition);
        this.cardRepo.addOrUpdate(card);
    }

    /** Truy ngược Card → List → Board → Workspace, ném lỗi nếu thiếu liên kết. */
    private int resolveWorkspaceId(Card card) {
        com.ccq.pojo.List list = card.getListId();
        if (list == null || list.getBoardId() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Không xác định được Workspace của thẻ này");
        }
        Workspace ws = list.getBoardId().getWorkspaceId();
        if (ws == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Board không thuộc Workspace nào");
        }
        return ws.getId();
    }
}