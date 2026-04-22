/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository;

import java.util.List;
import java.util.Map;

import com.ccq.pojo.Boardlist;

/**
 *
 * @author nguye
 */
public interface ListRepository {

    Boardlist getById(int id);

    void addOrUpdate(Boardlist l);

    void delete(int id);

    List<Boardlist> getList(Map<String, String> params);
}
