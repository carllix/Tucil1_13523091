package src;

import java.util.List;

public class PuzzleSolver {
    private Board board;
    private List<Block> blocks;
    private long totalCaseChecked = 0;
    private long duration = 0;
    private boolean isSolved = false;

    public PuzzleSolver(Board board, List<Block> blocks) {
        this.board = board;
        this.blocks = blocks;
    }

    public long getTotalCaseChecked() {
        return this.totalCaseChecked;
    }

    public long getDuration() {
        return this.duration;
    }

    public Board getBoard() {
        return this.board;
    }

    public boolean isSolved() {
        return this.isSolved;
    }

    public boolean solve(int idx) {

        if(board.isFull()) {
            if (idx < blocks.size()) {
                return false;
            }
            return true;
        }

        if (idx == blocks.size()) {
            return false;
        }

        Block block = blocks.get(idx);
        List<Block> allRotationsAndFlips = block.getAllRotationsAndFlips();
        for (int i = 0; i < allRotationsAndFlips.size(); i++) {
            Block cells = allRotationsAndFlips.get(i);
            for (int x = 0; x < board.getRows(); x++) {
                for (int y = 0; y < board.getCols(); y++) {
                    if (board.canPlaceBlock(cells, x, y)) {
                        totalCaseChecked++;
                        board.placeBlock(cells, x, y);

                        if (solve(idx + 1))
                            return true;

                        board.removeBlock(cells, x, y);
                    }
                }
            }
        }
        return false;
    }

    public void solvePuzzle() {
        long startTime = System.currentTimeMillis();

        boolean isSolved = solve(0);
        this.isSolved = isSolved;

        if (!isSolved) {
            System.out.println("Solusi tidak ditemukan.");
        } else {
            System.out.println("Solusi ditemukan.");
            board.printBoard();
        }
        long endTime = System.currentTimeMillis();
        this.duration = endTime - startTime;

        System.out.println("\nWaktu pencarian: " + (this.duration) + " ms");
        System.out.println("\nBanyak kasus yang ditinjau: " + this.totalCaseChecked);
    }

    public static void main(String[] args) {
        // Testing
    }
}
