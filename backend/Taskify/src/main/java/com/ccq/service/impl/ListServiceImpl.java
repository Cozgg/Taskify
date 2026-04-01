/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service.impl;

import com.ccq.pojo.Board;
import com.ccq.pojo.Boardlist;
import com.ccq.repository.BoardRepository;
import com.ccq.repository.ListRepository;
import com.ccq.service.ListService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author nguye
 */
@Service
public class ListServiceImpl implements ListService{

    @Autowired
    private ListRepository listRepo;
    
    @Autowired
    private BoardRepository boardRepo;
    
    @Override
    public Boardlist getById(int id) {
        return this.listRepo.getById(id);
    }

    @Override
    public void addOrUpdate(Boardlist l) {
        this.listRepo.addOrUpdate(l);
    }

    @Override
    public void delete(int id) {
        this.listRepo.delete(id);
    }

    @Override
    public java.util.List<Boardlist> getList(Map<String, String> params) {
        return this.listRepo.getList(params);
    }

    @Override
    public void createListInBoard(int boardId, Boardlist list) {
        Board board = this.boardRepo.getById(boardId);
        
        if(board == null){
            throw new RuntimeException("Không tìm thấy bảng với id: " + boardId);
        }
        
        list.setBoardId(board);
        this.listRepo.addOrUpdate(list);
    }
        
}
