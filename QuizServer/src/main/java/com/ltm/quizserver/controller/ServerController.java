package com.ltm.quizserver.controller;

import com.ltm.model.ObjectWrapper;
import com.ltm.model.User;
import com.ltm.quizserver.dao.UserDAO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerController {

    private ServerSocket ss;
    private Thread clientHandler;

    public static void main(String[] args) {
    }

    public void startServer() {
        ss = null;
        try {
            ss = new ServerSocket(5056);
        } catch (IOException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }

        clientHandler = new Thread(new ClientHandler());
        clientHandler.start();
    }

    public void stopServer() {
        try {
            clientHandler.stop();
            ss.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class ClientHandler implements Runnable {

        public ClientHandler() {
            super();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Socket clientSocket = ss.accept();
                    System.out.println("A new client is connected : " + clientSocket);
                    System.out.println("Assigning new thread for this client");
                    Thread t = new Thread(new ClientProcessor(clientSocket));
                    t.start();
                } catch (IOException ex) {
                    Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }
            }
        }
    }

    class ClientProcessor extends Thread {

        private ObjectOutputStream oos;
        private ObjectInputStream ois;
        private Socket s;

        public ClientProcessor(Socket s) {
            super();
            try {
                this.s = s;
                oos = new ObjectOutputStream(s.getOutputStream());
                ois = new ObjectInputStream(s.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ClientProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            System.out.println("thread started");
            ObjectWrapper received;
            ObjectWrapper toreturn;
            while (true) {
                try {
                    // receive the request from client
                    received = (ObjectWrapper) ois.readObject();
                    System.out.println(received);
                    User u = null;
                    // write on output stream based on the
                    // request from the client
                    switch (received.getPerformative()) {

                        case ObjectWrapper.LOGIN_USER -> {
                            u = (User) received.getData();
                            System.out.println(u);
                            toreturn = new ObjectWrapper(ObjectWrapper.REPLY_LOGIN_USER, new UserDAO().checkLogin(u));
//                        dos.writeUTF(toreturn);
                            oos.writeObject(toreturn);
                        }

                        case ObjectWrapper.REGISTER_USER -> {
                            u = (User) received.getData();
                            System.out.println(u);
                            toreturn = new ObjectWrapper(ObjectWrapper.REPLY_REGISTER_USER, new UserDAO().saveUser(u));
//                        dos.writeUTF(toreturn);
                            oos.writeObject(toreturn);
                        }

                        default ->
                            oos.writeObject(new ObjectWrapper(-1000, "Invalid input"));
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }

            }
        }

    }
}
