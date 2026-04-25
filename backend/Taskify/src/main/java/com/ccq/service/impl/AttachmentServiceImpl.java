/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.service.impl;

import com.ccq.pojo.Attachment;
import com.ccq.pojo.Card;
import com.ccq.pojo.response.ResAttachmentDTO;
import com.ccq.repository.AttachmentRepository;
import com.ccq.repository.CardRepository;
import com.ccq.service.AttachmentService;
import com.ccq.utils.DTOMapper;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Admin
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentRepository attachRepo;

    @Autowired
    private CardRepository cardRepo;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public ResAttachmentDTO addFile(int cardId, Map<String, String> params, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn file để tải lên");
        }
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif", "pdf", "xlsx", "xls", "docx", "doc", "txt", "zip");

        String fileExtension = "";
        if (originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        }

        if (!allowedExtensions.contains(fileExtension)) {
            throw new IllegalArgumentException("Định dạng file không được hỗ trợ! Chỉ cho phép: " + allowedExtensions.toString());
        }
        Card c = this.cardRepo.getById(cardId);
        if (c == null) {
            throw new IllegalArgumentException("Không tìm thấy card");
        }
        Attachment a = new Attachment();
        a.setUpdateDate(new Date());
        a.setFilename(params.get("name"));
        a.setCardId(c);
        String filename = file.getOriginalFilename();
        String fileNameWithoutExtension = filename.substring(0, filename.lastIndexOf("."));
        try {
            Map res = this.cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "public_id", "kanban/" + fileNameWithoutExtension + "_" + System.currentTimeMillis(), // Tạo folder và giữ tên file
                            "use_filename", true
                    ));
            a.setUrl(res.get("secure_url").toString());
        } catch (IOException ex) {
//            Logger.getLogger(AttachmentService.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Upload file thất bại");
        }

        this.attachRepo.addFile(a);
        return DTOMapper.toAttachmentDTO(a);
    }

    @Override
    public void deleteFile(int id) {
        Attachment a = this.attachRepo.getAttachById(id);
        if (a == null) {
            throw new IllegalArgumentException("Không tìm thấy file cần xóa");
        }
        this.attachRepo.deleteFile(id);
    }

}
