package stima.modules;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;

enum charToColor {
    A("#ff0000"),
    B("#ff8000"),
    C("#ffbf00"),
    D("#ffff00"),
    E("#bfff00"),
    F("#80ff00"),
    G("#00ff00"),
    H("#00ff80"),
    I("#00ffbf"),
    J("#00ffff"),
    K("#00bfff"),
    L("#0080ff"),
    M("#0000ff"),
    N("#8000ff"),
    O("#bf00ff"),
    P("#ff00ff"),
    Q("#ff80ff"),
    R("#ffb0ff"),
    S("#ffffff"),
    T("#bbbbbb"),
    U("#888888"),
    V("#444444"),
    W("#000000"),
    X("#400000"),
    Y("#800000"),
    Z("#bf0000");
    private final String hex;

    charToColor(String hex) {
        this.hex = hex;
    }

    public String hex() {
        return hex;
    }
}

public class Controller implements Initializable {

    Matrix m;
    int n;
    static Button[][] colorGrid = null;
    private final Image image = new Image(getClass().getResourceAsStream("/images/queen.png"));
    private Task<Void> backgroundTask;
    private String fileNameInserted = "";

    @FXML
    private TextField textNumberOfQueen;

    @FXML
    private TextArea textBoardConfig;

    @FXML
    private Label textFileInserted;

    @FXML
    private Label textStatusLeft;

    @FXML
    private Label textStatusRight;

    @FXML
    private GridPane grid;

    @FXML
    private CheckBox checkBoxBacktrack;

    @FXML
    private CheckBox checkBoxShowStep;

    @FXML
    private TextField textNStep;

    @FXML
    private TextField textMStep;

    @FXML
    private TextArea textSolution;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void changeGrid() {
        try {
            String boardConfig = textBoardConfig.getText();

            if(boardConfig.equals("")) {
                n = Integer.parseInt(textNumberOfQueen.getText());
                m = new Matrix(n);
                for(int i = 0; i < n; i++) {
                    for(int j = 0; j < n; j++) {
                        m.setElmt('A', i, j);
                    }
                }
            }
            else {
                m = Input.textToMatrix(boardConfig);
                if(m.getRow() == 0) throw new InputMismatchException("Text format not valid!");
                n = m.getRow();
                textNumberOfQueen.setText(String.valueOf(m.getRow()));
            }

            cancelCalculation();
            textStatusLeft.setText("Grid changed");

            grid.getChildren().clear();
            grid.getColumnConstraints().clear();
            grid.getRowConstraints().clear();

            colorGrid = new Button[n][n];
            for(int i = 0; i < n; i++) {
                for(int j = 0; j < n; j++) {
                    colorGrid[i][j] = new Button();
                    colorGrid[i][j].setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    colorGrid[i][j].setStyle("-fx-background-color: " + charToColor.valueOf(String.valueOf(m.getElmt(i, j))).hex());
                    colorGrid[i][j].setMinSize(0, 0);
                    colorGrid[i][j].setPrefSize(80, 80);
                    colorGrid[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    final int row = i;
                    final int col = j;
                    colorGrid[i][j].setOnAction(e -> {
                        changeColor(colorGrid[row][col]);
                    });

                    grid.add(colorGrid[i][j], j, i);
                    GridPane.setMargin(colorGrid[i][j], new Insets(1, 1, 1, 1));
                }
            }

            for(int i = 0; i < n; i++) {
                ColumnConstraints column = new ColumnConstraints();
                grid.getColumnConstraints().add(column);
                column.setPercentWidth(100.0/n);

                RowConstraints row = new RowConstraints();
                grid.getRowConstraints().add(row);
                row.setPercentHeight(100.0/n);
            }

            textBoardConfig.setText(m.toString());
        } catch (NumberFormatException e) {
            textStatusLeft.setText("Error: Number of queen is invalid!");
        } catch (InputMismatchException e) {
            textStatusLeft.setText("Error: " + e.getMessage());
        }
    }

    public void insertFile(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            File f = fileChooser.showOpenDialog(stage);
            String fileName = f.getName();
            if(!fileName.endsWith(".txt")) throw new IllegalArgumentException("File's extension is not .txt");

            String boardConfig = Input.inputFile(f);
            if(boardConfig.equals("")) throw new InputMismatchException("Text format not valid!");
            textBoardConfig.setText(boardConfig);

            textStatusLeft.setText(String.format("File inserted: %s", fileName));
            textFileInserted.setText(String.format("Last file inserted: %s", fileName));
            fileNameInserted = fileName;
        } catch(IllegalArgumentException | InputMismatchException e) {
            textStatusLeft.setText("Error: " + e.getMessage());
        } catch(NullPointerException e) {}
    }

