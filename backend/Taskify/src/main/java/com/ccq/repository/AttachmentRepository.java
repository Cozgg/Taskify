/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository;

import com.ccq.pojo.Attachment;

/**
 *
 * @author Admin
 */
public interface AttachmentRepository {
    void addFile(Attachment a);
    Attachment getAttachById(int id);
    void deleteFile(int id);
}
