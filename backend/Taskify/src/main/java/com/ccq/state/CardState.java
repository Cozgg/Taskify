/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.state;

import com.ccq.pojo.Card;
import com.ccq.pojo.ListStatus;

/**
 *
 * @author Admin
 */
public interface CardState {
    String applyBehavior(Card card); 
    ListStatus getStatus();
}
