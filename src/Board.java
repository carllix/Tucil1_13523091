package src;

import java.util.List;

public class Board {
    private int rows, cols;
    private char[][] board;

    private static final String[] COLORS = {
            "\u001B[31m", "\u001B[32m", "\u001B[33m", "\u001B[34m", "\u001B[35m", "\u001B[36m",
            "\u001B[91m", "\u001B[92m", "\u001B[93m", "\u001B[94m", "\u001B[95m", "\u001B[96m",
            "\u001B[41m", "\u001B[42m", "\u001B[43m", "\u001B[44m", "\u001B[45m", "\u001B[46m",
            "\u001B[101m", "\u001B[102m", "\u001B[103m", "\u001B[104m", "\u001B[105m", "\u001B[106m"
    };
    private static final String RESET = "\u001B[0m";

    public Board(int rows, int cols, List<Block> blocks) {
        this.rows = rows;
        this.cols = cols;
        this.board = new char[rows][cols];
        initializeEmptyBoard();
    }

    private void initializeEmptyBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = '.';
            }
        }
    }

    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    public char[][] getBoard() {
        return this.board;
    }

    public String[] getColors() {
        return COLORS;
    }

    public void printBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char cell = board[i][j];
    
                if (cell >= 'A' && cell <= 'Z') {
                    int colorIdx = cell - 'A'; 
                    System.out.print(COLORS[colorIdx] + cell + RESET + " ");
                } else {
                    System.out.print(cell + " ");
                }
            }
            System.out.println();
        }
    }

    private boolean isOutOfBounds(int row, int col) {
        return row >= rows || col >= cols;
    }

    public boolean canPlaceBlock(Block block, int x, int y) {
        for (int i = 0; i < block.getHeight(); i++) {
            for (int j = 0; j < block.getWidth(); j++) {
                char[][] cells = block.getCells();
                if (cells[i][j] != '.') {
                    if (isOutOfBounds(x + i, y + j) || board[x + i][y + j] != '.') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void placeBlock(Block block, int x, int y) {
        for (int i = 0; i < block.getHeight(); i++) {
            for (int j = 0; j < block.getWidth(); j++) {
                char[][] cells = block.getCells();
                if (cells[i][j] != '.') {
                    board[x + i][y + j] = cells[i][j];
                }
            }
        }
    }

    public void removeBlock(Block block, int x, int y) {
        for (int i = 0; i < block.getHeight(); i++) {
            for (int j = 0; j < block.getWidth(); j++) {
                char[][] cells = block.getCells();
                if (cells[i][j] != '.') {
                    board[x + i][y + j] = '.';
                }
            }
        }
    }

    public boolean isFull() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == '.') {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // Testing
    }
}