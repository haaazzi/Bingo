package com.nhnacademy.aiot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BingoPlayer {
    Board board;
    BufferedReader socketIn;
    BufferedWriter socketOut;
    BufferedReader terminalIn;
    BufferedWriter terminalOut;
    Scanner sc;

    public BingoPlayer(String host, int port) {
        try {
            sc = new Scanner(System.in);
            Socket socket = new Socket(host, port);
            socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            terminalIn = new BufferedReader(new InputStreamReader(System.in));
            terminalOut = new BufferedWriter(new OutputStreamWriter(System.out));
            new IOThread(socketIn, terminalOut).start();
            new IOThread(terminalIn, socketOut).start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // public Board getBoard() {
    // return board;
    // }

    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        int port = 1234;
        String host = "localhost";
        new BingoPlayer(host, port);
    }
}
