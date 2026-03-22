/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service.impl;

import com.ccq.pojo.Board;
import com.ccq.repository.BoardRepository;
import com.ccq.service.BoardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author nguye
 */
@Service
public class BoardServiceImpl implements BoardService{

    @Autowired
    private BoardRepository boardRepo;
    
    @Override
    public Board getById(int id) {
        return this.boardRepo.getById(id);
    }

    @Override
    public void addOrUpdate(Board b) {
        this.boardRepo.addOrUpdate(b);
    }

    @Override
    public void delete(int id) {
        this.boardRepo.delete(id);
    }
    
}
