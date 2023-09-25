package com.nhnacademy.aiot;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Bingo {
    
    public static void main(String[] args) {
        List<GameServer> clientList = new LinkedList<>();
        System.out.println("Let's Play Bingo");
        System.out.println("Game 준비 중...");

        try (ServerSocket serverSocket = new ServerSocket(1234);) {
            
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                GameServer player = new GameServer(socket);

                player.start();
                clientList.add(player);
                
                if (clientList.size() == 2) {
                    break;
                }

            }
        
        } catch (IOException ioe) {
            System.err.println(ioe);
        }

        try {
            for (GameServer ticTacToeServer : clientList) {
                ticTacToeServer.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
}
