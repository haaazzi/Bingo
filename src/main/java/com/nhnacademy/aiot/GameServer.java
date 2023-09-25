package com.nhnacademy.aiot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class GameServer extends Thread{
    Socket socket;
    char mark;
    static char turn = '[';
    boolean isGameOver = false;
    BufferedWriter serveroutput;// output results to players
    GameLogic game = new GameLogic();
    static List<GameServer> serverList = new LinkedList<>();// player와 연결된 소켓

    // 1.
    // 초기화========================================================================
    public GameServer(Socket socket) {
        this.socket = socket;
        serverList.add(this);
    }

    // 1-1. Send 기능
    public void sendMessage(String message) {
        try {
            serveroutput.write(message);
            serveroutput.flush();

        } catch (IOException e) {
            System.out.println("Something wrong with sendMessage method!");
            e.printStackTrace();
        }
    }

    // 1-2. Broadcast 기능
    public void telltoPlayers(String message) {
        for (GameServer player : serverList) {
            player.sendMessage(message);
        }
    }

    // 1-3. 1:1 send 기능(서버:특정 player)
    public void telltoOtherPlayer(String message) {
        for (GameServer player : serverList) {
            if (!player.getName().equals(getName())) {
                player.sendMessage(message);
            }
        }
    }

    // 1-4. 자기 차례인지 boolean 반납
    public boolean isMyTurn() {
        boolean isMyTurn = false;

        if (turn == mark) {
            isMyTurn = true;
        }

        return isMyTurn;
    }

    // 2. Player
    // Setting=============================================================
    // 2-1. ID 등록
    public void setClientID(String id) {
        setName(id);
        System.out.println(getName() + "이/가 게임에 접속되었습니다.");
    }

    // 2-2. Mark 부여(o/x)
    public void setMark() {
        // 먼저 등록된 서버이면 o, 아니면 x
        if (serverList.get(0).equals(this)) {
            this.mark = '[';
        } else {
            this.mark = '{';
        }
    }

    // 2-3. player setup과 관련된 기능들의 모음
    public void playerSetup(String inputline) throws InterruptedException {

        setClientID(inputline);// ID 설정
        setMark();// O,X 설정

        if (serverList.size() == 1) {
            sendMessage("현재 참가자가 1명입니다. 기다려주세요......\n");
            Thread.sleep(10000);

        } else {
            telltoPlayers("현재 참가자가 2명입니다. 게임을 곧 시작하겠습니다! 잠시만 기다려주세요......\n");
            printTable();
        }
        
    }

    // 3.게임 실행======================================================================
    // 3-1. Turn switch
    public static void nextTurn() {
        if (turn == '[') {
            turn = '{';
        } else if (turn == '{') {
            turn = '[';
        }
    }

    // 3-2. Game table 출력
    public void printTable() {
        telltoPlayers(game.tableToString());
    }

    // 3-3. 게임 실행
    @Override
    public void run() {
        try (BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter serverOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            this.serveroutput = serverOut;
            
            String input_line;
            sendMessage("============Tic-Tac-Toe Game============\n");
            sendMessage("환영합니다! 자신의 ID를 입력해주세요. ID : ");
            input_line = serverIn.readLine();

            // Player Setup
            if (!"123456789".contains(input_line.trim())) {

                playerSetup(input_line);

            }

            while (!Thread.currentThread().isInterrupted() && !isGameOver) {

                // play - first round ~ - gameplay
                if (isMyTurn()) {
                    telltoPlayers(getName() + "의 차례입니다!===============================\n");
                    sendMessage("1 ~ 9 사이의 수를 입력해주세요 : ");
                    telltoOtherPlayer(getName() + "이/가 입력하고 있습니다........\n");

                    boolean repeatInput = true;

                    while (repeatInput && (input_line = serverIn.readLine()) != null) {
                        if ("123456789".contains(input_line)) {
                            repeatInput = game.getPlayerInput(input_line.trim(), mark);
                        }else if ("QqEeexitExitQuitquit".contains(input_line)) {

                            telltoOtherPlayer("상대방이 나갔습니다. 게임 다시하기를 원하시면 프로그램을 다시 시작해주세요!/n");
                            sendMessage("지금 나갑니다....\n");
                            socket.close();
                            System.exit(0);

                        }else{
                            sendMessage("잘못 입력되었습니다.\n다시 입력해주세요. 1 ~ 9 사이의 수를 입력해주세요 : ");
                        }
                    }

                    // Print the board
                    printTable();

                    // checkGameOver
                    isGameOver = game.checkGameOver(mark);

                    if (isGameOver) {
                        // Win
                        telltoPlayers(getName() + "이/가 이겼습니다! 축하합니다!\n");
                    }else{
                        //다 찼으면
                        if (game.isFull()) {
                            isGameOver = true;
                            telltoPlayers("무승부입니다.\n");
                            break;
                        }
                    }

                    // Game Over가 아니면 turn 바꾸기
                    nextTurn();
                }

            }
            
            if (isGameOver) {
                socket.close();
                System.exit(0);
            }
        } catch (IOException ioe) {
            System.err.println("IO error in Server's run method!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted Server's run method!");
        }
    }
    
}
