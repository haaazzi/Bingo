package com.nhnacademy.aiot;

public class Board {
    private String id;
    String[] boardmaker;

    public Board(String id) {
        this.id = id;
        boardmaker = new String[25];
    }

    public void make(String[] index) {
        for (int i = 0; i < 25; i++) {
            int check = Integer.parseInt(index[i]);
            if (check < 10) {
                index[i] = "0" + index[i];
            }
            boardmaker[i] = " " + index[i] + " ";
            System.out.print(" " + index[i] + " ");
            if ((i + 1) % 5 == 0)
                System.out.println();
        }
    }

    public void show() {
        for (int i = 0; i < 25; i++) {
            System.out.print(" " + boardmaker[i] + " ");
            if ((i + 1) % 5 == 0)
                System.out.println();
        }
    }

    public String getId() {
        return id;
    }

    public String[] getBoardmaker() {
        return boardmaker;
    }

    public String getIndex(int i) {
        return boardmaker[i];
    }

    public void setIndex(int index) {
        boardmaker[index] = "[" + boardmaker[index].trim() + "]";

    }

}
