package com.ccq.service.impl;

import com.ccq.pojo.Boardlist;
import com.ccq.pojo.Card;
import com.ccq.repository.CardRepository;
import com.ccq.repository.ListRepository;
import com.ccq.service.PermissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepo;

    @Mock
    private ListRepository listRepo;

    @Mock
    private com.ccq.repository.UserRepository userRepo;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    void getById_existingCard_requiresAccessAndReturnsCard() {
        Card card = new Card();
        card.setId(1);

        when(cardRepo.getById(1)).thenReturn(card);

        Card result = cardService.getById(1);

        assertSame(card, result);
        verify(permissionService).requireCardAccess(1);
    }

    @Test
    void addOrUpdate_newCardInList_requiresListWritePermissionAndPersists() {
        Boardlist list = new Boardlist();
        list.setId(5);

        Card card = new Card();
        card.setListId(list);

        cardService.addOrUpdate(card);

        verify(permissionService).requireListWritePermission(5);
        verify(cardRepo).addOrUpdate(card);
    }

    @Test
    void moveCard_existingCardAndList_updatesPositionAndList() {
        Card card = new Card();
        card.setId(1);
        
        Boardlist newList = new Boardlist();
        newList.setId(2);

        when(cardRepo.getById(1)).thenReturn(card);
        when(listRepo.getById(2)).thenReturn(newList);

        cardService.moveCard(1, 2, 0);

        assertEquals(newList, card.getListId());
        assertEquals(0, card.getPosition());
        verify(permissionService).requireCardWritePermission(1);
        verify(cardRepo).addOrUpdate(card);
    }

    @Test
    void createCardInList_validInput_setsListAndPersists() {
        Boardlist list = new Boardlist();
        list.setId(10);
        
        Card card = new Card();
        card.setName("New Card");

        when(listRepo.getById(10)).thenReturn(list);

        cardService.createCardInList(10, card);

        assertEquals(list, card.getListId());
        verify(permissionService).requireListWritePermission(10);
        verify(cardRepo).addOrUpdate(card);
    }

    @Test
    void delete_existingCard_requiresWritePermissionAndDeletes() {
        cardService.delete(1);

        verify(permissionService).requireCardWritePermission(1);
        verify(cardRepo).delete(1);
    }

    @Test
    void getCard_withListId_requiresListAccessAndReturnsList() {
        java.util.Map<String, String> params = new java.util.HashMap<>();
        params.put("listId", "5");
        
        java.util.List<Card> mockList = java.util.Collections.singletonList(new Card());
        when(cardRepo.getCard(params)).thenReturn(mockList);

        java.util.List<Card> result = cardService.getCard(params);

        assertSame(mockList, result);
        verify(permissionService).requireListAccess(5);
    }

    @Test
    void assignUserForCard_validInput_assignsAndReturns() {
        com.ccq.pojo.User user = new com.ccq.pojo.User();
        user.setId(1);
        
        Card card = new Card();
        card.setId(2);
        
        when(userRepo.findUserById(1)).thenReturn(user);
        when(cardRepo.findCardById(2)).thenReturn(card);
        when(cardRepo.isUserInCard(1, 2)).thenReturn(false);
        
        com.ccq.pojo.CardUser result = cardService.assignUserForCard(1, 2);
        
        verify(permissionService).requireCardWritePermission(2);
        assertNotNull(result);
        assertEquals(user, result.getUserId());
        assertEquals(card, result.getCardId());
        verify(cardRepo).assignUserForCard(any(com.ccq.pojo.CardUser.class));
    }

    @Test
    void getMemberInCard_returnsUserDTOList() {
        com.ccq.pojo.User user = new com.ccq.pojo.User();
        user.setId(1);
        user.setUsername("Test User");
        
        when(cardRepo.getMemberInCard(10)).thenReturn(java.util.Collections.singletonList(user));
        
        java.util.List<com.ccq.pojo.response.ResUserDTO> result = cardService.getMemberInCard(10);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void removeUserInCard_existingUser_removes() {
        when(cardRepo.isUserInCard(1, 2)).thenReturn(true);
        
        cardService.removeUserInCard(1, 2);
        
        verify(permissionService).requireCardWritePermission(2);
        verify(cardRepo).removeUserInCard(1, 2);
    }
}
