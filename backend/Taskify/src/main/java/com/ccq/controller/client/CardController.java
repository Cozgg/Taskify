/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.controller.client;

import com.ccq.dto.CardDTO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ccq.pojo.Card;
import com.ccq.pojo.CardUser;
import com.ccq.pojo.User;
import com.ccq.pojo.response.ResActivityDTO;
import com.ccq.service.CardService;
import com.ccq.service.UserService;
import com.ccq.utils.DTOMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;


@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private UserService userService;

    @GetMapping("/lists/{listId}/cards")
    public ResponseEntity<?> getCards(@PathVariable("listId") int listId, @RequestParam Map<String, String> params) {
        params.put("listId", String.valueOf(listId));
        List<Card> cards = this.cardService.getCard(params);
        List<CardDTO> cardDTOs = cards.stream().map(c
                -> new CardDTO(c.getId(), c.getName(), c.getDescription(), c.getIsActive(), c.getDueDate(), c.getReminderDate(), c.getPosition(), c.getListId().getId())
        ).collect(Collectors.toList());

        return new ResponseEntity<>(cardDTOs, HttpStatus.OK);
    }

    @PostMapping("/lists/{listId}/cards")
    public ResponseEntity<?> createCard(
            @PathVariable("listId") int listId,
            @RequestBody Card c) {
        try {
            this.cardService.createCardInList(listId, c);

            CardDTO dto = new CardDTO(c.getId(), c.getName(), c.getDescription(), c.getIsActive(), c.getDueDate(), c.getReminderDate(), c.getPosition(), listId);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/cards/{cardId}")
    public ResponseEntity<?> updateCard(@PathVariable("cardId") int cardId, @RequestBody Card c) {
        try {
            c.setId(cardId);
            this.cardService.addOrUpdate(c);

            Card updatedCard = this.cardService.getById(cardId);

            CardDTO dto = new CardDTO(updatedCard.getId(), updatedCard.getName(), updatedCard.getDescription(), updatedCard.getIsActive(), updatedCard.getDueDate(), updatedCard.getReminderDate(), updatedCard.getPosition(), updatedCard.getListId().getId());
            return new ResponseEntity<>(dto, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable("cardId") int cardId) {
        try {
            this.cardService.delete(cardId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/cards/{cardId}/move")
    public ResponseEntity<?> moveCard(
            @PathVariable("cardId") int cardId,
            @RequestBody Map<String, Integer> payload) {
        try {
            int newListId = payload.get("newListId");
            int newPosition = payload.get("newPosition");

            this.cardService.moveCard(cardId, newListId, newPosition);
            Card updateCard = this.cardService.getById(cardId);

            CardDTO dto = new CardDTO(updateCard.getId(), updateCard.getName(), updateCard.getDescription(), updateCard.getIsActive(), updateCard.getDueDate(), updateCard.getReminderDate(), updateCard.getPosition(), updateCard.getListId().getId());
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cards/{cardId}/assign")
    public ResponseEntity<?> assignUserToCard(@PathVariable("cardId") int cardId) {

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = this.userService.getUserByUsername(currentUsername);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tìm thấy user!");
        }
        CardUser ac = this.cardService.assignUserForCard(currentUser.getId(), cardId);
        return new ResponseEntity<>(ac, HttpStatus.CREATED);
    }

}
