/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.controller.client;
import com.ccq.pojo.Board;
import com.ccq.service.BoardService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccq.pojo.Board;
import com.ccq.service.BoardService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 * @author nguye
 */
@RestController
@RequestMapping("/api")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/workspaces/{workspaceId}/boards")
    public ResponseEntity<?> getBoardsByWorkspace(
            @PathVariable("workspaceId") int workspaceId,
            @RequestParam Map<String, String> params) {

        params.put("workspaceId", String.valueOf(workspaceId));

        List<Board> boards = this.boardService.getBoards(params);
        return new ResponseEntity<>(boards, HttpStatus.OK);
    }

    @GetMapping("/boards/{boardId}")
    @PreAuthorize("@securityCustom.canAccessBoard(authentication.name, #boardId)")
    public ResponseEntity<?> getBoardById(@PathVariable("boardId") int boardId) {
        Board board = this.boardService.getById(boardId);
        if (board != null) {
            return new ResponseEntity<>(board, HttpStatus.OK);
        }
        return new ResponseEntity<>("Không tìm thấy Bảng", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/workspaces/{workspaceId}/boards")
    public ResponseEntity<?> createBoard(
            @PathVariable("workspaceId") int workspaceId,
            @RequestBody Board board) {
        try {
            this.boardService.createBoardInWorkspace(workspaceId, board);
            return new ResponseEntity<>(board, HttpStatus.CREATED); // 201
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi tạo bảng: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/boards/{boardId}")
    @PreAuthorize("@securityCustom.canAccessBoard(authentication.name, #boardId)")
    public ResponseEntity<?> updateBoard(
            @PathVariable("boardId") int boardId,
            @RequestBody Board board) {
        try {
            board.setId(boardId);
            this.boardService.addOrUpdate(board);
            return new ResponseEntity<>(board, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi cập nhật bảng: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    //da test, chua phan quyen
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/boards/{boardId}")
    @PreAuthorize("@securityCustom.canAccessBoard(authentication.name, #boardId)")
    public ResponseEntity<?> deleteBoard(@PathVariable("boardId") int boardId) {
        try {
            this.boardService.delete(boardId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi xóa bảng: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
