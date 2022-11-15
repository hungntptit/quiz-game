/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import common.Question;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public class FileIO {

    public static Map<Integer, Question> getListQuestion(String fileName) throws FileNotFoundException, IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        Map<Integer, Question> questions = new HashMap<>();
        String line = null;
        int count = 1;
        while ((line = bufferedReader.readLine()) != null) {
            String question = line;
            String option1 = bufferedReader.readLine().trim();
            String option2 = bufferedReader.readLine().trim();
            String option3 = bufferedReader.readLine().trim();
            String option4 = bufferedReader.readLine().trim();
            String answer = bufferedReader.readLine().trim();
            Question q = new Question();
            q.id = count;
            q.setQuestion(question);
            q.setCorrectAnswer(answer.trim().charAt(0));
            q.setOptions(new String[]{option1, option2, option3, option4});
            questions.put(count++, q);
        }
        return questions;
    }

}
