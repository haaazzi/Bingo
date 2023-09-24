package com.nhnacademy.aiot;

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
    int MAX_SIZE;// n의 값
    static String[][] bingoTable;

    // 1. 판을 생성(n x n - 단 n은 홀수이고 5이상인 수)
    public GameLogic(int MAX_SIZE) {

        // 2. n x n 개의 수를 생성 -> random으로 석김
    }
    
    public void serTable(int MAX_SIZE){
        for (int row = 0; row < MAX_SIZE; row++) {
            for (int col = 0; col < MAX_SIZE; col++) {
                bingoTable[row][col] = Double.toString((Math.random() * (MAX_SIZE - 1)) + 1);
            }
        }
    }

    // table return
    public String[][] getGameTable() {
        return bingoTable;
    }

    // table이 다 찼으면
    public boolean isFull() {
        int count = 0;
        for (int row = 0; row < MAX_SIZE; row++) {
            for (int column = 0; column < MAX_SIZE; column++) {
                if (!bingoTable[row][column].equals("[ ]")) {
                    count++;
                }
            }
        }

        return (count == 9);

    }

    // table을 string으로 반환하기
    public String tableToString() {

        StringBuilder line = new StringBuilder();

        for (int row = 0; row < MAX_SIZE; row++) {
            for (int column = 0; column < MAX_SIZE; column++) {
                line.append(bingoTable[row][column]);
            }
            line.append("\n");
        }

        return line.toString();
    }

    // player 입력 받기
    public synchronized boolean getPlayerInput(String playerInput, char mark) {
        int placement = Integer.parseInt(playerInput);
        boolean repeatInput = false;

        if (placement > 9 || placement < 0) {
            repeatInput = true;

        } else {
            // row & column 찾기
            int row = placement / 3;
            int column = placement % 3;

            if (placement % 3 == 0) {
                row--;
                column = 2;
            } else {
                column--;
            }

            // mark를 입력
            if (bingoTable[row][column].equals("[ ]")) {
                bingoTable[row][column] = "[" + mark + "]";
            } else {
                repeatInput = true;
                System.out.println("이미 다른 값이 있습니다.");
            }
        }

        return repeatInput;

    }

    // 우승유무 확인
    public boolean checkGameOver(char mark) {
        String encasedMark = "[" + mark + "]";
        boolean isGameOver = false;

        // Check rows
        /*
         * for each rows in gametable
         * if all symbols in row are same and not null -> return true
         * 
         */

        for (int row = 0; row < 3; row++) {
            if (bingoTable[row][0].equals(encasedMark) && bingoTable[row][1].equals(encasedMark)
                    && bingoTable[row][2].equals(encasedMark)) {
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
            if (bingoTable[0][column].equals(encasedMark) && bingoTable[1][column].equals(encasedMark)
                    && bingoTable[2][column].equals(encasedMark)) {
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

        if ((bingoTable[0][0].equals(encasedMark) || bingoTable[0][2].equals(encasedMark))
                && bingoTable[1][1].equals(encasedMark)
                && (bingoTable[2][2].equals(encasedMark) || bingoTable[2][0].equals(encasedMark))) {
            isGameOver = true;
            return isGameOver;
        }

        return isGameOver;
    }

}
