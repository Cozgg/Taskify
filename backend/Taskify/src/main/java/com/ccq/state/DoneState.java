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
public class DoneState implements CardState {

    @Override
    public void applyBehavior(Card card) {
        System.out.println("Thẻ đã DONE, cập nhật tiến độ!");
    }

    @Override
    public ListStatus getStatus() {
        return ListStatus.DONE;
    }

}
