/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository.impl;

import com.ccq.pojo.Attachment;
import com.ccq.pojo.response.ResAttachmentDTO;
import com.ccq.repository.AttachmentRepository;
import com.ccq.utils.DTOMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.hibernate.query.Query;
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
public class AttachmentRepositoryImpl implements AttachmentRepository {

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
        if (a != null) {
            s.remove(a);
        }
    }

    @Override
    public List<ResAttachmentDTO> getAttachments() {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Attachment> q = s.createNamedQuery("Attachment.findAll", Attachment.class);
        return q.getResultList()
                .stream()
                .map(DTOMapper::toAttachmentDTO)
                .toList();
    }

    @Override
    public List<ResAttachmentDTO> getAttachments(int cardId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Attachment> q = b.createQuery(Attachment.class);

        Root<Attachment> root = q.from(Attachment.class);

        q.select(root);
        q.where(b.equal(root.get("cardId").get("id"), cardId));

        Query<Attachment> query = s.createQuery(q);

        return query.getResultList()
                .stream()
                .map(DTOMapper::toAttachmentDTO)
                .collect(Collectors.toList());
    }

}
