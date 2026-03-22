/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.controller;

import com.ccq.pojo.Card;
import com.ccq.service.CardService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nguye
 */
@RestController
@RequestMapping("/api/v1")
public class CardController {

    @Autowired
    private CardService cardService;

    // 1. Lấy danh sách thẻ (có tìm kiếm và phân trang)
    @GetMapping("/cards")
    public ResponseEntity<List<Card>> getCards(@RequestParam Map<String, String> params) {
        List<Card> cards = this.cardService.getCard(params);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    // 2. Lấy chi tiết 1 thẻ
    @GetMapping("/cards/{cardId}")
    public ResponseEntity<?> getCardById(@PathVariable int cardId) {
        Card card = this.cardService.getById(cardId);
        if (card != null) {
            return new ResponseEntity<>(card, HttpStatus.OK);
        }
        return new ResponseEntity<>("Không tìm thấy thẻ", HttpStatus.NOT_FOUND);
    }

    // 3. Tạo thẻ mới
//    @PostMapping("/lists/{listId}/cards")
//    public ResponseEntity<?> createCard(@PathVariable int listId, @RequestBody Card card) {
//        try {
//            // Lưu ý: Trong Service, bạn cần lấy List bằng listId và gán vào Card trước khi lưu
//            this.cardService.addOrUpdate(listId, card); 
//            return new ResponseEntity<>(card, HttpStatus.CREATED); // 201 Created
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }

    // 4. Cập nhật thẻ
    @PutMapping("/cards/{cardId}")
    public ResponseEntity<?> updateCard(@PathVariable int cardId, @RequestBody Card card) {
        try {
            card.setId(cardId); // Đảm bảo ID không bị thay đổi sai lệch
            this.cardService.addOrUpdate(card);
            return new ResponseEntity<>(card, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 5. Xóa thẻ
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable int cardId) {
        try {
            this.cardService.delete(cardId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content (Xóa thành công)
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}