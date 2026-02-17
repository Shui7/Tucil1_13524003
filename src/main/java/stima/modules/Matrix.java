package stima.modules;

public class Matrix {
    private final int Row, Col;
    private char[][] Content;

    public char getElmt(int row, int col) {
        return this.Content[row][col];
    }

    public int getRow() {
        return this.Row;
    }

    public int getCol() {
        return this.Col;
    }

    public void setElmt(char x, int row, int col) {
        this.Content[row][col] = x;
    }

    public boolean isSquare() {
        return this.Row == this.Col;
    }

    public Matrix() {
        this.Row = 0;
        this.Col = 0;
    }

    public Matrix(int row, int col) {
        this.Row = row;
        this.Col = col;
        this.Content = new char[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                this.Content[i][j] = 0;
            }
        }
    }
    public Matrix(int size) {
        this.Row = size;
        this.Col = size;
        this.Content = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.Content[i][j] = 0;
            }
        }
    }

    public Matrix(Matrix other) {
        this.Row = other.Row;
        this.Col = other.Col;
        this.Content = new char[Row][Col];

        for (int i = 0; i < this.Row; i++) {
            System.arraycopy(other.Content[i], 0, this.Content[i], 0, this.Col);
        }
    }

    @Override
    public String toString() {
        String result = "";
        for(int i = 0; i < this.Row; i++){
            for(int j = 0; j < this.Col; j++){
                result += String.format("%c", this.getElmt(i, j));
            }
            result += "\n";
        }
        return result;
    }
}