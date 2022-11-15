/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import common.Question;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class ServerThreadBus {

    private List<ServerThread> listServerThreads;
    private Map<Integer, Question> questions;

    public List<ServerThread> getListServerThreads() {
        return listServerThreads;
    }

    public ServerThreadBus(Map<Integer, Question> questions) {
        listServerThreads = new ArrayList<>();
        this.questions = questions;
    }

    public void updatePoint(int id, int increment) {
        for (ServerThread serverThread : Server.serverThreadBus.getListServerThreads()) {
            if (serverThread.getClientNumber() == id) {
                serverThread.setPoint(serverThread.getPoint() + 1);
            }
        }
    }

    public void writeFileUpdate(int id) {
        File f = new File("points.txt");
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f, true));
            for (ServerThread serverThread : Server.serverThreadBus.getListServerThreads()) {
                if (serverThread.getClientNumber() == id) {
                    try {
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
                        bufferedWriter.write("Client " + serverThread.getClientNumber() + ": "
                                + serverThread.getPoint() + "/" + serverThread.getQuestionCount() + " - "
                                + dateTimeFormatter.format(LocalDateTime.now()) + "\n");
                        System.out.println("Points of client " + serverThread.getClientNumber() + " saved to file.");
                    } catch (IOException ex) {
                        System.out.println("Cannot write " + serverThread.getClientNumber() + " to file.");
//                        Logger.getLogger(ServerThreadBus.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println("Cannot open file " + f + ".");
//            Logger.getLogger(ServerThreadBus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeFileUpdateAll() {
        File f = new File("points.txt");
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f, true));;
            for (ServerThread serverThread : Server.serverThreadBus.getListServerThreads()) {
//                if (serverThread != null) {
                try {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
                    bufferedWriter.write("Client " + serverThread.getClientNumber() + ": "
                            + serverThread.getPoint() + "/" + serverThread.getQuestionCount() + " - "
                            + dateTimeFormatter.format(LocalDateTime.now()) + "\n");
                    System.out.println("Points of client " + serverThread.getClientNumber() + " saved to file.");
                } catch (IOException ex) {
                    System.out.println("Cannot write " + serverThread.getClientNumber() + " to file.");
//                    Logger.getLogger(ServerThreadBus.class.getName()).log(Level.SEVERE, null, ex);
                }
//                }
            }
            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println("Cannot open file " + f + ".");
//            Logger.getLogger(ServerThreadBus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void add(ServerThread serverThread) {
        listServerThreads.add(serverThread);
    }

    public void broadCast(String message) { //like sockets.emit in socket.io
//        System.out.print("BroadCast: ");
        for (ServerThread serverThread : Server.serverThreadBus.getListServerThreads()) {
//            System.out.print(serverThread.getClientNumber() + ",");
            serverThread.write(message);
        }
//        System.out.println("");
    }

    public void multiCast(int id, String message) {
        for (ServerThread serverThread : Server.serverThreadBus.getListServerThreads()) {
            if (serverThread.getClientNumber() == id) {
                continue;
            } else {
                serverThread.write(message);
            }
        }
    }

    public int getLength() {
        return listServerThreads.size();
    }

    public void sendOnlineList() {
        String res = "";
        List<ServerThread> threadbus = Server.serverThreadBus.getListServerThreads();
        for (ServerThread serverThread : threadbus) {
            res += serverThread.getClientNumber() + "-";
        }
        Server.serverThreadBus.broadCast("update-online-list" + "," + res);
    }

    public void sendMessageToPersion(int id, String message) {
        for (ServerThread serverThread : Server.serverThreadBus.getListServerThreads()) {
            if (serverThread.getClientNumber() == id) {
                serverThread.write("global-message" + "," + message);
                break;
            }
        }
    }

    public void sendUpdatePoint() {
        String res = "";
        List<ServerThread> threadbus = Server.serverThreadBus.getListServerThreads();
        List<Map.Entry<Integer, Integer>> rank = new ArrayList<>();
        for (ServerThread serverThread : threadbus) {
//            res += "Client " + serverThread.getClientNumber() + ": " + serverThread.getPoint() + "/" + serverThread.getQuestionCount() + "-";
            rank.add(new AbstractMap.SimpleEntry<>(serverThread.getClientNumber(), serverThread.getPoint()));
        }
        Collections.sort(rank, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });
        for (Map.Entry<Integer, Integer> m : rank) {
            res += "Client " + m.getKey() + ": " + m.getValue() + "/" + questions.size() + "-";
        }
        Server.serverThreadBus.broadCast("update-points" + "," + res);
    }

    public void sendMessage(int id, String message) {
        for (ServerThread serverThread : Server.serverThreadBus.getListServerThreads()) {
            if (serverThread.getClientNumber() == id) {
                serverThread.write(message);
                break;
            }
        }
    }

    public void sendObjectToPerson(int id, Object obj) {
        for (ServerThread serverThread : Server.serverThreadBus.getListServerThreads()) {
            if (serverThread != null && serverThread.getClientNumber() == id) {
                try {
                    Socket socket = serverThread.getSocketOfServer();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(obj);
                    break;
                } catch (IOException ex) {
//                    ex.printStackTrace();
                    System.out.println("Object: /" + obj + "/ couldnt send to client " + id);
                    serverThread.close();
                }
            }
        }
    }

    public synchronized void remove(int id) {
        for (int i = 0; i < Server.serverThreadBus.getLength(); i++) {
            ServerThread serverThread = Server.serverThreadBus.getListServerThreads().get(i);
            if (serverThread.getClientNumber() == id) {
                Server.serverThreadBus.listServerThreads.remove(i);
                break;
            }
        }
    }
}
