/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.google.gson.Gson;
import common.Question;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class ServerThread implements Runnable {

    private Socket socketOfServer;
    private int clientNumber;
    private BufferedReader is;
    private BufferedWriter os;
    private boolean isClosed;
    private boolean isCompleted;
    private Map<Integer, Question> questions;
    private int point;

    public BufferedReader getIs() {
        return is;
    }

    public BufferedWriter getOs() {
        return os;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public ServerThread(Socket socketOfServer, int clientNumber, Map<Integer, Question> questions) {
        this.socketOfServer = socketOfServer;
        this.clientNumber = clientNumber;
        System.out.println("Server thread number " + clientNumber + " Started");
        isClosed = false;
        isCompleted = false;
        this.questions = questions;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getQuestionCount() {
        return questions.size();
    }

    @Override
    public void run() {
        try {
            // Mở luồng vào ra trên Socket tại Server.
            is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream(), "UTF-8"));
            os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream(), "UTF-8"));
            System.out.println("Khời động luồng mới thành công, ID là: " + clientNumber);
            write("get-id" + "," + this.clientNumber);
            Server.serverThreadBus.sendOnlineList();
            Server.serverThreadBus.broadCast("global-message" + "," + "---Client " + this.clientNumber + " đã đăng nhập---");
            String message;
            while (!isClosed) {
                message = is.readLine();
                System.out.println("message: " + message);
                if (message == null) {
                    break;
                }
                String[] messageSplit = message.split(",");
                if (messageSplit[0].equals("send-to-global")) {
                    Server.serverThreadBus.multiCast(this.getClientNumber(), "global-message" + "," + "Client " + messageSplit[2] + ": " + messageSplit[1]);
                }
                if (messageSplit[0].equals("send-to-person")) {
                    Server.serverThreadBus.sendMessageToPersion(Integer.parseInt(messageSplit[3]), "Client " + messageSplit[2] + " (tới bạn): " + messageSplit[1]);
                }
                if (messageSplit[0].equals("start-quizz")) {
//                    Server.serverThreadBus.sendMessage(clientNumber, "list-questions");
                    Server.serverThreadBus.sendMessage(clientNumber, "list-questions" + "," + new Gson().toJson(questions));
                }
//                if (messageSplit[0].equals("send-list-question")) {
//                    Server.serverThreadBus.sendObjectToPerson(clientNumber, questions);
//                    Server.serverThreadBus.sendMessage(clientNumber, new Gson().toJson(questions));
//                    Server.serverThreadBus.sendMessage(clientNumber, "list-questions" + "," + new Gson().toJson(questions));
//                }
                if (messageSplit[0].equals("send-update-point")) {
                    Server.serverThreadBus.updatePoint(Integer.parseInt(messageSplit[1]), 1);
                    Server.serverThreadBus.sendUpdatePoint();
                }
                if (!isCompleted && messageSplit[0].equals("Write-file-update-point")) {
                    Server.serverThreadBus.writeFileUpdate(Integer.parseInt(messageSplit[1]));
                    isCompleted = true;
                }
                if (messageSplit[0].equals("disconnect") && Integer.parseInt(messageSplit[1]) == clientNumber) {
                    this.close();
                }
            }
        } catch (IOException e) {
            this.close();
        }
    }

    public void close() {
        isClosed = true;
        Server.serverThreadBus.remove(this.clientNumber);
        System.out.println(this.clientNumber + " đã thoát");
        Server.serverThreadBus.sendOnlineList();
        Server.serverThreadBus.broadCast("global-message" + "," + "---Client " + this.clientNumber + " đã thoát---");
    }

    public void write(String message) {
        try {
            os.write(message);
            os.newLine();
            os.flush();
        } catch (IOException ex) {
            System.out.println("Couldnt write message: " + message + " to client " + clientNumber + ".");
        }
    }

    public Socket getSocketOfServer() {
        return socketOfServer;
    }

    public void setSocketOfServer(Socket socketOfServer) {
        this.socketOfServer = socketOfServer;
    }

    public boolean isIsClosed() {
        return isClosed;
    }

    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public Map<Integer, Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<Integer, Question> questions) {
        this.questions = questions;
    }

}
