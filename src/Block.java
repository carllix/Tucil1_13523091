package src;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private int height, width;
    private char[][] shape;
    private char id;

    public Block(char[][] shape, char id) {
        this.shape = shape;
        this.id = id;
        this.height = shape.length;
        this.width = shape[0].length;
    }
    
    public char[][] getShape() {
        return this.shape;
    }

    public char getId() {
        return this.id;
    }

    private Block rotateCW() {
        char[][] newShape = new char[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                newShape[i][j] = shape[height - j - 1][i];
            }
        }
        return new Block(newShape, id);
    }

    private Block flipHorizontal() {
        char[][] newShape = new char[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                newShape[i][j] = shape[i][width - j - 1];
            }
        }
        return new Block(newShape, id);
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

    public void printShape(char[][] shape) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(shape[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // Testing
    }
}
