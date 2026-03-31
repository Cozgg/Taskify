/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.controller.client;

import com.ccq.pojo.User;
import com.ccq.pojo.Boardlist;
import com.ccq.service.ListService;
import com.ccq.service.UserService;
import com.ccq.utils.JwtUtil;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize; 

/**
 *
 * @author nguye
 */
@RestController
@RequestMapping("/api") 
public class ListController {
    @Autowired
    private ListService listSer;
    
    @Autowired 
    private UserService userSer;
    
    @GetMapping("/boards/{boardId}/lists")
    public ResponseEntity<?> getLists(@PathVariable("boardId") int boardId, @RequestParam Map<String, String> params){
        params.put("boardId", String.valueOf(boardId));
        List<Boardlist> lists = this.listSer.getList(params);
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }
    
    @PostMapping("/boards/{boardId}/lists")
    public ResponseEntity<?> createList(
            @PathVariable("boardId") int boardId, 
            @RequestBody Boardlist list) {
        
        try {
            this.listSer.createListInBoard(boardId, list);
            return new ResponseEntity<>(list, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/lists/{listId}")
    public ResponseEntity<?> updateList(
            @PathVariable("listId") int listId, 
            @RequestBody Boardlist list) {
        
        try {
            list.setId(listId); 
            this.listSer.addOrUpdate(list);
            return new ResponseEntity<>(list, HttpStatus.OK); 
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/lists/{listId}")
    public ResponseEntity<?> deleteList(@PathVariable("listId") int listId) {
        try {
            this.listSer.delete(listId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}