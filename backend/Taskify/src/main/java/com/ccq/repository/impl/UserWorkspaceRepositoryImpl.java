/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository.impl;

import com.ccq.pojo.UserWorkspace;
import com.ccq.repository.UserWorkspaceRepository;
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
public class UserWorkspaceRepositoryImpl implements UserWorkspaceRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public void saveInviteUser(UserWorkspace uw) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(uw);
    }

    @Override
    public boolean isUserInWorkspace(int userId, int workspaceId) {
        Session s = this.factory.getObject().getCurrentSession();
        String sql = "from UserWorkspace uw " + "where uw.userId.id =: userId and uw.workspaceId.id =: workspaceId";
        Query q = s.createQuery(sql, UserWorkspace.class);
        q.setParameter("userId", userId);
        q.setParameter("workspaceId", workspaceId);
        return q.uniqueResult() != null;
    }
    
}
