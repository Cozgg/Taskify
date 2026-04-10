/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository.impl;

import com.ccq.pojo.Comment;
import com.ccq.repository.CommentRepository;
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
public class CommentRepositoryImpl implements CommentRepository{
    
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
    public boolean isCommentOwner(int commentId, String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Long> q = s.createQuery(
                "SELECT COUNT(c.id) FROM Comment c WHERE c.id = :cId AND c.userId.username = :username", Long.class);
        q.setParameter("cId", commentId);
        q.setParameter("username", username);

        return q.getSingleResult()> 0;
    }

    @Override
    public boolean isWorkspaceAdminOfThisComment(int commentId, String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Long> q = s.createQuery("select count(c.id) from Comment c " + "where c.id = :cId " + 
                "and c.cardId.listId.boardId.workspaceId.ownerId.username = :username", Long.class);
        q.setParameter("cId", commentId);
        q.setParameter("username", username);
        return q.getSingleResult() > 0;
    }
    
    
}
