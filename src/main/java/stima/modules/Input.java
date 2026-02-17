package stima.modules;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Input {
    // public static String inputFileName(Scanner scanner) {
    //     System.out.print("Nama file: ");
    //     String string;
    //     string = scanner.nextLine();
    //     System.out.println("File name entered: " + string);

    //     return string;
    // }

    // public static boolean inputShowStepChoice(Scanner scanner) {
    //     System.out.print("Perlihatkan langkah? (Y/N): ");
    //     String string;
    //     while(true) {
    //         string = scanner.nextLine();
    //         if(!string.equalsIgnoreCase("y") && !string.equalsIgnoreCase("n")) throw new InputMismatchException("pilihan salah!");
    //         else {
    //             return string.equalsIgnoreCase("y");
    //         }
    //     }
    // }

    // public static String inputFile(String fileName) {
    //     File f = new File(fileName);
    //     String result = "";

    //     try(Scanner scanner = new Scanner(f)) {
    //         if(!scanner.hasNextLine()) throw new InputMismatchException("file kosong");
    //         while(scanner.hasNextLine()) {
    //             result += scanner.nextLine();
    //         }
    //         if(!result.matches("^[a-zA-Z]+$")) {
    //             result = "";
    //             throw new InputMismatchException("ada karakter bukan alfabet dalam file");
    //         } 
    //     } catch(FileNotFoundException e) {
    //         System.out.println("File tidak ditemukan.");
    //     } catch(InputMismatchException e) {
    //         System.out.println("Error: " + e.getMessage());
    //     }

    //     return result;
    // }

    public static String inputFile(File f) {
        String result = "";

        try(Scanner scanner = new Scanner(f)) {
            if(!scanner.hasNextLine()) throw new InputMismatchException("file kosong");
            while(scanner.hasNextLine()) {
                result += scanner.nextLine() + "\n";
            }
            if(!result.matches("^[a-zA-Z\n]+$")) {
                result = "";
                throw new InputMismatchException("ada karakter bukan alfabet dalam file");
            } 
        } catch(FileNotFoundException e) {
            System.out.println("File tidak ditemukan.");
        } catch(InputMismatchException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return result;
    }

    public static Matrix textToMatrix(String inputText) {
        InputStream inputStream = new ByteArrayInputStream(inputText.getBytes());
        InputStream dimensionInputStream = new ByteArrayInputStream(inputText.getBytes());
        Matrix m;

        try(Scanner scanner = new Scanner(inputStream)) {
            // Check the dimension of the matrix
            int row;
            int col;
            try (Scanner dimensionScanner = new Scanner(dimensionInputStream)) {
                if(!dimensionScanner.hasNextLine()) throw new InputMismatchException("file kosong");
                col = dimensionScanner.nextLine().toCharArray().length;
                row = 1;
                while(dimensionScanner.hasNextLine()) {
                    row++;
                    dimensionScanner.nextLine();
                }
            }

            if(row != col) throw new InputMismatchException("terbuat matriks bukan persegi");
            m = new Matrix(row, col);
            for(int i = 0; i < row; i++) {
                String str = scanner.nextLine();
                if(!str.matches("^[a-zA-Z]+$")) {
                    throw new InputMismatchException("ada karakter bukan alfabet");
                } 
                char[] input = str.toUpperCase().toCharArray();
                for(int j = 0; j < col; j++) {
                    m.setElmt(input[j], i, j);
                }
            }
        } catch(Exception e) {
            System.out.println("Error input " + e.getMessage());
            m = new Matrix(0,0);
        }

        return m;
    }

    // public static Matrix inputMatrix(String filename) {
    //     File f = new File(filename);
    //     Matrix m;

    //     try(Scanner scanner = new Scanner(f)) {
    //         // Check the dimension of the matrix
    //         int row;
    //         int col;
    //         try (Scanner dimensionScanner = new Scanner(f)) {
    //             if(!dimensionScanner.hasNextLine()) throw new InputMismatchException("file kosong");
    //             col = dimensionScanner.nextLine().toCharArray().length;
    //             row = 1;
    //             while(dimensionScanner.hasNextLine()) {
    //                 row++;
    //                 dimensionScanner.nextLine();
    //             }
    //         }

    //         m = new Matrix(row, col);
    //         for(int i = 0; i < row; i++) {
    //             char[] input = scanner.nextLine().toCharArray();
    //             for(int j = 0; j < col; j++) {
    //                 m.setElmt(input[j], i, j);
    //             }
    //         }
    //     } catch(FileNotFoundException e) {
    //         System.out.println("File tidak ditemukan.");
    //         m = new Matrix(0,0);
    //     } catch(InputMismatchException e) {
    //         System.out.println("Error: " + e.getMessage());
    //         m = new Matrix(0,0);
    //     }

    //     return m;
    // }
}
