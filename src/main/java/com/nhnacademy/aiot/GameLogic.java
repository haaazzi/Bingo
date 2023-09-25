package com.nhnacademy.aiot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * :게임의 작동원리 담음
 * 
 * 1. 판을 생성
 * 2. player 입력을 받고 적용
 * 3. 우승유무 판별
 * 4. 판을 반납 및 우승유무인지 결과를 전달
 * 
 */
public class GameLogic {
    static String[][] gameTable = new String[5][5];

    // table return
    public String[][] getGameTable() {
        return gameTable;
    }

    public static void setGameTable() {
        List<Integer> number = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            number.add(i + 1);
        }

        Collections.shuffle(number);

        for (int i = 0; i < 5; i++) {

            for (int j = 0; j < 5; j++) {
                if (number.get((5*i) + j) < 10) {
                    gameTable[i][j] = "0" + String.valueOf(number.get((5*i) + j));
                } else {
                    gameTable[i][j] = String.valueOf(number.get((5*i) + j));
                }
            }
        }
    }

    // table이 다 찼으면
    public boolean isFull() {
        int count = 0;
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                if (gameTable[row][column].contains("[") || gameTable[row][column].contains("{")) {
                    count++;
                }
            }
        }

        return (count == 25);

    }

    // table을 string으로 반환하기
    public String tableToString() {

        StringBuilder line = new StringBuilder();

        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                line.append(gameTable[row][column] + " ");
            }
            line.append("\n");
        }

        return line.toString();
    }

    // player 입력 받기
    public synchronized boolean getPlayerInput(String playerInput, char mark) {
        int placement = Integer.parseInt(playerInput);
        boolean repeatInput = false;

        if (placement > 25 || placement < 0) {
            repeatInput = true;

        } else {
            // row & column 찾기
            int row = placement / 5;
            int column = placement % 5;

            if (placement % 5 == 0) {
                row--;
                column = 4;
            } else {
                column--;
            }

            // mark를 입력
            if (!gameTable[row][column].contains("[") || !gameTable[row][column].contains("{")) {
                if (mark == '[') {
                    gameTable[row][column] = "[" + gameTable[row][column] + "]";
                } else if (mark == '{') {
                    gameTable[row][column] = "{" + gameTable[row][column] + "}";
                }
            } else {
                repeatInput = true;
                System.out.println("이미 다른 값이 있습니다.");
            }
        }

        return repeatInput;

    }

    // 우승유무 확인
    public boolean checkGameOver(char mark) {
        String encasedMark = String.valueOf(mark);
        boolean isGameOver = false;

        // Check rows
        /*
         * for each rows in gametable
         * if all symbols in row are same and not null -> return true
         * 
         */

        for (int row = 0; row < 5; row++) {
            if (gameTable[row][0].contains(encasedMark) && gameTable[row][1].contains(encasedMark)
                    && gameTable[row][2].contains(encasedMark) && gameTable[row][3].contains(encasedMark)
                    && gameTable[row][4].contains(encasedMark)) {
                isGameOver = true;
                return isGameOver;
            }
        }
        // Check columns
        /*
         * for each columns in gametable
         * if all symbols in columns are same and not null -> return true
         * 
         */
        for (int column = 0; column < 3; column++) {
            if (gameTable[0][column].contains(encasedMark) && gameTable[1][column].contains(encasedMark)
                    && gameTable[2][column].contains(encasedMark) && gameTable[3][column].contains(encasedMark)
                    && gameTable[4][column].contains(encasedMark)) {
                isGameOver = true;
                return isGameOver;
            }
        }

        // Check diagonals
        /*
         * for each rows in gametable
         * if all symbols in any diagonal are same and not null -> return true
         * 
         */

        if ((gameTable[0][0].contains(encasedMark) || gameTable[0][4].contains(encasedMark))
                && (gameTable[1][1].contains(encasedMark) || gameTable[1][3].contains(encasedMark))
                && gameTable[2][2].contains(encasedMark)
                && (gameTable[3][1].contains(encasedMark) || gameTable[3][3].contains(encasedMark))
                && (gameTable[4][0].contains(encasedMark) || gameTable[4][4].contains(encasedMark))) {
            isGameOver = true;
            return isGameOver;
        }

        return isGameOver;
    }
}
