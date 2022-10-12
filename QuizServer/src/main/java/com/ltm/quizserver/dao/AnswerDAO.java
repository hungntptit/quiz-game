
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ltm.quizserver.dao;

import com.ltm.model.Answer;
import java.util.List;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author hung
 */
public class AnswerDAO {

    public AnswerDAO() {
        super();
    }

    public List<Answer> getAnswers() {
        try ( Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Answer").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Answer> getAnswerByTitle(String key) {
        try ( Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("from Answer where title like :key", Answer.class);
            query.setParameter("key", key);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addAnswer(Answer answer) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the user object
            session.save(answer);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAnswer(Answer answer) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the user object
            session.update(answer);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAnswer(Answer answer) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the user object
            session.delete(answer);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AnswerDAO answerDAO = new AnswerDAO();
        List<Answer> answerzes = answerDAO.getAnswers();
        int i = 0;
        for (Answer q : answerzes) {
            System.out.println(q.getContent());
            q.setContent(q.getContent() + "123");
//            if (i == 0) {
//                answerDAO.deleteAnswer(q);
//            }
            i++;
        }
    }
}
