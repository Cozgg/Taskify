/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service;

import java.util.Map;

import com.ccq.pojo.Boardlist;

/**
 *
 * @author nguye
 */
public interface ListService {

    Boardlist getById(int id);

    void addOrUpdate(Boardlist l);

    void delete(int id);

    java.util.List<Boardlist> getList(Map<String, String> params);

    void createListInBoard(int boardId, Boardlist list);
}
