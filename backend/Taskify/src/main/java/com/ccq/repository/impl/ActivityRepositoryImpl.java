/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository.impl;

import com.ccq.pojo.Activity;
import com.ccq.repository.ActivityRepository;
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
public class ActivityRepositoryImpl implements ActivityRepository{
    @Autowired
    private LocalSessionFactoryBean factory;
    
    
    @Override
    public void assignUserForCard(Activity ac) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(ac);
    }
    
    @Override
    public boolean isUserInCard(int userId, int cardId) {
        Session s = this.factory.getObject().getCurrentSession();
        String sql = "from Activity ac " + "where ac.userId.id = :userId and ac.cardId.id = :cardId";
        Query q = s.createQuery(sql, Activity.class);
        q.setParameter("userId", userId);
        q.setParameter("cardId", cardId);
        return q.uniqueResult() != null;
    }
    
}
