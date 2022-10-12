/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ltm.quizserver.dao;

import com.ltm.model.Quiz;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author hung
 */
public class QuizDAO {

    public QuizDAO() {
        super();
    }

    public List<Quiz> getQuizzes() {
        try ( Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Quiz").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Quiz> getQuizByTitle(String key) {
        try ( Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("from Quiz where title like :key", Quiz.class);
            query.setParameter("key", key);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveOrUpdateQuiz(Quiz quiz) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the user object
            session.saveOrUpdate(quiz);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteQuiz(Quiz quiz) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the user object
            session.delete(quiz);
            System.out.println("delete quiz");
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        QuizDAO quizDAO = new QuizDAO();
//        List<Quiz> quizzes = quizDAO.getQuizzes();
//        quizzes.get(0).getQuestions().remove(0);
//        quizzes.remove(1);
//        for (Quiz q : quizzes) {
//            quizDAO.saveOrUpdateQuiz(q);
//        }
//        int i = 0;
        //        for (Quiz q : quizzes) {
        ////            System.out.println(q.getTitle());
        ////            q.setTitle(q.getTitle() + "123");
        //            if (i == 0) {
        //                quizDAO.deleteQuiz(q);
        //            }
        //            i++;
        //        }
        //        Quiz quiz = new Quiz("qweqweqwe", new ArrayList<Question>());
        //        List<Question> questions = new ArrayList<>();
        //        for (int i = 0; i < 10; i++) {
        //            Question question = new Question("question " + i, new ArrayList<Answer>());
        //            for (int j = 0; j < 4; j++) {
        //                question.getAnswers().add(new Answer("answer " + i + j));
        //            }
        //            questions.add(question);
        //        }
        //        quiz.setQuestions(questions);
        //        quizDAO.addQuiz(quiz);
    }
}
