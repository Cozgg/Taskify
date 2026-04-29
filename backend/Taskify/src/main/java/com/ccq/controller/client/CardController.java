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
import com.ccq.pojo.response.ResUserDTO;
import com.ccq.service.AttachmentService;
import com.ccq.service.CardService;
import com.ccq.service.UserService;
import com.ccq.utils.DTOMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

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
    public ResponseEntity<?> getCards(@PathVariable("listId") int listId, @RequestParam Map<String, String> params) {
        params.put("listId", String.valueOf(listId));
        List<Card> cards = this.cardService.getCard(params);
        List<ResCardDTO> cardDTOs = cards.stream()
                .map(DTOMapper::toCardDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(cardDTOs, HttpStatus.OK);
    }

    @PostMapping("/lists/{listId}/cards")
    public ResponseEntity<?> createCard(
            @PathVariable("listId") int listId,
            @Valid @RequestBody Card c) {
        try {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentUser = this.userService.getUserByUsername(currentUsername);
            c.setUserId(currentUser);
            c.setCreatedDate(new java.util.Date());
            c.setIsActive(true);
            
            this.cardService.createCardInList(listId, c);

            ResCardDTO dto = DTOMapper.toCardDTO(c);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/cards/{cardId}")
    public ResponseEntity<?> updateCard(@PathVariable("cardId") int cardId, @Valid @RequestBody Card c) {
        try {
            Card existingCard = this.cardService.getById(cardId);
            if (existingCard == null) {
                return new ResponseEntity<>("Không tìm thấy thẻ!", HttpStatus.NOT_FOUND);
            }

            if (c.getName() != null) existingCard.setName(c.getName());
            if (c.getDescription() != null) existingCard.setDescription(c.getDescription());
            if (c.getDueDate() != null) existingCard.setDueDate(c.getDueDate());
            if (c.getReminderDate() != null) existingCard.setReminderDate(c.getReminderDate());
            if (c.getIsActive() != null) existingCard.setIsActive(c.getIsActive());
            if (c.getPosition() != null) existingCard.setPosition(c.getPosition());

            this.cardService.addOrUpdate(existingCard);

            ResCardDTO dto = DTOMapper.toCardDTO(existingCard);
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

            ResCardDTO dto = DTOMapper.toCardDTO(updateCard);
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            String detail = e.getClass().getSimpleName() + ": " + e.getMessage();
            return ResponseEntity.badRequest().body(detail);
        }
    }

    @GetMapping("/cards/{cardId}/members")
    public ResponseEntity<List<ResUserDTO>> getMembersInCard(@PathVariable("cardId") int cardId) {
        return new ResponseEntity<>(this.cardService.getMemberInCard(cardId), HttpStatus.OK);
    }

    @PostMapping("/cards/{cardId}/assign")
    public ResponseEntity<?> assignUserToCard(@PathVariable("cardId") int cardId, @RequestBody Map<String, Integer> payload) {
        Integer userId = payload.get("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thiếu thông tin userId!");
        }
        User currentUser = this.userService.getUserById(userId);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tìm thấy user!");
        }
        CardUser ac = this.cardService.assignUserForCard(currentUser.getId(), cardId);
        return new ResponseEntity<>(ac, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/cards/{cardId}/unassign")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserCard(@PathVariable("cardId") int cardId , @RequestBody Map<String, Integer> payload){
        Integer userId = payload.get("userId");
        this.cardService.removeUserInCard(userId, cardId);
    }

    @PostMapping(path = "/cards/{cardId}/attach", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResAttachmentDTO> attachFile(@PathVariable("cardId") int cardId,
            @RequestParam Map<String, String> params, @RequestParam(value = "file") MultipartFile file) {
        ResAttachmentDTO dto = this.attachService.addFile(cardId, params, file);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("/attachments/{attachId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAttach(@PathVariable("attachId") int attachId) {
        this.attachService.deleteFile(attachId);
    }

    @GetMapping("/cards/{cardId}/attachments")
    public ResponseEntity<List<ResAttachmentDTO>> getAttachments(@PathVariable("cardId") int cardId) {
        return new ResponseEntity<>(this.attachService.getAttachments(cardId), HttpStatus.OK);
    }

}
