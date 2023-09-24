package com.nhnacademy.aiot;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server {
    static List<BingoServerThread> serverList = new LinkedList<>();

    public static void main(String[] args) {
        int port = 1234;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                BingoServerThread server = new BingoServerThread(socket);
                server.start();
            }
        } catch (IOException ignore) {

        }
        for (BingoServerThread server : serverList) {
            server.interrupt();
            try {
                server.join();
            } catch (InterruptedException ignore) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
