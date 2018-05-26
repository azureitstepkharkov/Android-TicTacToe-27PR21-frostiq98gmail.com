package com.example.frost.tictactoe_android.game;

public class Game {

    private int size;
    private Player board[][];
    private Player currentPlayer = Player.PLAYER_1;
    private Player winner;

    public Game(int size) {
        this.size = size;
        board = new Player[size][size];
    }

    public Player getWinner() {
        return winner;
    }

    public boolean isFinished() {
        if (isPlayerWinner(Player.PLAYER_1)) {
            winner = Player.PLAYER_1;
        } else if (isPlayerWinner(Player.PLAYER_2)) {
            winner = Player.PLAYER_2;
        }

        return winner != null || isBoardFilled();
    }

    private boolean isBoardFilled() {
        int total = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board[x][y] != null) {
                    total++;
                }
            }
        }
        return total == size * size;
    }

    private boolean isPlayerWinner(Player player) {
        return isAnyColumnFilled(player) || isAnyRowFilled(player) || isMainDiagonalFilled(player) || isBackwardDiagonalFilled(player);
    }

    private boolean isBackwardDiagonalFilled(Player player) {
        int total = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (x + y == size - 1 && player == board[x][y]) {
                    total++;
                }
            }
        }
        return total >= size;
    }

    private boolean isMainDiagonalFilled(Player player) {
        int total = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (x == y && player == board[x][y]) {
                    total++;
                }
            }
        }
        return total >= size;
    }

    private boolean isAnyRowFilled(Player player) {
        for (int y = 0; y < size; y++) {
            int total = 0;
            for (int x = 0; x < size; x++) {
                if (player == board[x][y]) {
                    total++;
                }
            }
            if (total >= size) {
                return true;
            }
        }
        return false;
    }

    private boolean isAnyColumnFilled(Player player) {
        for (int x = 0; x < size; x++) {
            int total = 0;
            for (int y = 0; y < size; y++) {
                if (player == board[x][y]) {
                    total++;
                }
            }
            if (total >= size) {
                return true;
            }
        }
        return false;
    }

    public void makeTurn(int x, int y) {
        board[x][y] = currentPlayer;
        if (currentPlayer == Player.PLAYER_1) {
            currentPlayer = Player.PLAYER_2;
        } else {
            currentPlayer = Player.PLAYER_1;
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void reset() {
        board = new Player[size][size];
        currentPlayer = Player.PLAYER_1;
        winner = null;
    }

    public enum Player {
        PLAYER_1("X"), PLAYER_2("O");

        private String name;

        Player(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
