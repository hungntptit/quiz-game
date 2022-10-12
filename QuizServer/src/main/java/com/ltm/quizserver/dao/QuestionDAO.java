
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ltm.quizserver.dao;

import com.ltm.model.Question;
import java.util.List;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author hung
 */
public class QuestionDAO {

    public QuestionDAO() {
        super();
    }

    public List<Question> getQuestions() {
        try ( Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Question").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Question> getQuestionByTitle(String key) {
        try ( Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("from Question where title like :key", Question.class);
            query.setParameter("key", key);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addQuestion(Question question) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the user object
            session.save(question);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateQuestion(Question question) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the user object
            session.update(question);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteQuestion(Question question) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the user object
            session.delete(question);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        QuestionDAO questionDAO = new QuestionDAO();
        List<Question> questionzes = questionDAO.getQuestions();
        int i = 0;
        for (Question q : questionzes) {
            System.out.println(q.getContent());
            q.setContent(q.getContent()+ "123");
//            if (i == 0) {
//                questionDAO.deleteQuestion(q);
//            }
            i++;
        }
    }
}
