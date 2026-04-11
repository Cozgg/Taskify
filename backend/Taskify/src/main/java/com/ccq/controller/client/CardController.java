/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.controller.client;

import com.ccq.pojo.Card;
import com.ccq.pojo.response.ResCardDTO;
import com.ccq.service.CardService;
import com.ccq.service.PermissionService;
import com.ccq.utils.DTOMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private PermissionService permissionService;
    private UserService userService;

    @GetMapping("/lists/{listId}/cards")
    @PreAuthorize("@securityCustom.canAccessList(authentication.name, #listId)")
    public ResponseEntity<?> getCards(@PathVariable("listId") int listId, @RequestParam Map<String, String> params) {
//        permissionService.requireListPermission(listId);

        params.put("listId", String.valueOf(listId));
        List<Card> cards = this.cardService.getCard(params);

        List<ResCardDTO> cardDTOs = cards.stream()
                .map(DTOMapper::toCardDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(cardDTOs, HttpStatus.OK);
    }

    @PostMapping("/lists/{listId}/cards")
    @PreAuthorize("@securityCustom.canAccessList(authentication.name, #listId)")
    public ResponseEntity<?> createCard(
            @PathVariable("listId") int listId,
            @RequestBody Card c) {
        try {
//            permissionService.requireListPermission(listId);

            this.cardService.createCardInList(listId, c);

            ResCardDTO dto = DTOMapper.toCardDTO(c);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/cards/{cardId}")
    @PreAuthorize("@securityCustom.canAccessCard(authentication.name, #cardId)")
    public ResponseEntity<?> updateCard(@PathVariable("cardId") int cardId, @RequestBody Card c) {
        try {
//            permissionService.requireCardPermission(cardId);

            c.setId(cardId);
            this.cardService.addOrUpdate(c);

            Card updatedCard = this.cardService.getById(cardId);

            ResCardDTO dto = DTOMapper.toCardDTO(updatedCard);
            return new ResponseEntity<>(dto, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/cards/{cardId}")
    @PreAuthorize("@securityCustom.canAccessCard(authentication.name, #cardId)")
    public ResponseEntity<?> deleteCard(@PathVariable("cardId") int cardId) {
        try {
//            permissionService.requireCardPermission(cardId);

            this.cardService.delete(cardId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/cards/{cardId}/move")
    @PreAuthorize("@securityCustom.canAccessCard(authentication.name, #cardId) and @securityCustom.canAccessList(authentication.name, #payload.get('newListId'))")
    public ResponseEntity<?> moveCard(
            @PathVariable("cardId") int cardId,
            @RequestBody Map<String, Integer> payload) {
        try {
            int newListId = payload.get("newListId");
            int newPosition = payload.get("newPosition");

//            permissionService.requireCardPermission(cardId);
//            permissionService.requireListPermission(newListId);
            this.cardService.moveCard(cardId, newListId, newPosition);
            Card updateCard = this.cardService.getById(cardId);

            ResCardDTO dto = DTOMapper.toCardDTO(updateCard);
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
