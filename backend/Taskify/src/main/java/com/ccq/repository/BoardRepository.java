/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository;

import com.ccq.pojo.Board;

/**
 *
 * @author nguye
 */
public interface BoardRepository {
    Board getById(int id);
    void addOrUpdate(Board b);
    void delete(int id);
    
}