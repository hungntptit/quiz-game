/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import common.Question;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public ServerThread(Socket socketOfServer, int clientNumber, String questionPath) {
        this.socketOfServer = socketOfServer;
        this.clientNumber = clientNumber;
        System.out.println("Server thread number " + clientNumber + " Started");
        isClosed = false;
        try {
            questions = new FileIO(questionPath).getListQuestion();
        } catch (IOException ex) {
            System.out.println("Cannot open the file \"" + questionPath + "\"");
//            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
            System.out.println("Khời động luông mới thành công, ID là: " + clientNumber);
            write("get-id" + "," + this.clientNumber);
            Server.serverThreadBus.sendOnlineList();
            Server.serverThreadBus.mutilCastSend("global-message" + "," + "---Client " + this.clientNumber + " đã đăng nhập---");
            String message;
            while (!isClosed) {
                message = is.readLine();
                System.out.println("message: " + message);
                if (message == null) {
                    break;
                }
                String[] messageSplit = message.split(",");
                if (messageSplit[0].equals("send-to-global")) {
                    Server.serverThreadBus.boardCast(this.getClientNumber(), "global-message" + "," + "Client " + messageSplit[2] + ": " + messageSplit[1]);
                }
                if (messageSplit[0].equals("send-to-person")) {
                    Server.serverThreadBus.sendMessageToPersion(Integer.parseInt(messageSplit[3]), "Client " + messageSplit[2] + " (tới bạn): " + messageSplit[1]);
                }
                if (messageSplit[0].equals("start-quizz")) {
                    Server.serverThreadBus.sendMessage(clientNumber, "list-questions");
                }
                if (messageSplit[0].equals("send-list-question")) {
                    Server.serverThreadBus.sendObjectToPerson(clientNumber, questions);
                }
                if (messageSplit[0].equals("send-update-point")) {
                    Server.serverThreadBus.updatePoint(Integer.parseInt(messageSplit[1]), 1);
                    Server.serverThreadBus.sendUpdatePoint();
                }
                if (messageSplit[0].equals("Write-file-update-point")) {
                    Server.serverThreadBus.writeFileUpdate(Integer.parseInt(messageSplit[1]));
                }
                
                
                if (messageSplit[0].equals("get-end-time")) {
                    Server.serverThreadBus.sendMessage(clientNumber, "send-end-time");
                }
                if (messageSplit[0].equals("send-end-time")) {
                    Server.serverThreadBus.sendObjectToPerson(clientNumber, Server.getEndTime());
                }

            }
        } catch (IOException e) {
            isClosed = true;
            Server.serverThreadBus.remove(clientNumber);
            System.out.println(this.clientNumber + " đã thoát");
            Server.serverThreadBus.sendOnlineList();
            Server.serverThreadBus.mutilCastSend("global-message" + "," + "---Client " + this.clientNumber + " đã thoát---");
        }
    }

    public void write(String message) throws IOException {
        os.write(message);
        os.newLine();
        os.flush();
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
