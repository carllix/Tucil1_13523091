package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GUI extends JFrame {
    private JButton loadButton, solveButton, savePngButton, saveTxtButton;
    private JPanel boardPanel;
    private JLabel statusLabel;
    private IO io;
    private PuzzleSolver solver;
    private Board board;
    private List<Block> blocks;
    private String currentInputFileName;

    public GUI() {
        setTitle("IQ Puzzler Pro Solver");
        setSize(720, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        loadButton = new JButton("Unggah File Puzzle (.txt)");
        solveButton = new JButton("Selesaikan");
        savePngButton = new JButton("Simpan sebagai PNG");
        saveTxtButton = new JButton("Simpan sebagai TXT");

        topPanel.add(loadButton);
        topPanel.add(solveButton);
        topPanel.add(savePngButton);
        topPanel.add(saveTxtButton);

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(6, 6));

        statusLabel = new JLabel(" ");

        add(topPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        io = new IO();

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();

                    currentInputFileName = file.getName().replaceFirst("[.][^.]+$", "");

                    try {
                        board = null;
                        blocks = null;
                        solver = null;
                        boardPanel.removeAll();
                        boardPanel.revalidate();
                        boardPanel.repaint();

                        io.readInputFile(file.getAbsolutePath());
                        blocks = io.getBlocks();
                        board = io.getBoard();

                        if (board == null || blocks == null || blocks.isEmpty()) {
                            throw new IOException("File tidak valid atau tidak berisi data yang cukup.");
                        }

                        int rows = board.getRows();
                        int cols = board.getCols();
                        boardPanel.setLayout(new GridLayout(rows, cols));
                        drawBoard();

                        JOptionPane.showMessageDialog(null, "File berhasil dimuat.", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat memuat file: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (board != null && blocks != null) {
                    solver = new PuzzleSolver(board, blocks);
                    solver.solvePuzzle();
                    boolean isSolved = solver.isSolved();
        
                    if (!isSolved) {
                        statusLabel.setText("Waktu pencarian: - | Kasus diuji: -");
                        JOptionPane.showMessageDialog(null, "Solusi tidak ditemukan.", "Solution",
                                JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
        
                    drawBoard();
                    statusLabel.setText("Waktu pencarian: " + solver.getDuration() + " ms | Banyak kasus yang ditinjau: " + solver.getTotalCaseChecked());
                    
                    JOptionPane.showMessageDialog(null,
                            "Waktu pencarian: " + solver.getDuration() + " ms\n" + "Banyak kasus yang ditinjau: "
                                    + solver.getTotalCaseChecked(),
                            "Solution",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Tidak ada puzzle yang dimuat untuk diselesaikan.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        savePngButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (solver != null && currentInputFileName != null) {
                    io.writeOutputImage(currentInputFileName + ".png", solver.getBoard());
                    JOptionPane.showMessageDialog(null,
                            "Gambar berhasil disimpan di: " + "test/output/image/" + currentInputFileName + ".png",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Tidak ada solusi yang dapat disimpan.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        saveTxtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (solver != null && currentInputFileName != null) {
                    io.writeOutputFile(currentInputFileName + ".txt", solver.getBoard(), solver.getDuration(),
                            solver.getTotalCaseChecked());
                    JOptionPane.showMessageDialog(null,
                            "Ouput berhasil disimpan di: " + "test/output/file/" + currentInputFileName + ".txt",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Tidak ada solusi yang dapat disimpan.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void drawBoard() {
        boardPanel.removeAll();

        if (board == null) {
            JOptionPane.showMessageDialog(null, "Board atau belum diinisialisasi.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int rows = board.getRows();
        int cols = board.getCols();
        boardPanel.setLayout(new GridLayout(rows, cols));

        char[][] solution = board.getBoard();
        String[] colors = board.getColors();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char cell = solution[i][j];
                if (cell == '%') {
                    cell = ' ';
                }
                JLabel label = new JLabel(String.valueOf(cell), SwingConstants.CENTER);
                label.setOpaque(true);

                if (cell >= 'A' && cell <= 'Z') {
                    int colorIdx = cell - 'A';
                    String ansiColor = colors[colorIdx];
                    label.setBackground(IO.convertAnsiToColor(ansiColor));
                } else if (cell == '.') {
                    label.setBackground(Color.BLACK);
                } else {
                    label.setBackground(Color.WHITE);
                }

                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                label.setFont(label.getFont().deriveFont(20f));
                boardPanel.add(label);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.setVisible(true);
        });
    }
}
