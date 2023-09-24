package com.nhnacademy.aiot;

public class Bingo {

    public static void changeTurn() {
        for (BingoPlayerHandler handler : BingoServer.playerHandlerList) {
            handler.isMyTurn = !handler.isMyTurn;
        }
    }

    public static void main(String[] args) {
        System.out.println("Let's Play Bingo");
    }

}
