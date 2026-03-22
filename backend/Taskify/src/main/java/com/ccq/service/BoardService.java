/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service;

import com.ccq.pojo.Board;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nguye
 */
public interface BoardService {
    Board getById(int id);
    void addOrUpdate(Board b);
    void delete(int id);
    List<Board> getBoards(Map<String, String> params);
    
    void createBoardInWorkspace(int workspaceId, Board board);
}