    public void changeColor(Button color) {
        cancelCalculation();

        int row = GridPane.getRowIndex(color);
        int col = GridPane.getColumnIndex(color);

        char character = m.getElmt(row, col);
        character++;
        if(character - 65 >= n) character = 'A';
        m.setElmt(character, row, col);

        color.setStyle("-fx-background-color: " + charToColor.valueOf(String.valueOf(character)).hex());
        colorGrid[row][col] = color;

        textBoardConfig.setText(m.toString());
    }

    public void calculate() {
        HashSet<Character> colorSet = new HashSet<Character>();

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                colorSet.add(m.getElmt(i, j));
            }
        }

        if(colorSet.size() != n) {
            textStatusLeft.setText("Error: Number of colors not the same as number of row/column");
            return;
        }

        cancelCalculation();

        if((checkBoxShowStep.isSelected() && !textNStep.getText().matches("^[0-9]+$") && !textMStep.getText().matches("^[0-9]+$"))
        || (checkBoxShowStep.isSelected() && (Integer.parseInt(textNStep.getText()) < 1 || Integer.parseInt(textMStep.getText()) < 0))) {
            textStatusLeft.setText("Error: Invalid input on Show step...");
            return;
        }

        textStatusRight.setText("Calculating...");

        try {
            Thread.sleep(10);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }   

        Result solution = new Result();
        if(!checkBoxBacktrack.isSelected()) {
            exhaustiveSolveGUI(m, solution);
        }
        else {
            backtrackSolveGUI(m, solution);
        }
    }

    public void cancelCalculation() {
        if(backgroundTask != null && backgroundTask.isRunning()) {
            backgroundTask.cancel();
        }
    }
    
    private void displayResult(Result solution) {
        String result = "";

        if(solution.position[0][0] == -1) {
            result += "Tidak ada solusi!";
            textStatusRight.setText("Calculation complete: no solution");
        }
        else{
            Matrix newBoard = new Matrix(m);
            for(int i = 0; i < n; i++) {
                if(solution.position[i][0] == -1) break;
                    newBoard.setElmt('#', solution.position[i][1], solution.position[i][0]);
                }
            result += newBoard.toString();
            textStatusRight.setText("Calculation complete: solution found");
        }
        result += "\n";

        result += "Waktu pencarian: ";
        String timeText = Long.toString(solution.calculationTime);
        // To get accurate time without floating point error
        for(int i = 0; i < timeText.length(); i++) {
            result += timeText.charAt(i);
            if(timeText.length() - i == 7) result += '.';
        }
        result += " ms\n";

        result += String.format("Banyak kasus yang ditinjau: %d kasus\n", solution.cases);

        displayQueen(solution.position);
        textSolution.setText(result);
    }

    void backtrackSolveGUI(Matrix board, Result result) {
        boolean showStep = checkBoxShowStep.isSelected();
        int nStep;
        if(showStep) nStep = Integer.parseInt(textNStep.getText());
        else nStep = 1;
        int mStep;
        if(showStep) mStep = Integer.parseInt(textMStep.getText());
        else mStep = 1;

        backgroundTask = new Task<Void>() {
            @Override
            public Void call() {
                long startTime = System.nanoTime();
                int cases = 0;
                
                int[][] position = new int[n][2];
                for(int i = 0; i < n; i++) {
                    position[i][0] = -1;
                    position[i][1] = 0;
                }

                int placed = 0;
                // Candidate position to place the next queen
                int newX = -1;
                int newY = 0;
                boolean possible = true;
                
                while(placed < n && possible) {
                    if (isCancelled()) {
                        System.out.println("Task cancelled");
                        return null;
                    }

                    // Increment algorithm so it doesn't overflow beyond board's size
                    newX++;
                    if(newX >= n) {
                        newX = 0;
                        newY += 1;
                    }
                    if(newY >= n) {
                        placed--;
                        if(placed < 0) {
                            possible = false;
                            break;
                        }
                        if(placed >= 0) {
                            newX = position[placed][0];
                            newY = position[placed][1];
                            position[placed][0] = -1;
                            position[placed][1] = 0;
                        }
                        continue;
                    }

                    cases++;

                    int[][] snapshot = new int[n][2];
                    for(int i = 0; i < n; i++) {
                        snapshot[i][0] = position[i][0];
                        snapshot[i][1] = position[i][1];
                    }
                    snapshot[placed][0] = newX;
                    snapshot[placed][1] = newY; 
                    final int[][] finalPos = snapshot;
                    if(!isCancelled() && showStep && cases % nStep == 0){
                        Platform.runLater(() -> {
                            if (backgroundTask == null || backgroundTask.isCancelled()) {
                                return;
                            }
                            displayQueen(finalPos);
                        });
                        try {
                            Thread.sleep(mStep);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    // Check if the candidate position is valid
                    // Valid: record the position in the position array
                    // Not valid: do nothing
                    if(Algorithm.isValid(board, position, newX, newY)) {
                        position[placed][0] = newX;
                        position[placed][1] = newY;
                        placed++;
                    }
                }
                
                if(possible) {
                    System.out.println("Possible to solve! Solution:");
                    for(int i = 0; i < n; i++) {
                        System.out.printf("Pos %d: (%d,%d)\n", i+1, position[i][0], position[i][1]);
                    }
                }
                
                long endTime = System.nanoTime();
                long time = endTime - startTime;
                
                result.position = position;
                result.calculationTime = time;
                result.cases = cases;
                
                return null;
            }
        };
        backgroundTask.setOnSucceeded(e -> {
            displayResult(result);
        });
        backgroundTask.setOnCancelled(e -> {
            for(int row = 0; row < n; row++) {
                for(int col = 0; col < n; col++) {
                    colorGrid[row][col].setGraphic(null);
                }
            }
            textStatusRight.setText("Calculation cancelled");
        });
        new Thread(backgroundTask).start();
    }

    void exhaustiveSolveGUI(Matrix board, Result result) {
        boolean showStep = checkBoxShowStep.isSelected();
        int nStep;
        if(showStep) nStep = Integer.parseInt(textNStep.getText());
        else nStep = 1;
        int mStep;
        if(showStep) mStep = Integer.parseInt(textMStep.getText());
        else mStep = 1;

        backgroundTask = new Task<Void>() {
            @Override
            public Void call() {
                long startTime = System.nanoTime();
                int cases = 1;
                
                int[][] position = new int[n][2];
                for(int i = 0; i < n; i++) {
                    position[i][0] = i;
                    position[i][1] = 0;
                }

                int placed = n;
                int toMove = n-1;
                boolean possible = true;
                
                while(toMove < n && possible) {
                    if (isCancelled()) {
                        System.out.println("Task cancelled");
                        return null;
                    }

                    int[][] snapshot = new int[n][2];
                    for(int i = 0; i < n; i++) {
                        snapshot[i][0] = position[i][0];
                        snapshot[i][1] = position[i][1];
                    }
                    final int[][] finalPos = snapshot;
                    final int finalPlaced = placed;
                    if(!isCancelled() && showStep && cases % nStep == 0){
                        Platform.runLater(() -> {
                            if (backgroundTask == null || backgroundTask.isCancelled()) {
                                return;
                            }
                            if(finalPlaced == n){
                                displayQueen(finalPos);
                            }
                        });
                        try {
                            Thread.sleep(mStep);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    if(placed == n) {
                        if(Algorithm.isValidAll(board, position)) {
                            break;
                        }
                        cases++;
                    }

                    int newX = position[toMove][0];
                    int newY = position[toMove][1];
                    
                    if(newX == -1) {
                        newX = position[toMove-1][0];
                        newY = position[toMove-1][1];
                        placed++;
                    }
                    
                    newX++;
                    if(newX >= n) {
                        newX = 0;
                        newY += 1;
                    }
                    if(newY >= n) {
                        position[toMove][0] = -1;
                        position[toMove][1] = 0;
                        placed--;
                        toMove--;
                        if(toMove < 0) {
                            possible = false;
                            break;
                        }
                        continue;
                    }
                    else {
                        position[toMove][0] = newX;
                        position[toMove][1] = newY;
                    }
                    
                    if(toMove < n-1) {
                        toMove++;
                    }
                }
                
                long endTime = System.nanoTime();
                long time = endTime - startTime;
                
                result.position = position;
                result.calculationTime = time;
                result.cases = cases;
                
                return null;
            }
        };
        backgroundTask.setOnSucceeded(e -> {
            displayResult(result);
        });
        backgroundTask.setOnCancelled(e -> {
            for(int row = 0; row < n; row++) {
                for(int col = 0; col < n; col++) {
                    colorGrid[row][col].setGraphic(null);
                }
            }
            textStatusRight.setText("Calculation cancelled");
        });
        new Thread(backgroundTask).start();
    }

    void displayQueen(int[][] position) {
        for(int row = 0; row < n; row++) {
            for(int col = 0; col < n; col++) {
                colorGrid[row][col].setGraphic(null);
            }
        }
        for(int i = 0; i < n; i++) {
            if(position[i][0] == -1) break;
            ImageView imageQueen = new ImageView(image);
            imageQueen.setFitWidth(60);
            imageQueen.setFitHeight(60);
            colorGrid[position[i][1]][position[i][0]].setGraphic(imageQueen);
        }
    }

    public void saveTxt(ActionEvent event) {
        String toSave = textSolution.getText();

        if(toSave.equals("")) {
            textStatusLeft.setText("Error: No solution to save");
            return;
        }

        try {
            FileChooser fileChooser = new FileChooser();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            fileChooser.setInitialFileName(String.format("[Solution] %s", fileNameInserted));
            File f = fileChooser.showSaveDialog(stage);
            String filePath = f.getAbsolutePath();
            if(!filePath.endsWith(".txt")) {
                filePath += ".txt";
                f = new File(filePath);
            }
            
            try (FileWriter fileWriter = new FileWriter(f)) {
                fileWriter.write(toSave);
                textStatusLeft.setText(String.format("File saved: %s", filePath));
            }

        } catch(IllegalArgumentException | InputMismatchException | IOException e) {
            textStatusLeft.setText("Error: " + e.getMessage());
        } catch(NullPointerException e) {}
    }

    public void savePng(ActionEvent event) {
        WritableImage toSave = grid.snapshot(null, null);

        if(toSave == null) {
            textStatusLeft.setText("Error: No solution to save");
            return;
        }

        try {
            FileChooser fileChooser = new FileChooser();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            String fileName = fileNameInserted;
            String fileNamePng = fileName.substring(0, fileName.length() - 4) + ".png";
            fileChooser.setInitialFileName(String.format("[Solution] %s", fileNamePng));
            File f = fileChooser.showSaveDialog(stage);
            String filePath = f.getAbsolutePath();
            if(!filePath.endsWith(".png")) {
                filePath += ".png";
                f = new File(filePath);
            }
            
            if(!ImageIO.write(SwingFXUtils.fromFXImage(toSave, null), "png", f)) throw new IOException("Failed to save");
            
            textStatusLeft.setText(String.format("File saved: %s", filePath));

        } catch(IllegalArgumentException | InputMismatchException | IOException e) {
            textStatusLeft.setText("Error: " + e.getMessage());
        } catch(NullPointerException e) {}
    }
}