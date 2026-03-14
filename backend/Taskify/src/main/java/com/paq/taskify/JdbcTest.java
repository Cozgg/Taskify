package com.paq.taskify;

import com.paq.pojo.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JdbcTest {
    public static void main(String[] args) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.paq_Taskify_jar_1.0-SNAPSHOTPU");
            EntityManager em = emf.createEntityManager();
            
            em.getTransaction().begin();
            User newUser = new User();
            newUser.setUsername("phamquyendz");
            newUser.setPassword("123456");  
            em.persist(newUser);
            em.getTransaction().commit();
            System.out.println("Da them thanh cong nguoi dung thu nghiem!");
            
            if (em.isOpen()) {
                System.out.println("Chuc mung ban! Da ket noi toi TiDB thanh cong");
                long count = (long) em.createQuery("SELECT count(u) FROM User u").getSingleResult();
                System.out.println("So luong nguoi dung hien tai: " + count);
            }
            
            em.close();
            emf.close();
        } catch (Exception e) {
            System.err.println("That bai!!!");
            e.printStackTrace();
        }
    }
}