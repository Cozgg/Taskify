package com.ccq.controller.client;

import com.ccq.pojo.Board;
import com.ccq.pojo.response.ResBoardDTO;
import com.ccq.service.BoardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardControllerTest {

    @Mock
    private BoardService boardService;

    @InjectMocks
    private BoardController boardController;

    @Test
    void getBoardById_existingBoard_returnsBoard() {
        ResBoardDTO dto = new ResBoardDTO();
        dto.setId(1);
        dto.setName("Board Name");
        when(boardService.getBoardDTOById(1)).thenReturn(dto);

        ResponseEntity<?> response = boardController.getBoardById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void getBoardById_nonExistingBoard_returnsNotFound() {
        when(boardService.getBoardDTOById(99)).thenReturn(null);

        ResponseEntity<?> response = boardController.getBoardById(99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createBoard_validInput_returnsCreated() {
        Board board = new Board();
        board.setName("New Board");

        ResponseEntity<?> response = boardController.createBoard(1, board);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(boardService).createBoardInWorkspace(1, board);
    }
}
