/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository.impl;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ccq.pojo.Comment;
import com.ccq.pojo.response.ResCommentDTO;
import com.ccq.repository.CommentRepository;
import com.ccq.utils.DTOMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.query.Query;

/**
 *
 * @author Admin
 */
@Repository
@Transactional
public class CommentRepositoryImpl implements CommentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void addComment(Comment c) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(c);
    }

    @Override
    public void deleteComment(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Comment c = this.getCommentById(id);
        s.remove(c);
    }

    @Override
    public Comment getCommentById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Comment.class, id);
    }

    @Override
    public List<ResCommentDTO> getComments(int cardId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Comment> q = b.createQuery(Comment.class);

        Root<Comment> root = q.from(Comment.class);

        q.select(root);
        q.where(b.equal(root.get("cardId").get("id"), cardId));

        Query<Comment> query = s.createQuery(q);

        return query.getResultList()
                .stream()
                .map(DTOMapper::toCommentDTO)
                .collect(Collectors.toList());
    }
}
