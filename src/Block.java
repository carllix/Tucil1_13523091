package src;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private int height, width;
    private char[][] cells;

    public Block(char[][] cells) {
        this.cells = cells;
        this.height = cells.length;
        this.width = cells[0].length;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }
    
    public char[][] getCells() {
        return this.cells;
    }

    private Block rotateCW() {
        char[][] newCells = new char[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                newCells[i][j] = cells[height - j - 1][i];
            }
        }
        return new Block(newCells);
    }

    private Block flipHorizontal() {
        char[][] newCells = new char[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                newCells[i][j] = cells[i][width - j - 1];
            }
        }
        return new Block(newCells);
    }

    public List<Block> getAllRotationsAndFlips() {
        List<Block> rotationAndFlips = new ArrayList<>();
        Block current = this;

        for (int i = 0; i < 4; i++) {
            rotationAndFlips.add(current);
            rotationAndFlips.add(current.flipHorizontal());
            current = current.rotateCW();
        }

        return rotationAndFlips;
    }

    public void printCells() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(cells[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // Testing
    }
}
