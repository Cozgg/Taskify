/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository.impl;

import com.ccq.pojo.Attachment;
import com.ccq.repository.AttachmentRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Admin
 */

@Repository
@Transactional
public class AttachmentRepositoryImpl implements AttachmentRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public void addFile(Attachment a) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(a);
    }

    @Override
    public Attachment getAttachById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Attachment.class, id);
    }

    @Override
    public void deleteFile(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Attachment a = this.getAttachById(id);
        if (a != null)
            s.remove(a);
    }
    
}
