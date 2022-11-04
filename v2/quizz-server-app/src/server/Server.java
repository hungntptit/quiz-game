/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Enumeration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class Server {

    public static volatile ServerThreadBus serverThreadBus;
    public static Socket socketOfServer;
    private static String questionPath = null;
    private static long timeLimit = 0;
    private static Instant startTime;
    private static Instant endTime;

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equals("-file") && i < args.length - 1) {
                questionPath = args[i + 1];
            }
            if (args[i].equals("-time") && i < args.length - 1) {
                timeLimit = Integer.parseInt(args[i + 1]);
            }
        }
        if (questionPath == null || timeLimit == 0) {
            System.out.println("args:\n -file <question file path> \n -time <time limit in minutes>");
            return;
        }
        if (!(new File(questionPath).exists())) {
            System.out.println("Question file path does not exists.\n" + questionPath);
            return;
        }
        ServerSocket listener = null;
        serverThreadBus = new ServerThreadBus();

        int clientNumber = 0;
        try {
            listener = new ServerSocket(7778);
        } catch (IOException e) {
            System.out.println(e);
            return;
        }
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10, // corePoolSize
                100, // maximumPoolSize
                10, // thread timeout
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(8) // queueCapacity
        );
        displayAddresses();
        System.out.println("Server is waiting to accept user...");
        startTime = Instant.now();
        endTime = startTime.plus(timeLimit, ChronoUnit.MINUTES);
        System.out.println("Started at: " + startTime);
        System.out.println("Ending at: " + endTime);
        try {
            while (true) {
                socketOfServer = listener.accept();
                ServerThread serverThread = new ServerThread(socketOfServer, clientNumber++, questionPath);
                serverThreadBus.add(serverThread);
                System.out.println("Số thread đang chạy là: " + serverThreadBus.getLength());
                executor.execute(serverThread);
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                listener.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void displayAddresses() {
        try {
            Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();
            System.out.println("Server IP Addresses:");
            while (nics.hasMoreElements()) {
                NetworkInterface nic = nics.nextElement();
                Enumeration<InetAddress> addrs = nic.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    if (addr instanceof Inet4Address) {
                        System.out.printf("\t%s %s%n", nic.getName(), addr.getHostAddress());
                    }
                }
            }
            System.out.println("Port: " + 7778);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static Instant getEndTime() {
        return endTime;
    }

}
