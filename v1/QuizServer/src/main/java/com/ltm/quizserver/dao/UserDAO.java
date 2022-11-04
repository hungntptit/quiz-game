/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ltm.quizserver.dao;

import com.ltm.model.User;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author hung
 */
public class UserDAO {

    public UserDAO() {
    }

    public List<User> getUsers() {
        try ( Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveUser(User user) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the user object
            session.save(user);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public User checkLogin(User u) {
        try ( Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("checking login");
            Query query = session.createQuery("from User where username = :u and password = :p", User.class);
            query.setParameter("u", u.getUsername());
            query.setParameter("p", u.getPassword());
            List<User> users = (List<User>) query.getResultList();
            if (!users.isEmpty()) {
                u.setId(users.get(0).getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("checked login");
        return u;

    }

    public static void main(String[] args) {

//        User u = new UserDAO().checkLogin(new User("Editor 101", "123"));
//        if (u != null) {
//            System.out.println(u.getId());
//        } else {
//            System.out.println(u);
//        }
        List<User> users = new UserDAO().getUsers();
        for (User u1 : users) {
            System.out.println(u1.getUsername());
        }
    }
}
