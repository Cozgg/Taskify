/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service.impl;

import com.ccq.repository.StatRepository;
import com.ccq.service.StatService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */

@Service
public class StatServiceImpl implements StatService {
    
    @Autowired
    private StatRepository statRepo;
    
    @Override
    public List<Object[]> getBoardProgressStats(int id) {
        return this.statRepo.getBoardProgressStats(id);
    }

    @Override
    public List<Object[]> getMemberProgress(int id) {
        return this.getMemberProgress(id);
    }
    
}
