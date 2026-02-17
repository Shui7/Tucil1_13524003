package stima;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    // public static void main(String[] args) {
    //     boolean showStep;
    //     String fileName;
    //     Matrix m;

    //     try(Scanner scanner = new Scanner(System.in)) {
    //         showStep = Input.inputShowStepChoice(scanner);
    //         fileName = Input.inputFileName(scanner);
    //         m = Input.inputMatrix(fileName);
    //     }

    //     System.out.print(m);

    //     Algorithm.solve(m, showStep);
    // }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader sceneLoader = new FXMLLoader(getClass().getResource("/fxml/MainPage.fxml"));

            Parent root = (Parent) sceneLoader.load();
            Scene scene = new Scene(root);

            stage.setTitle("Queen Solver");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load FXML file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
