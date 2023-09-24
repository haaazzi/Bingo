package com.nhnacademy.aiot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

// java BingoServer PORT MAX_PLAYER BOARD_SIZE
public class BingoServer {
    static int BOARD_SIZE;
    static int MAX_PLAYER;
    static int readyCount = 0;
    static List<Socket> playerSocketList = new ArrayList<>();
    static List<BingoPlayerHandler> playerHandlerList = new ArrayList<>();

    static List<BufferedWriter> playerWriterList = new ArrayList<>();
    // static List<BufferedReader> playerReaderList = new ArrayList<>();
    // static List<Board> playerBoardList = new ArrayList<>();

    public static void sendAll(String message) throws IOException {
        for (BufferedWriter writer : playerWriterList) {
            writer.write(message);
            writer.flush();
        }
    }

    public static void main(String[] args) throws IOException {
        // int port = Integer.parseInt(args[0]);
        // int MAX_PLAYER = Integer.parseInt(args[1]);
        // int BOARD_SIZE = Integer.parseInt(args[2]);

        int port = 1234;
        MAX_PLAYER = 2;
        BOARD_SIZE = 5;
        Socket socket;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while ((socket = serverSocket.accept()) != null) {
                playerSocketList.add(socket);
                System.out.println(socket.getInetAddress());
                BingoPlayerHandler handler = new BingoPlayerHandler(socket);
                // handler.start();
                playerHandlerList.add(handler);
                if (playerSocketList.size() == MAX_PLAYER) {
                    startGame();
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void startGame() {
        for (BingoPlayerHandler handler : playerHandlerList) {
            handler.start();
        }
    }
}
