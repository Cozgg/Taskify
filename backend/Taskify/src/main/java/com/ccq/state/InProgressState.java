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
public class InProgressState implements CardState {

    @Override
    public void applyBehavior(Card card) {
        System.out.println("Thẻ đang IN_PROGRESS, gửi thông báo team!");
    }

    @Override
    public ListStatus getStatus() {
        return ListStatus.IN_PROGRESS;
    }

}
