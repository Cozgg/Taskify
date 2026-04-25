package com.ccq.service.impl;

import com.ccq.pojo.Boardlist;
import com.ccq.pojo.Card;
import com.ccq.pojo.CardUser;
import com.ccq.pojo.User;
import com.ccq.pojo.Workspace;
import com.ccq.repository.CardRepository;
import com.ccq.repository.ListRepository;
import com.ccq.repository.UserRepository;
import com.ccq.service.CardService;
import com.ccq.service.PermissionService;
import com.ccq.state.CardState;
import com.ccq.state.DoneState;
import com.ccq.state.InProgressState;
import com.ccq.state.ToDoState;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepo;

    @Autowired
    private ListRepository listRepo;
    
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PermissionService permissionService;

    @Override
    public Card getById(int id) {
        Card card = this.cardRepo.getById(id);
        if (card != null) {
            permissionService.requireCardAccess(id);
        }
        return card;
    }

    @Override
    public void addOrUpdate(Card c) {
        if (c.getId() != null) {
            permissionService.requireCardWritePermission(c.getId());
        } else if (c.getListId() != null && c.getListId().getId() != null) {
            permissionService.requireListWritePermission(c.getListId().getId());
        }
        this.cardRepo.addOrUpdate(c);
    }

    @Override
    public void delete(int id) {
        permissionService.requireCardWritePermission(id);
        this.cardRepo.delete(id);
    }

    @Override
    public List<Card> getCard(Map<String, String> params) {
        if (params != null && params.containsKey("listId")) {
            permissionService.requireListAccess(Integer.parseInt(params.get("listId")));
        }
        return this.cardRepo.getCard(params);
    }

    @Override
    public void createCardInList(int listId, Card c) {
        permissionService.requireListWritePermission(listId);
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
        permissionService.requireCardWritePermission(cardId);

        Card card = this.cardRepo.getById(cardId);
        if (card == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Không tìm thấy thẻ cần di chuyển!");
        }

        Boardlist newList = this.listRepo.getById(newListId);
        if (newList == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Không tìm thấy cột đích!");
        }

        card.setListId(newList);
        card.setPosition(newPosition);
        this.cardRepo.addOrUpdate(card);
        return "";
    }
    
    @Override
    public CardUser assignUserForCard(int userId, int cardId) {
        permissionService.requireCardWritePermission(cardId);
        User u = this.userRepo.findUserById(userId);
        if(u == null){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "User not found");
        }
        Card c = this.cardRepo.findCardById(cardId);
        if(c == null){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Card not found");
        }
        
        boolean isUserValid = this.cardRepo.isUserInCard(userId, cardId);
        if(isUserValid){
            throw  new ResponseStatusException(HttpStatusCode.valueOf(409), "User đã tồn tại trong card");
        }
        CardUser ac = new CardUser();
        ac.setCardId(c);
        ac.setUserId(u);
        ac.setAssignedDate(new Date());
        this.cardRepo.assignUserForCard(ac);
        return ac;
    }

    @Override
    public void removeUserFromCard(int userId, int cardId) {
        permissionService.requireCardWritePermission(cardId);
        boolean isUserValid = this.cardRepo.isUserInCard(userId, cardId);
        if(!isUserValid){
            throw  new ResponseStatusException(HttpStatusCode.valueOf(404), "User không tồn tại trong card");
        }
        this.cardRepo.removeUserFromCard(userId, cardId);
    }

}
