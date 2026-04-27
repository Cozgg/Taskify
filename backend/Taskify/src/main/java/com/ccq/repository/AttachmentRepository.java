/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository;

import com.ccq.pojo.Attachment;
import com.ccq.pojo.response.ResAttachmentDTO;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface AttachmentRepository {
    void addFile(Attachment a);
    Attachment getAttachById(int id);
    void deleteFile(int id);
    List<ResAttachmentDTO> getAttachments();
    List<ResAttachmentDTO> getAttachments(int cardId);
}
