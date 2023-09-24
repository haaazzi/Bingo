package com.nhnacademy.aiot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class BingoPlayerHandler extends Thread {
    Socket socket;
    BufferedReader reader;
    BufferedWriter writer;
    Board board;
    static int playerCount = 0;
    String playerId;
    boolean isMyTurn;

    public BingoPlayerHandler(Socket socket) throws IOException {
        isMyTurn = (playerCount == 0);
        playerId = "player" + playerCount++;
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void send(String message) throws IOException {
        writer.write(message + System.lineSeparator());
        writer.flush();
    }

    public void sendBoard() throws IOException {
        for (BingoPlayerHandler handler : BingoServer.playerHandlerList) {
            handler.send(handler.getBoard().getBoardString());
        }
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public void run() {
        try {
            initialize();
            String line;
            while ((line = reader.readLine()) != null) {
                if (isMyTurn) {
                    if (board.isSelected(line)) {
                        send("이미 선택된 숫자입니다. 다른 숫자를 선택하세요.");
                        continue;
                    }
                    for (BingoPlayerHandler handler : BingoServer.playerHandlerList) {
                        if (handler != this) {
                            handler.send("상대 플레이어 숫자 : " + line);
                            handler.getBoard().marking(line, false);
                        } else {
                            board.marking(line, true);
                        }
                    }
                    sendBoard();
                    Bingo.changeTurn();
                } else {
                    send("상대방의 순서입니다.");
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void initialize() throws IOException {
        send("bingo start, " + BingoServer.BOARD_SIZE * BingoServer.BOARD_SIZE + "개의 숫자를 입력해주세요.");
        String line = reader.readLine();
        board = new Board(line);
        send(board.getBoardString());
    }
}
