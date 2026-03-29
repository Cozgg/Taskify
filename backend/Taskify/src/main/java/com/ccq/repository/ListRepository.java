/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository;

import com.ccq.pojo.Boardlist;
import java.util.Map;


/**
 *
 * @author nguye
 */
public interface ListRepository {
    Boardlist getById(int id);
    void addOrUpdate(Boardlist l);
    void delete(int id);
    java.util.List<Boardlist> getList(Map<String, String> params);
}
