/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service;

import com.ccq.pojo.response.ResAttachmentDTO;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Admin
 */
public interface AttachmentService {
    ResAttachmentDTO addFile(int cardId, Map<String, String> params, MultipartFile file);
    void deleteFile(int id);
    List<ResAttachmentDTO> getAttachments();
    List<ResAttachmentDTO> getAttachments(int cardId);
    
}
