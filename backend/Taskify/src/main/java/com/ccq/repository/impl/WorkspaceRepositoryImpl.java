/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.repository.impl;

import com.ccq.pojo.UserWorkspace;
import com.ccq.pojo.Workspace;
import com.ccq.repository.WorkspaceRepository;
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
public class WorkspaceRepositoryImpl implements WorkspaceRepository{
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public Workspace findWorkspaceById(int workspaceId) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Workspace.class, workspaceId);
    }


}
