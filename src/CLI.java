package src;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class CLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        IO io = new IO();

        System.out.println("=============================================");
        System.out.println("   Selamat Datang di IQ Puzzler Pro Solver");
        System.out.println("=============================================");

        System.out.print("\nMasukkan nama file input (contoh: 1.txt): ");
        String fileName = scanner.nextLine().trim();
        String filePath = "test/input/" + fileName;
        System.out.println();

        try {
            io.readInputFile(filePath);

            List<Block> blocks = io.getBlocks();
            Board board = io.getBoard();
            PuzzleSolver solver = new PuzzleSolver(board, blocks);
            solver.solvePuzzle();

            long duration = solver.getDuration();
            long totalCaseChecked = solver.getTotalCaseChecked();

            String choose = "";
            while (!choose.equalsIgnoreCase("Ya") && !choose.equalsIgnoreCase("Tidak")) {
                System.out.print("\nApakah Anda ingin menyimpan solusi? (Ya/Tidak): ");
                choose = scanner.nextLine().trim();
                if (!choose.equalsIgnoreCase("Ya") && !choose.equalsIgnoreCase("Tidak")) {
                    System.out.println("\nInput tidak valid! Harap masukkan 'Ya' atau 'Tidak'.");
                }
            }

            if (choose.equalsIgnoreCase("Ya")) {
                int option = -1;
                while (option < 1 || option > 3) {
                    System.out.println("\nPilih format penyimpanan:");
                    System.out.println("1. File (TXT)");
                    System.out.println("2. Image (PNG)");
                    System.out.println("3. File (TXT) + Image (PNG)");
                    System.out.print("\nPilihan (1-3): ");

                    if (scanner.hasNextInt()) {
                        option = scanner.nextInt();
                        scanner.nextLine();
                    } else {
                        System.out.println("\nInput tidak valid! Harap masukkan angka 1-3.");
                        scanner.nextLine();
                        continue;
                    }

                    switch (option) {
                        case 1:
                            io.writeOutputFile(fileName, solver.getBoard(), duration, totalCaseChecked);
                            break;
                        case 2:
                            io.writeOutputImage(fileName.replace(".txt", ".png"), solver.getBoard());
                            break;
                        case 3:
                            io.writeOutputFile(fileName, solver.getBoard(), duration, totalCaseChecked);
                            io.writeOutputImage(fileName.replace(".txt", ".png"), solver.getBoard());
                            break;
                        default:
                            System.out.println("\nPilihan tidak valid! Silakan coba lagi.");
                    }
                }
            } else {
                System.out.println("\nSolusi tidak disimpan.");
            }

        } catch (IOException e) {
            System.out.println("\nTerjadi kesalahan saat membaca file: " + e.getMessage());
        }

        scanner.close();
        System.out.println("\nTerima kasih telah menggunakan IQ Puzzler Pro Solver!");
    }
}