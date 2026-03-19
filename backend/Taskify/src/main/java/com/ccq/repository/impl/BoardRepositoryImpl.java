/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository.impl;

import com.ccq.pojo.Board;
import com.ccq.repository.BoardRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author nguye
 */
@Repository
@PropertySource("classpath:configs.properties")
@Transactional
public class BoardRepositoryImpl implements BoardRepository{

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;
    
     @Override
    public Board getById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Board.class, id);
    }

    @Override
    public void addOrUpdate(Board w) {
        Session s = this.factory.getObject().getCurrentSession();
        if (w.getId() != null) {
            s.merge(w);
        } else {
            s.persist(w);
        }
    }

    @Override
    public void delete(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Board w = this.getById(id);
        if (w != null) {
            s.remove(w);
        }
    }
}
