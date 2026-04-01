package com.ccq.service.impl;

import com.ccq.pojo.Boardlist;
import com.ccq.pojo.Card;
import com.ccq.pojo.Workspace;
import com.ccq.repository.CardRepository;
import com.ccq.repository.ListRepository;
import com.ccq.service.CardService;
import com.ccq.service.PermissionService;
import com.ccq.state.CardState;
import com.ccq.state.DoneState;
import com.ccq.state.InProgressState;
import com.ccq.state.ToDoState;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

@Service

public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepo;

    @Autowired
    private ListRepository listRepo;

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
        Boardlist list = this.listRepo.getById(listId);
        if (list == null) {
            throw new RuntimeException("Không tìm thấy cột");
        }
        c.setListId(list);
        this.cardRepo.addOrUpdate(c);
    }

    @Override
    @Transactional
    public String moveCard(int cardId, int newListId, int newPosition) {
        Card card = this.cardRepo.getById(cardId);
        if (card == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Không tìm thấy thẻ cần di chuyển!");
        }

        Boardlist oldList = card.getListId();
        int oldPosition = card.getPosition();
        String msg = "";

        if (oldList == null || oldList.getId() != newListId) {
            Boardlist newList = this.listRepo.getById(newListId);
            if (newList == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy cột đích!");
            }

            CardState newState = switch (newList.getStatus().toString()) {
                case "IN_PROGRESS" ->
                    new InProgressState();
                case "DONE" ->
                    new DoneState();
                default ->
                    new ToDoState();
            };

            msg = card.changeState(newState, newList);

            if (oldList != null) {
                updatePositionsInList(oldList.getId(), oldPosition + 1, Integer.MAX_VALUE, -1);
            }

            updatePositionsInList(newListId, newPosition, Integer.MAX_VALUE, 1);

            card.setListId(newList);
        } else {
            if (oldPosition < newPosition) {
                updatePositionsInList(newListId, oldPosition + 1, newPosition, -1);
            } else if (oldPosition > newPosition) {
                updatePositionsInList(newListId, newPosition, oldPosition - 1, 1);
            }
        }

        card.setPosition(newPosition);
        this.cardRepo.addOrUpdate(card);
        return msg;
    }

    private void updatePositionsInList(int listId, int startPos, int endPos, int offset) {
        Map<String, String> params = new java.util.HashMap<>();
        params.put("listId", String.valueOf(listId));

        java.util.List<Card> cards = this.cardRepo.getCard(params);
        for (Card c : cards) {
            if (c.getPosition() >= startPos && c.getPosition() <= endPos) {
                c.setPosition(c.getPosition() + offset);
                this.cardRepo.addOrUpdate(c);
            }
        }
    }

}
