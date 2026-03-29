/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service;

import com.ccq.pojo.Activity;

/**
 *
 * @author Admin
 */
public interface ActivityService {
    Activity assignUserForCard(int userId, int cardId);
}
