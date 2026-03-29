/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.controller.client;

import com.ccq.pojo.Card;
import com.ccq.service.CardService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author nguye
 */
@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping("/lists/{listId}")
    public ResponseEntity<?> getCards(@PathVariable("listId") int listId, @RequestParam Map<String, String> params){
        params.put("listId", String.valueOf(listId));
        List<Card> cards = this.cardService.getCard(params);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }
    
    @PostMapping("/lists/{listId}")
    public ResponseEntity<?> createCard(
            @PathVariable("listId") int listId, 
            @RequestBody Card c) {
        try {
            this.cardService.createCardInList(listId, c);
            return new ResponseEntity<>(c, HttpStatus.CREATED); 
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{cardId}") 
    public ResponseEntity<?> updateCard(@PathVariable("cardId") int cardId, @RequestBody Card c) {
        try {
            c.setId(cardId); 
            this.cardService.addOrUpdate(c);
            return new ResponseEntity<>(c, HttpStatus.OK); 
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{cardId}") 
    public ResponseEntity<?> deleteCard(@PathVariable("cardId") int cardId) {
        try {
            this.cardService.delete(cardId); 
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{cardId}/move")
    public ResponseEntity<?> moveCard(
            @PathVariable("cardId") int cardId, 
            @RequestBody Map<String, Integer> payload) { 
        try {
            int newListId = payload.get("newListId");
            int newPosition = payload.get("newPosition");

            this.cardService.moveCard(cardId, newListId, newPosition);
            Card updateCard = this.cardService.getById(cardId);
            return ResponseEntity.ok(updateCard);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}