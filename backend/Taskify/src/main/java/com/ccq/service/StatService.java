/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service;

import java.util.List;

/**
 *
 * @author Admin
 */
public interface StatService {
    List<Object[]> getBoardProgressStats(int id);
    List<Object[]> getMemberProgress(int id);
}
