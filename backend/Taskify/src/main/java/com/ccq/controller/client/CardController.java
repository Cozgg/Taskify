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
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping("/lists/{listId}/cards")
    public ResponseEntity<?> getCards(@PathVariable("listId") int listId, @RequestParam Map<String, String> params){
        params.put("listId", String.valueOf(listId));
        List<Card> cards = this.cardService.getCard(params);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }
    
    @PostMapping("/lists/{listId}/cards")
    public ResponseEntity<?> createCard(
            @PathVariable("listId") int listId, 
            @RequestBody Card c) {
        try {
            this.cardService.createCardInList(listId, c);
            return new ResponseEntity<>(c, HttpStatus.CREATED); 
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi tạo thẻ " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/cards/{cardId}") 
    public ResponseEntity<?> updateCard(@PathVariable("cardId") int cardId, @RequestBody Card c) {
        try {
            c.setId(cardId); 
            this.cardService.addOrUpdate(c);
            return new ResponseEntity<>(c, HttpStatus.OK); 
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi cập nhật thẻ " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/cards/{cardId}") 
    public ResponseEntity<?> deleteCard(@PathVariable("cardId") int cardId) {
        try {
            this.cardService.delete(cardId); 
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi xóa thẻ " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/cards/{cardId}/move")
    public ResponseEntity<?> moveCard(
            @PathVariable("cardId") int cardId, 
            @RequestBody Map<String, Integer> payload) { 
        try {
            int newListId = payload.get("newListId");
            int newPosition = payload.get("newPosition");
            
            String msg = this.cardService.moveCard(cardId, newListId, newPosition);
            return ResponseEntity.ok("Đã di chuyển thẻ thành công " + msg);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi kéo thả: " + e.getMessage());
        }
    }
}