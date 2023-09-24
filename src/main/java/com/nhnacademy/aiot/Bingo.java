package com.nhnacademy.aiot;

public class Bingo {

    public static void changeTurn() {
        for (BingoPlayerHandler handler : BingoServer.playerHandlerList) {
            handler.isMyTurn = !handler.isMyTurn;
        }
    }

    public static boolean checkWin(Board board) {
        String check;
        int[] winXY = new int[BingoServer.BOARD_SIZE * BingoServer.BOARD_SIZE];
        for (int i = 0; i < winXY.length; i++) {
            check = board.getBoard()[i];
            if (check.startsWith(Board.ANSI_BLUE)) {
                winXY[i] = 1;
            }
        }
        for (int i = 0; i < 25; i += 5) {
            if (winXY[i] == 1 && winXY[i + 1] == 1 && winXY[i + 2] == 1 && winXY[i + 3] == 1 && winXY[i + 4] == 1) {
                return true;
            }
        }
        for (int i = 0; i < 5; i++) {
            if (winXY[i] == 1 && winXY[i + 5] == 1 && winXY[i + 10] == 1 && winXY[i + 15] == 1
                    && winXY[i + 20] == 1) {
                return true;
            }
        }
        if (winXY[0] == 1 && winXY[6] == 1 && winXY[12] == 1 && winXY[18] == 1
                && winXY[24] == 1) {
            return true;
        } else if (winXY[4] == 1 && winXY[8] == 1 && winXY[12] == 1 && winXY[16] == 1
                && winXY[20] == 1) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("Let's Play Bingo");
    }

}
