package com.nhnacademy.aiot;

public class Victory {
    private Board board;
    boolean result = false;

    public Victory(Board board) {
        this.board = board;
        result = false;
    }

    public void win(Board board) {
        String compare1;
        char compare2;
        int[] winXY = new int[25];
        for (int i = 0; i < 25; i++) {
            compare1 = board.getIndex(i);
            compare2 = compare1.charAt(0);
            if (compare2 == '[') {
                winXY[i] = 1;
            }
        }
        for (int i = 0; i < 25; i += 5) {
            if (winXY[i] == 1 && winXY[i + 1] == 1 && winXY[i + 2] == 1 && winXY[i + 3] == 1 && winXY[i + 4] == 1) {
                result = true;
            } else if (winXY[i] == 1 && winXY[i + 5] == 1 && winXY[i + 10] == 1 && winXY[i + 15] == 1
                    && winXY[i + 20] == 1) {
                result = true;
            }
        }
        for (int i = 0; i < 5; i++) {
            if (winXY[i] == 1 && winXY[i + 5] == 1 && winXY[i + 10] == 1 && winXY[i + 15] == 1
                    && winXY[i + 20] == 1) {
                result = true;
            }
        }
        if (winXY[0] == 1 && winXY[6] == 1 && winXY[12] == 1 && winXY[18] == 1
                && winXY[24] == 1) {
            result = true;
        } else if (winXY[4] == 1 && winXY[8] == 1 && winXY[12] == 1 && winXY[16] == 1
                && winXY[20] == 1) {
            result = true;
        }
    }

    public boolean isResult() {
        return result;
    }
}
