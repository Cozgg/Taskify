/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.controller.client;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccq.pojo.Card;
import com.ccq.pojo.CardUser;
import com.ccq.pojo.User;
import com.ccq.pojo.response.ResAttachmentDTO;
import com.ccq.pojo.response.ResCardDTO;
import com.ccq.service.AttachmentService;
import com.ccq.service.CardService;
import com.ccq.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private AttachmentService attachService;

    @GetMapping("/lists/{listId}/cards")
    @PreAuthorize("@securityCustom.canAccessList(authentication.name, #listId)")
    public ResponseEntity<?> getCards(@PathVariable("listId") int listId, @RequestParam Map<String, String> params) {
        params.put("listId", String.valueOf(listId));
        List<Card> cards = this.cardService.getCard(params);
        List<ResCardDTO> cardDTOs = cards.stream().map(c
                -> new ResCardDTO(c.getId(), c.getName(), c.getDescription(), c.getIsActive(), c.getDueDate(), c.getReminderDate(), c.getPosition(), c.getListId().getId())
        ).collect(Collectors.toList());

        return new ResponseEntity<>(cardDTOs, HttpStatus.OK);
    }

    @PostMapping("/lists/{listId}/cards")
    @PreAuthorize("@securityCustom.canAccessList(authentication.name, #listId)")
    public ResponseEntity<?> createCard(
            @PathVariable("listId") int listId,
            @RequestBody Card c) {
        try {
            this.cardService.createCardInList(listId, c);

            ResCardDTO dto = new ResCardDTO(c.getId(), c.getName(), c.getDescription(), c.getIsActive(), c.getDueDate(), c.getReminderDate(), c.getPosition(), listId);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/cards/{cardId}")
    @PreAuthorize("@securityCustom.canAccessCard(authentication.name, #cardId)")
    public ResponseEntity<?> updateCard(@PathVariable("cardId") int cardId, @RequestBody Card c) {
        try {
            c.setId(cardId);
            this.cardService.addOrUpdate(c);

            Card updatedCard = this.cardService.getById(cardId);

            ResCardDTO dto = new ResCardDTO(updatedCard.getId(), updatedCard.getName(), updatedCard.getDescription(), updatedCard.getIsActive(), updatedCard.getDueDate(), updatedCard.getReminderDate(), updatedCard.getPosition(), updatedCard.getListId().getId());
            return new ResponseEntity<>(dto, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/cards/{cardId}")
    @PreAuthorize("@securityCustom.canAccessCard(authentication.name, #cardId)")
    public ResponseEntity<?> deleteCard(@PathVariable("cardId") int cardId) {
        try {
            this.cardService.delete(cardId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/cards/{cardId}/move")
    @PreAuthorize("@securityCustom.canAccessCard(authentication.name, #cardId)")
    public ResponseEntity<?> moveCard(
            @PathVariable("cardId") int cardId,
            @RequestBody Map<String, Integer> payload) {
        try {
            int newListId = payload.get("newListId");
            int newPosition = payload.get("newPosition");

            this.cardService.moveCard(cardId, newListId, newPosition);
            Card updateCard = this.cardService.getById(cardId);

            ResCardDTO dto = new ResCardDTO(updateCard.getId(), updateCard.getName(), updateCard.getDescription(), updateCard.getIsActive(), updateCard.getDueDate(), updateCard.getReminderDate(), updateCard.getPosition(), updateCard.getListId().getId());
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cards/{cardId}/assign")
    @PreAuthorize("@securityCustom.canAccessCard(authentication.name, #cardId)")
    public ResponseEntity<?> assignUserToCard(@PathVariable("cardId") int cardId) {

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = this.userService.getUserByUsername(currentUsername);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tìm thấy user!");
        }
        CardUser ac = this.cardService.assignUserForCard(currentUser.getId(), cardId);
        return new ResponseEntity<>(ac, HttpStatus.CREATED);
    }
    
    @PostMapping(path = "/cards/{cardId}/attach", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResAttachmentDTO> attachFile(@PathVariable("cardId") int cardId,
            @RequestParam Map<String, String> params, @RequestParam(value = "attachment") MultipartFile file){ 
        ResAttachmentDTO dto = this.attachService.addFile(cardId, params, file);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/attachment/{attachId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAttach(@PathVariable("attachId") int attachId){
        this.attachService.deleteFile(attachId);
    }

}
