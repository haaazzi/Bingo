package com.nhnacademy.aiot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class BingoServerThread extends Thread {
    static List<BingoServerThread> serverList = new LinkedList<>();
    Socket socket;
    BufferedWriter writer;
    Board oneBoard;
    Board twoBoard;
    static String one = null;
    static String two = null;
    static int who = 0;
    int start = 0;

    public BingoServerThread(Socket socket) {
        this.socket = socket;
        serverList.add(this);
    }

    public void send(String message) throws IOException {
        writer.write(message);
        writer.flush();
    }

    public void tab() throws IOException {
        writer.write("\n");
        writer.flush();
    }

    @Override
    public void run() {
        try (BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            this.writer = socketWriter;
            writer.write("ID:아이디를 입력해주세요.\n이 게임이 처음이라면 tips를 입력해주세요\n");
            writer.flush();
            while (!Thread.currentThread().isInterrupted()) {
                String line = socketReader.readLine() + "\n";
                System.out.println(getName() + " : " + line);
                String[] tokens = line.trim().split(":");

                if (tokens.length == 1) {
                    if (tokens[0].equalsIgnoreCase("tips")) {
                        writer.write(
                                " bingo 게임입니다 환영합니다\n");
                        writer.flush();
                        for (int i = 0; i < 25; i++) {
                            writer.write(" [ ] ");
                            writer.flush();
                            if ((i + 1) % 5 == 0) {
                                writer.write("\n");
                            }
                        }
                        writer.write(
                                " [ ] <-임의에 숫자를 넣어 한줄을 완성시키면 이기는 게임입니다.\n명령어 목록 : tips, @상대id:메세지, trun, who\nstart:25개의 숫자를 넣어 게임을 시작할수 있습니다.\n보드판이 완성되면 ch:숫자로 숫자를 선택할수 있습니다\n");
                        writer.flush();
                    } else if (tokens[0].equalsIgnoreCase("show") && getName().equals(one)) {
                        writer.write("나의 보드판\n");
                        writer.flush();
                        for (int i = 0; i < 25; i++) {
                            writer.write(oneBoard.getIndex(i) + "");
                            writer.flush();
                            if ((i + 1) % 5 == 0) {
                                writer.write("\n");
                                writer.flush();
                            }
                        }
                    } else if (tokens[0].equalsIgnoreCase("show") && getName().equals(two)) {
                        writer.write("나의 보드판\n");
                        writer.flush();
                        for (int i = 0; i < 25; i++) {
                            writer.write(twoBoard.getIndex(i) + "");
                            writer.flush();
                            if ((i + 1) % 5 == 0) {
                                writer.write("\n");
                                writer.flush();
                            }
                        }
                    } else if (tokens[0].equalsIgnoreCase("turn")) {
                        if (who == 0) {
                            writer.write("ID : " + one + " 순서입니다\n");
                            writer.flush();
                        } else if (who == 1) {
                            writer.write("ID : " + two + " 순서입니다\n");
                            writer.flush();
                        }
                    } else if (tokens[0].equalsIgnoreCase("who")) {
                        writer.write("ID : " + getName() + "\n");
                        writer.flush();
                    } else {
                        writer.write("잘못된 명령어 입니다.\n");
                        writer.flush();
                    }

                } else if (tokens.length > 1) {
                    if (tokens[0].equalsIgnoreCase("ID")) {
                        setName(tokens[1]);
                        if (one == null) {
                            one = getName();
                            System.out.println(one + " 플레이어가 게임에 입장하였습니다");
                            writer.write("첫번째 플레이어\n");
                            writer.flush();
                            System.out.println("첫번째 플레이어");
                        } else if (two == null) {
                            two = getName();
                            System.out.println(tokens[1] + " 플레이어가 게임에 입장하였습니다");
                            writer.write("두번째 플레이어\n플레이어가 모두 입장하여 게임 준비가 완료되었습니다.\n");
                            writer.flush();
                            System.out.println("두번째 플레이어");
                        } else {
                            writer.write("이미 플레이어 두명이 있습니다.");
                            writer.flush();
                            System.out.println("이미 플레이어 두명이 있습니다.");
                        }
                    } else if ((one == null) || (two == null)) {
                        writer.write("플레이어들이 없습니다. ID:아이디를 입력하세요.\n");
                        writer.flush();
                    } else if ((tokens[0].charAt(0) == '@') && (tokens[0].length() > 1)) {
                        String targetid = tokens[0].substring(1, tokens[0].length());
                        for (BingoServerThread server : serverList) {
                            if (targetid.equals(server.getName())) {
                                server.send("#" + getName() + " : " + tokens[1] + "\n");
                            }
                        }
                    } else if ((tokens[0].equalsIgnoreCase("start")) && getName().equals(one)) {
                        start = 0;
                        String[] booardIndex = tokens[1].split(",");
                        for (int i = 24; i >= 0; i--) {
                            for (int j = i - 1; j >= 0; j--) {
                                if (booardIndex[i].equals(booardIndex[j])) {
                                    System.out.println("중복되는 수를 넣었습니다");
                                    writer.write("중복되는 수를 넣었습니다. 다시 입력하세요.\n");
                                    writer.flush();
                                    start = 1;
                                    break;
                                }
                                if (start == 1)
                                    break;
                            }
                        }
                        if (start == 0) {
                            oneBoard = new Board(one);
                            oneBoard.make(booardIndex);
                            writer.write("id : " + getName() + "의 빙고판\n");
                            writer.flush();
                            for (int i = 0; i < 25; i++) {
                                writer.write(" " + booardIndex[i] + " ");
                                writer.flush();
                                if ((i + 1) % 5 == 0) {
                                    writer.write("\n");
                                    writer.flush();
                                }
                            }
                        }
                    } else if ((tokens[0].equalsIgnoreCase("start")) && getName().equals(two)) {
                        start = 0;
                        String[] booardIndex = tokens[1].split(",");
                        for (int i = 24; i >= 0; i--) {
                            for (int j = i - 1; j >= 0; j--) {
                                if (booardIndex[i].equals(booardIndex[j])) {
                                    System.out.println("중복되는 수를 넣었습니다");
                                    writer.write("중복되는 수를 넣었습니다.다시 입력하세요.\n");
                                    writer.flush();
                                    start = 1;
                                    break;
                                }
                                if (start == 1)
                                    break;
                            }
                        }
                        if (start == 0) {
                            twoBoard = new Board(two);
                            twoBoard.make(booardIndex);
                            writer.write("id : " + getName() + "의 빙고판\n");
                            writer.flush();
                            for (int i = 0; i < 25; i++) {
                                writer.write(" " + booardIndex[i] + " ");
                                writer.flush();
                                if ((i + 1) % 5 == 0) {
                                    writer.write("\n");
                                    writer.flush();
                                }
                            }
                        }
                    } else if ((tokens[0].equalsIgnoreCase("ch")) && (getName() == one) && (who == 0)) {
                        int compare = Integer.parseInt(tokens[1]);
                        if (compare < 10) {
                            tokens[1] = " 0" + tokens[1] + " ";
                        } else {
                            tokens[1] = " " + tokens[1] + " ";
                        }
                        for (int i = 0; i < 25; i++) {
                            if (oneBoard.getIndex(i).equals(tokens[1])) {
                                oneBoard.setIndex(i);
                            }
                        }
                        oneBoard.show();
                        for (int i = 0; i < 25; i++) {
                            writer.write(oneBoard.getIndex(i));
                            writer.flush();
                            if ((i + 1) % 5 == 0) {
                                writer.write("\n");
                                writer.flush();
                            }
                        }
                        Victory vic = new Victory(oneBoard);
                        vic.win(oneBoard);
                        if (vic.isResult() == true) {
                            writer.write(one + "플레이어가 빙고를 완료하여 승리하였습니다.\n");
                            writer.flush();
                        }
                        for (BingoServerThread server : serverList) {
                            if (server.getName() == two) {
                                for (int i = 0; i < 25; i++) {
                                    if (server.twoBoard.getIndex(i).equals(tokens[1])) {
                                        server.twoBoard.setIndex(i);
                                    }
                                }
                                server.send(one + "가" + tokens[1] + "을 선택하였습니다\n" + one + "플레이어턴이 지났습니다.\n" + one
                                        + "플레이어의 현재 보드\n");
                                for (int i = 0; i < 25; i++) {
                                    server.send(oneBoard.getIndex(i));
                                    if ((i + 1) % 5 == 0) {
                                        server.tab();
                                    }
                                }
                                if (vic.isResult() == true) {
                                    server.send(one + "플레이어가 빙고를 완료하여 승리하였습니다.\n패배하였습니다\n");
                                    System.exit(0);
                                }
                                server.send("\n");
                                server.send("플레이어 턴\n");
                            }
                        }
                        writer.write("\n");
                        writer.flush();
                        who = 1;
                    } else if ((tokens[0].equalsIgnoreCase("ch")) && (getName() == two) && (who == 1)) {
                        int compare = Integer.parseInt(tokens[1]);
                        if (compare < 10) {
                            tokens[1] = " 0" + tokens[1] + " ";
                        } else {
                            tokens[1] = " " + tokens[1] + " ";
                        }
                        for (int i = 0; i < 25; i++) {
                            if (twoBoard.getIndex(i).equals(tokens[1])) {
                                twoBoard.setIndex(i);
                            }
                        }
                        twoBoard.show();
                        for (int i = 0; i < 25; i++) {
                            writer.write(twoBoard.getIndex(i));
                            writer.flush();
                            if ((i + 1) % 5 == 0) {
                                writer.write("\n");
                                writer.flush();
                            }
                        }
                        Victory vic = new Victory(twoBoard);
                        vic.win(twoBoard);
                        if (vic.isResult() == true) {
                            writer.write(two + "플레이어가 빙고를 완료하여 승리하였습니다.\n");
                            writer.flush();
                        }
                        for (BingoServerThread server : serverList) {
                            if (server.getName() == one) {
                                for (int i = 0; i < 25; i++) {
                                    if (server.oneBoard.getIndex(i).equals(tokens[1])) {
                                        server.oneBoard.setIndex(i);
                                    }
                                }
                                server.send(two + "가" + tokens[1] + "을 선택하였습니다\n" + two + "플레이어턴이 지났습니다.\n" + two
                                        + "플레이어의 현재 보드\n");
                                for (int i = 0; i < 25; i++) {
                                    server.send(twoBoard.getIndex(i));
                                    if ((i + 1) % 5 == 0) {
                                        server.tab();
                                    }
                                }
                                if (vic.isResult() == true) {
                                    server.send(two + "플레이어가 빙고를 완료하여 승리하였습니다.\n패배하였습니다\n");
                                    System.exit(0);
                                }
                                server.send("\n");
                                server.send("플레이어 턴\n");
                            }
                        }
                        writer.write("\n");
                        writer.flush();
                        who = 0;

                    } else {
                        writer.write("플레이어의 순서가 아니거나 잘못 입력하였습니다\n");
                        writer.flush();
                    }
                }
            }

        } catch (IOException ignore) {
        }
        try {
            socket.close();
        } catch (IOException ignore) {

        }
    }
}
