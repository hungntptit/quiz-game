/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ltm.quizserver.view;

import com.ltm.model.Answer;
import com.ltm.model.Question;
import com.ltm.model.Quiz;
import com.ltm.quizserver.controller.ServerController;
import com.ltm.quizserver.dao.QuestionDAO;
import com.ltm.quizserver.dao.QuizDAO;
import com.ltm.quizserver.dao.UserDAO;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hung
 */
public class ServerMainFrm extends javax.swing.JFrame {

    private ServerController serverController;
    private JTabbedPane tabbedPane;
    private JPanel mainPanel;
    private JPanel quizPanel;

    // main panel components
    private JButton btnStart;
    private JButton btnStop;
    private JTextArea txtLog;
    private JLabel lblStatus;

    // quiz panel components
    // panel1 components
    private JScrollPane sp1;
    private DefaultTableModel tm1;
    private JTable tblQuiz;
    private JButton btnAddQuiz;
    private JButton btnDeleteQuiz;

    // panel2 components
    private JScrollPane sp2;
    private DefaultTableModel tm2;
    private JTable tblQuestion;
    private JButton btnAddQuestion;
    private JButton btnDeleteQuestion;

    // panel3 components
    private JLabel lblQuestion;
    private JLabel lblAnswer;
    private JTextField txtQuestion;
    private JTextField[] txtAnswers;
    private JRadioButton[] rbAnswers;
    private ButtonGroup bg;

    private JButton btnSave;
    private JButton btnReload;

    private JPanel quizPanel1;
    private JPanel quizPanel2;
    private JPanel quizPanel3;

    private UserDAO userDAO;
    private QuizDAO quizDAO;
    private QuestionDAO questionDAO;

    private List<Quiz> quizzes = new ArrayList<>();
    private List<Question> questions = new ArrayList<>();
    private List<Answer> answers = new ArrayList<>();

