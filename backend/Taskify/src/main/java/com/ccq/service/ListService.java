/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service;

import com.ccq.pojo.List;
import java.util.Map;

/**
 *
 * @author nguye
 */
public interface ListService {
    List getById(int id);
    void addOrUpdate(List l);
    void delete(int id);
    java.util.List<List> getList(Map<String, String> params);
}
