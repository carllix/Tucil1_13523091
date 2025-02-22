package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class IO {
    private List<Block> blocks = new ArrayList<>();
    private String type;
    private int rows, cols, totalBlock;
    private Board board;

    public List<Block> getBlocks() {
        return this.blocks;
    }

    public String getType() {
        return this.type;
    }

    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    public int getTotalBlock() {
        return this.totalBlock;
    }

    public void readInputFile(String filename) throws IOException {
        String inputFilePath = "test/input/" + filename;

        BufferedReader br = new BufferedReader(new FileReader(inputFilePath));

        try (br) {
            StringTokenizer tokenizer = new StringTokenizer(br.readLine());
            this.rows = Integer.parseInt(tokenizer.nextToken());
            this.cols = Integer.parseInt(tokenizer.nextToken());
            this.totalBlock = Integer.parseInt(tokenizer.nextToken());
            this.type = br.readLine().trim();

            processBlocks(br);
        }
    }

    private void processBlocks(BufferedReader br) throws IOException {
        List<String> currentShapeLines = new ArrayList<>();
        char currentBlockId = '\0';

        String currentLine;
        while ((currentLine = br.readLine()) != null) {
            char foundBlockId = findId(currentLine);

            if (foundBlockId != currentBlockId && !currentShapeLines.isEmpty()) {
                blocks.add(new Block(toCharMatrix(currentShapeLines)));
                currentShapeLines.clear();
            }

            currentShapeLines.add(currentLine);
            currentBlockId = foundBlockId;
        }

        if (!currentShapeLines.isEmpty()) {
            blocks.add(new Block(toCharMatrix(currentShapeLines)));
        }

        validateBlockCount();
    }

    private void validateBlockCount() {
        if (blocks.size() != totalBlock) {
            System.out.println("Jumlah block tidak sesuai, silahkan cek kembali file input!");
        }
    }

    private char findId(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (Character.isLetter(line.charAt(i))) {
                return line.charAt(i);
            }
        }
        return '.';
    }

    private char[][] toCharMatrix(List<String> lines) {
        int maxWidth = 0;
        for (String line : lines) {
            if (line.length() > maxWidth) {
                maxWidth = line.length();
            }
        }

        char[][] matrix = new char[lines.size()][maxWidth];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < maxWidth; j++) {
                if (j < lines.get(i).length()) {
                    matrix[i][j] = (lines.get(i).charAt(j) == ' ') ? '.' : lines.get(i).charAt(j);
                } else {
                    matrix[i][j] = '.';
                }
            }
        }

        return matrix;
    }

    public void writeOutputFile(String filename, Board board, long duration, long totalCaseChecked) {
        String outputDir = "test/output/file/";
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File outputFile = new File(outputDir + filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            char[][] boardState = board.getBoard();

            for (char[] row : boardState) {
                writer.write(new String(row));
                writer.newLine();
            }

            writer.newLine();
            writer.write("Waktu pencarian: " + duration + " ms");
            writer.newLine();
            writer.write("Banyak kasus yang ditinjau: " + totalCaseChecked);
            writer.flush();

            System.out.println("Output berhasil ditulis ke: " + outputDir + filename);
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat menulis file: " + e.getMessage());
        }
    }

    public void writeOutputImage(String filename, Board board) {
        int cellSize = 50;
        int rows = board.getRows();
        int cols = board.getCols();
        char[][] boardState = board.getBoard();

        int width = cols * cellSize;
        int height = rows * cellSize;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        String[] colors = board.getColors();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char cell = boardState[i][j];
                Color cellColor = Color.WHITE;

                if (cell >= 'A' && cell <= 'Z') {
                    int colorIdx = cell - 'A';
                    String ansiColor = colors[colorIdx];
                    cellColor = convertAnsiToColor(ansiColor);
                }

                g2d.setColor(cellColor);
                g2d.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);

                g2d.setColor(Color.BLACK);
                g2d.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }

        g2d.dispose();

        String outputDir = "test/output/image/";
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File outpuutFile = new File(outputDir + filename);

        try {
            ImageIO.write(image, "png", outpuutFile);
            System.out.println("Gambar berhasil disimpan di: " + outputDir + filename);
        } catch (IOException e) {
            System.out.println("Gagal menyimpan gambar: " + e.getMessage());
        }
    }

    private static Color convertAnsiToColor(String ansiColor) {
        switch (ansiColor) {
            case "\u001B[31m":
                return new Color(255, 0, 0);
            case "\u001B[32m":
                return new Color(0, 255, 0);
            case "\u001B[33m":
                return new Color(255, 255, 0);
            case "\u001B[34m":
                return new Color(0, 0, 255);
            case "\u001B[35m":
                return new Color(128, 0, 128);
            case "\u001B[36m":
                return new Color(0, 255, 255);
            case "\u001B[91m":
                return new Color(255, 102, 102);
            case "\u001B[92m":
                return new Color(102, 255, 102);
            case "\u001B[93m":
                return new Color(255, 255, 153);
            case "\u001B[94m":
                return new Color(102, 102, 255);
            case "\u001B[95m":
                return new Color(255, 102, 255);
            case "\u001B[96m":
                return new Color(102, 255, 255);
            case "\u001B[41m":
                return new Color(255, 0, 0);
            case "\u001B[42m":
                return new Color(0, 255, 0);
            case "\u001B[43m":
                return new Color(255, 255, 0);
            case "\u001B[44m":
                return new Color(0, 0, 255);
            case "\u001B[45m":
                return new Color(128, 0, 128);
            case "\u001B[46m":
                return new Color(0, 255, 255);
            case "\u001B[101m":
                return new Color(255, 102, 102);
            case "\u001B[102m":
                return new Color(102, 255, 102);
            case "\u001B[103m":
                return new Color(255, 255, 153);
            case "\u001B[104m":
                return new Color(102, 102, 255);
            case "\u001B[105m":
                return new Color(255, 102, 255);
            case "\u001B[106m":
                return new Color(102, 255, 255);
            case "\u001B[48;5;208m":
                return new Color(255, 140, 0);
            case "\u001B[48;5;129m":
                return new Color(139, 0, 139);
            default:
                return Color.WHITE;
        }
    }

    public static void main(String[] args) {
        // Testing
    }
}