    /**
     * Creates new form ServerFrm
     */
    public ServerMainFrm() {
//        initComponents();
        userDAO = new UserDAO();
        quizDAO = new QuizDAO();
        questionDAO = new QuestionDAO();
        serverController = new ServerController();
        loadMainPanel();
        loadQuizPanel();
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Main", null, mainPanel, "Click to show main panel.");
//        tabbedPane.addTab("Rooms", null, new JPanel(new MigLayout()), "Click to show room manager panel.");
//        tabbedPane.addTab("Users", null, new JPanel(new MigLayout()), "Click to show user manager panel.");
        tabbedPane.addTab("Quizzes", null, quizPanel, "Click to show quiz manager panel.");
        this.setTitle("Quiz Server");
        this.setContentPane(tabbedPane);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setMinimumSize(this.getPreferredSize());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Windows look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Windows (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerMainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerMainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerMainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerMainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerMainFrm().setVisible(true);
            }
        });
    }

    private void loadMainPanel() {
        mainPanel = new JPanel(new MigLayout("", "[100][200:300:400][400!][::400]", "[][][]"));
        btnStart = new JButton("Start server");
        btnStop = new JButton("Stop server");
        lblStatus = new JLabel("Server stopped.");
        btnStart.addActionListener((ActionEvent e) -> {
            serverController.startServer();
            lblStatus.setText("Server is running...");
        });
        btnStop.addActionListener((ActionEvent e) -> {
            serverController.stopServer();
            lblStatus.setText("Server stopped.");
        });
        txtLog = new JTextArea();
        txtLog.setRows(20);
        txtLog.setLineWrap(true);
        txtLog.setWrapStyleWord(true);
        JScrollPane areaScrollPane = new JScrollPane(txtLog);
        areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainPanel.add(btnStart, "split3, h 40");
        mainPanel.add(btnStop, "h 40");
        mainPanel.add(lblStatus, "wrap");
//        mainPanel.add(areaScrollPane, "span,push,grow");
    }

    private void loadQuizPanel() {
        quizPanel = new JPanel(new MigLayout());
        loadQuizPanel1();
        loadQuizPanel2();
        loadQuizPanel3();
        quizPanel.add(quizPanel1, "push, grow");
        quizPanel.add(quizPanel2, "push, grow, wrap");
        quizPanel.add(quizPanel3, "pushx, growx, span, wrap");
        btnSave = new JButton("Save");
        btnReload = new JButton("Reload");
        btnSave.addActionListener((ActionEvent e) -> {
            int dialogResult = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to save all changes? You cannot undo this step.");
            if (dialogResult == JOptionPane.YES_OPTION) {
                int questionRow = tblQuestion.getSelectedRow();
                if (questionRow >= 0) {
                    answers.clear();
                    for (int i = 0; i < 4; i++) {
                        answers.add(new Answer(txtAnswers[i].getText(), rbAnswers[i].isSelected()));
                    }
                    questions.get(questionRow).setAnswers(answers);
                }
                int quizRow = tblQuiz.getSelectedRow();
                if (quizRow >= 0) {
                    quizzes.get(quizRow).setQuestions(questions);
                }
                for (Quiz q : quizzes) {
                    quizDAO.saveOrUpdateQuiz(q);
                }
            }
        });
        btnReload.addActionListener((ActionEvent e) -> {
            loadTblQuiz();
            loadTblQuestion(-1);
            loadAnswers(-1);
        });
        quizPanel.add(btnSave, "al right");
        quizPanel.add(btnReload, "");
    }

    private void loadQuizPanel1() {
        loadTblQuiz();
        quizPanel1 = new JPanel(new MigLayout());
        quizPanel1.setBorder(BorderFactory.createTitledBorder("Quizzes"));
        btnAddQuiz = new JButton("Add Quiz");
        btnDeleteQuiz = new JButton("Delete Quiz");
        btnAddQuiz.addActionListener((ActionEvent e) -> {
            tm1.addRow(new Object[]{""});
            quizzes.add(new Quiz("", new ArrayList<>()));
        });
        btnDeleteQuiz.addActionListener((ActionEvent e) -> {
            int r = tblQuiz.getSelectedRow();
            if (r >= 0) {
                int dialogResult = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to delete this quiz?");
                if (dialogResult == JOptionPane.YES_OPTION) {
                    quizDAO.deleteQuiz(quizzes.get(r));
                    tm1.removeRow(r);
                    quizzes.remove(r);
                }
            }
        });
        sp1 = new JScrollPane(tblQuiz);
        quizPanel1.add(btnDeleteQuiz, "split2, al right");
        quizPanel1.add(btnAddQuiz, "wrap");
        quizPanel1.add(sp1, "push, grow, span");
    }

    private void loadQuizPanel2() {
        loadTblQuestion(-1);
        quizPanel2 = new JPanel(new MigLayout());
        quizPanel2.setBorder(BorderFactory.createTitledBorder("Questions"));
        btnAddQuestion = new JButton("Add Question");
        btnDeleteQuestion = new JButton("Delete Question");
        btnAddQuestion.addActionListener((ActionEvent e) -> {
            tm2.addRow(new Object[]{""});
            questions.add(new Question("", new ArrayList<>()));
        });
        btnDeleteQuestion.addActionListener((ActionEvent e) -> {
            int r = tblQuestion.getSelectedRow();
            if (r >= 0) {
                int dialogResult = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to delete this question? You cannot undo this.");
                if (dialogResult == JOptionPane.YES_OPTION) {
                    questionDAO.deleteQuestion(questions.get(r));
                    tm2.removeRow(r);
                    questions.remove(r);
                }
            }
        }
        );
        sp2 = new JScrollPane(tblQuestion);
        quizPanel2.add(btnDeleteQuestion, "split2, al right");
        quizPanel2.add(btnAddQuestion, "wrap");
        quizPanel2.add(sp2, "push, grow");
    }

    private void loadQuizPanel3() {
        quizPanel3 = new JPanel(new MigLayout());
        quizPanel3.setBorder(BorderFactory.createTitledBorder("Answers"));
        lblQuestion = new JLabel("Question:");
        lblAnswer = new JLabel("Answers");
        quizPanel3.add(lblQuestion, "span, wrap");
        quizPanel3.add(lblAnswer, "span, wrap, al center");
        bg = new ButtonGroup();
        txtAnswers = new JTextField[4];
        rbAnswers = new JRadioButton[4];
        for (int i = 0; i < 4; i++) {
            txtAnswers[i] = new JTextField(20);
            rbAnswers[i] = new JRadioButton();
            bg.add(rbAnswers[i]);
            quizPanel3.add(rbAnswers[i], "flowx");
            quizPanel3.add(txtAnswers[i], "pushx, growx, wrap");
        }
    }

    public void showMessage(String s) {
        txtLog.append("\n" + s);
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }

    private void loadTblQuiz() {
        quizzes = quizDAO.getQuizzes();
        if (tm1 == null) {
            tm1 = new DefaultTableModel();
            String[] columns = {"Title"};
            tm1.setColumnIdentifiers(columns);
        }
        tm1.setRowCount(0);
        for (Quiz quiz : quizzes) {
            tm1.addRow(new Object[]{quiz.getTitle()});
        }
        if (tblQuiz == null) {
            tblQuiz = new JTable(tm1);
            tblQuiz.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tblQuiz.putClientProperty("terminateEditOnFocusLost", true);
            tblQuiz.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = tblQuiz.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        loadTblQuestion(row);
                        loadAnswers(-1);
                    }
                }
            });
            tblQuiz.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                int r = tblQuiz.getSelectedRow();
                if (r >= 0) {
                    quizzes.get(r).setTitle((String) tm1.getValueAt(r, 0));
                }
            });
        }
    }

    private void loadTblQuestion(int quizRow) {
        if (tm2 == null) {
            tm2 = new DefaultTableModel();
            String[] columns = {"Content"};
            tm2.setColumnIdentifiers(columns);
        }
        tm2.setRowCount(0);
        if (quizRow >= 0) {
            questions = quizzes.get(quizRow).getQuestions();
            for (Question question : questions) {
                tm2.addRow(new Object[]{question.getContent()});
            }
        }
        if (tblQuestion == null) {
            tblQuestion = new JTable(tm2);
            tblQuestion.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tblQuestion.putClientProperty("terminateEditOnFocusLost", true);
            tblQuestion.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = tblQuestion.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        loadAnswers(row);
                    }
                }
            });
            tblQuestion.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                int quizR = tblQuiz.getSelectedRow();
                int questionR = tblQuestion.getSelectedRow();
                if (quizR >= 0 && questionR >= 0) {
                    String changed = (String) tm2.getValueAt(questionR, 0);
                    questions.get(questionR).setContent(changed);
                    quizzes.get(quizR).setQuestions(questions);
                }
            });
        }
    }

    private void loadAnswers(int questionRow) {
//        System.out.println("question row " + questionRow);
        if (bg != null) {
            bg.clearSelection();
        }
        lblQuestion.setText("Question: ");
        for (int i = 0; i < 4; i++) {
            txtAnswers[i].setText("");
        }
        if (questionRow >= 0) {
            Question q = questions.get(questionRow);
            lblQuestion.setText("Question: " + q.getContent());
            answers = q.getAnswers();
//            System.out.println(answers.size());
            for (int i = 0; i < answers.size(); i++) {
                Answer answer = answers.get(i);
                if (answer.isCorrect()) {
                    rbAnswers[i].setSelected(true);
                }
                txtAnswers[i].setText(answer.getContent());
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
