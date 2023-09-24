package com.nhnacademy.aiot;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private String[] board;
    List<String> selected = new ArrayList<>();

    public Board(String input) {
        board = input.split(" ");
        for (int i = 0; i < board.length; i++) {
            if (Integer.parseInt(board[i]) < 10) {
                board[i] = "0" + board[i];
            }
        }
    }

    public String getBoardString() {
        StringBuilder boardString = new StringBuilder();
        boardString.append(System.lineSeparator()).append("현재 보드").append(System.lineSeparator());
        int count = 0;
        for (int i = 1; i <= board.length; i++) {
            boardString.append(board[i - 1] + " ");
            count++;
            if (count % BingoServer.BOARD_SIZE == 0) {
                boardString.append(System.lineSeparator());
            }
        }
        return boardString.toString();
    }

    public boolean isSelected(String number) {
        return selected.contains(number);
    }

    public static void changeTurn() {
        for (BingoPlayerHandler handler : BingoServer.playerHandlerList) {
            handler.isMyTurn = !handler.isMyTurn;
        }
    }

    public void marking(String number, boolean isMyNumber) {
        for (int i = 0; i < 25; i++) {
            if (board[i].equals(number)) {
                board[i] = isMyNumber ? ANSI_BLUE + number + ANSI_RESET : ANSI_RED + number + ANSI_RESET;
                selected.add(number);
            }
        }
    }
}
