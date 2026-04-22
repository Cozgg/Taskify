/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.ccq.repository.impl;

import com.ccq.pojo.User;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaOrder;
import org.hibernate.query.criteria.JpaPath;
import org.hibernate.query.criteria.JpaPredicate;
import org.hibernate.query.criteria.JpaRoot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import static org.mockito.Mockito.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.SessionFactoryUtils;

/**
 *
 * @author Admin
 */
@ExtendWith(MockitoExtension.class)
public class UserRepositoryImplTest {

    @Mock
    private LocalSessionFactoryBean factory;
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Query<User> query;

    @InjectMocks
    private UserRepositoryImpl userRepository;

    @Mock
    private Environment env;

    @Mock
    private HibernateCriteriaBuilder b;
    @Mock
    private JpaCriteriaQuery<User> cq;

    @Mock
    private JpaRoot<User> root;

    @Mock
    private JpaPredicate predicate;

    @Mock
    private JpaOrder order;

    @Mock
    private JpaPath<Object> path;

    public UserRepositoryImplTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of findUserById method, of class UserRepositoryImpl.
     */
//    @Test
//    public void testFindUserById() {
//        System.out.println("findUserById");
//        int id = 0;
//        UserRepositoryImpl instance = new UserRepositoryImpl();
//        User expResult = null;
//        User result = instance.findUserById(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of getUserByUsername method, of class UserRepositoryImpl.
     */
    @Test
    public void testGetUserByUsername() {
        String testUsername = "test";
        User mockUser = new User();
        mockUser.setUsername(testUsername);

        when(factory.getObject()).thenReturn(sessionFactory);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createNamedQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(mockUser);

        User result = this.userRepository.getUserByUsername(testUsername);
        assertNotNull(result, "User không được null");
        assertEquals(testUsername, result.getUsername(), "Username phải khớp nhau");
        verify(query).setParameter(eq("username"), eq(testUsername));

    }

    /**
     * Test of addOrUpdateUser method, of class UserRepositoryImpl.
     */
    @Test
    public void testAddUser() {
        String testUsername = "test";
        User mockUser = new User();
        mockUser.setUsername(testUsername);

        when(factory.getObject()).thenReturn(sessionFactory);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        this.userRepository.addOrUpdateUser(mockUser);

        verify(session, times(1)).persist(mockUser);
    }

    @Test
    public void testUpdateUser() {
        String testUsername = "test";
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername(testUsername);

        when(factory.getObject()).thenReturn(sessionFactory);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        this.userRepository.addOrUpdateUser(mockUser);

        verify(session, times(1)).merge(mockUser);
    }

    /**
     * Test of deleteUser method, of class UserRepositoryImpl.
     */
    @Test
    public void testDeleteUser() {
        int testId = 1;
        User mockUser = new User();
        mockUser.setId(testId);

        when(factory.getObject()).thenReturn(sessionFactory);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.get(User.class, testId)).thenReturn(mockUser);

        this.userRepository.deleteUser(testId);
        verify(session, times(1)).remove(mockUser);

    }

    /**
     * Test of findUserByEmail method, of class UserRepositoryImpl.
     */
    @Test
    public void testFindUserByEmail() {
        String testEmail = "test@gmail.com";
        User mockUser = new User();
        mockUser.setEmail(testEmail);

        when(factory.getObject()).thenReturn(sessionFactory);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createNamedQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(mockUser);

        User result = userRepository.findUserByEmail(testEmail);

        assertNotNull(result);
        assertEquals(testEmail, result.getEmail());
        verify(query).setParameter(eq("email"), eq(testEmail));
    }

    /**
     * Test of getUsers method, of class UserRepositoryImpl.
     */
    @Test
    public void testGetUsers() {
        // 1. Chuẩn bị dữ liệu
        Map<String, String> params = new HashMap<>();
        params.put("kw", "can");
        params.put("page", "1");
        List<User> mockList = Arrays.asList(
//                new User(1, "Canh", "123", "canh@gmail.com"),
//                new User(2, "Test", "123", "test@gmail.com")
                new User()
        );

        when(factory.getObject()).thenReturn(sessionFactory);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.getCriteriaBuilder()).thenReturn(b);
        when(b.createQuery(User.class)).thenReturn(cq);
        when(cq.from(User.class)).thenReturn(root);
        when(cq.select(root)).thenReturn(cq);

        when(root.get(anyString())).thenReturn(path);

        when(b.like(any(), anyString())).thenReturn(predicate);
        when(b.or(any(JpaPredicate.class), any(JpaPredicate.class))).thenReturn(predicate);

        when(cq.where(any(jakarta.persistence.criteria.Predicate[].class))).thenReturn(cq);

        when(b.desc(any(jakarta.persistence.criteria.Expression.class))).thenReturn(order);
        when(cq.orderBy(any(JpaOrder.class))).thenReturn(cq);

        when(session.createQuery(cq)).thenReturn(query);
        when(env.getProperty(eq("user.page_size"), anyString())).thenReturn("10");
        when(query.setFirstResult(anyInt())).thenReturn(query);
        when(query.setMaxResults(anyInt())).thenReturn(query);
        when(query.getResultList()).thenReturn(mockList);

        List<User> result = userRepository.getUsers(params);

        assertNotNull(result);
        assertEquals(1, result.size()); 
        verify(query).setFirstResult(0);
        verify(query).setMaxResults(10);
        verify(query).getResultList();
    }

    /**
     * Test of existEmail method, of class UserRepositoryImpl.
     */
//    @Test
//    public void testExistEmail() {
//        System.out.println("existEmail");
//        String email = "";
//        UserRepositoryImpl instance = new UserRepositoryImpl();
//        boolean expResult = false;
//        boolean result = instance.existEmail(email);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of count method, of class UserRepositoryImpl.
//     */
//    @Test
//    public void testCount() {
//        System.out.println("count");
//        UserRepositoryImpl instance = new UserRepositoryImpl();
//        Long expResult = null;
//        Long result = instance.count();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}
