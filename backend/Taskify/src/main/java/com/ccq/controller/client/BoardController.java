/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.controller.client;

import com.ccq.dto.BoardDTO;
import com.ccq.dto.request.BoardRequestDTO;
import com.ccq.pojo.Board;
import com.ccq.service.BoardService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author nguye
 */
@RestController
@RequestMapping("/api")
public class BoardController {

    @Autowired
    private BoardService boardService;

    //da test, chua phan quyen
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<?> getBoardById(@PathVariable("boardId") int boardId) {
        Board board = this.boardService.getById(boardId);
        if (board != null) {
            BoardDTO bdto = new BoardDTO(board.getId(), board.getName(), board.getCreatedDate(), board.getIsPublic());
            return new ResponseEntity<>(bdto, HttpStatus.OK);
        }
        return new ResponseEntity<>("Không tìm thấy Bảng", HttpStatus.NOT_FOUND);
    }

    //da test, chua phan quyen
    @PostMapping("/workspaces/{workspaceId}/boards")
    public ResponseEntity<?> createBoard(
            @PathVariable("workspaceId") int workspaceId,
            @RequestBody BoardRequestDTO req) {
        try {
            Board board = new Board();
            board.setName(req.getName());
            board.setIsPublic(req.getIsPublic());
            Board b = this.boardService.createBoardInWorkspace(workspaceId, board);
            BoardDTO bdto = new BoardDTO(b.getId(), b.getName(), b.getCreatedDate(), b.getIsPublic(), b.getWorkspaceId().getId());
            return new ResponseEntity<>(bdto, HttpStatus.CREATED); // 201
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi tạo bảng: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    //da test, chua phan quyen
    @PutMapping("/boards/{boardId}")
    public ResponseEntity<?> updateBoard(
            @PathVariable("boardId") int boardId,
            @RequestBody BoardRequestDTO req) {
        try {
            Board board = this.boardService.getById(boardId);
            if (board == null) {
                return new ResponseEntity<>("Không tìm thấy Board với ID: " + boardId, HttpStatus.NOT_FOUND);
            }
            board.setName(req.getName());
            board.setIsPublic(req.getIsPublic());
            this.boardService.addOrUpdate(board);
            BoardDTO bdto = new BoardDTO(board.getId(), board.getName(), board.getCreatedDate(), board.getIsPublic(), board.getWorkspaceId().getId());
            return new ResponseEntity<>(bdto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi cập nhật bảng: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //da test, chua phan quyen
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable("boardId") int boardId) {
        try {
            this.boardService.delete(boardId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi xóa bảng: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
