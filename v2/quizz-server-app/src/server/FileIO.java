/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import common.Question;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public class FileIO {

    private String fileName;

    public FileIO(String fileName) {
        this.fileName = fileName;
    }

    public Map<Integer, Question> getListQuestion() throws IOException {
        Map<Integer, Question> questions = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
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
