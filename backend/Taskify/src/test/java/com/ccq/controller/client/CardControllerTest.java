package com.ccq.controller.client;

import com.ccq.pojo.Card;
import com.ccq.pojo.User;
import com.ccq.service.CardService;
import com.ccq.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {

    @Mock
    private CardService cardService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CardController cardController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createCard_validInput_returnsCreated() {
        Card card = new Card();
        card.setId(1);
        card.setName("New Card");
        User user = new User();
        user.setUsername("testuser");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userService.getUserByUsername("testuser")).thenReturn(user);

        ResponseEntity<?> response = cardController.createCard(1, card);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, card.getUserId());
        verify(cardService).createCardInList(1, card);
    }

    @Test
    void updateCard_existingCard_returnsOk() {
        Card card = new Card();
        card.setId(1);
        card.setName("Updated Title");

        when(cardService.getById(1)).thenReturn(card);

        ResponseEntity<?> response = cardController.updateCard(1, card);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cardService).addOrUpdate(card);
    }
}
