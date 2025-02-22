package src;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        IO io = new IO();
        
        String filePath = "6.txt";

        try {
            io.readInputFile(filePath);

            List<Block> blocks = io.getBlocks();
            Board board = io.getBoard();
            PuzzleSolver solver = new PuzzleSolver(board, blocks);
            solver.solvePuzzle();

            long duration = solver.getDuration();
            long totalCaseChecked = solver.getTotalCaseChecked();

            String outputFilePath = "6.txt";
            io.writeOutputFile(outputFilePath, solver.getBoard(), duration, totalCaseChecked);

            String outputImageFilePath = "6.png";
            io.writeOutputImage(outputImageFilePath, solver.getBoard());

        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
        }
    }
}
