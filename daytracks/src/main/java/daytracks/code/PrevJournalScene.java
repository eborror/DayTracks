package daytracks.code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static daytracks.code.DayTracksGUI.buttonStyle;
import static daytracks.code.DayTracksGUI.labelStyle;
import static daytracks.code.DayTracksGUI.mainStyle;
import static daytracks.code.DayTracksGUI.textBoxStyle;
import static daytracks.code.DayTracksGUI.titleStyle;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PrevJournalScene {
    public static void prevJournals(String title) {
        // Creating Header
        Label titleLabel = new Label(title);
        titleLabel.setStyle(titleStyle);
        Button backButton = new Button("Back");
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(e -> DayTracksGUI.showMainScene());
        HBox header = new HBox(titleLabel, backButton);
        header.setAlignment(Pos.CENTER);

        // Make boxes to search for previous entries
        VBox prevjournalBox = new VBox(20);
        prevjournalBox.setAlignment(Pos.CENTER);
        prevjournalBox.setPadding(new Insets(20));

        Label searchLabel = new Label("Type the date you would like to view? (MM-DD-YYYY)");
        searchLabel.setStyle(labelStyle);


        TextField searchField = new TextField();
        searchField.setAlignment(Pos.CENTER);
        searchField.setMaxWidth(100);
        searchField.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-text-fill: black; -fx-font-size: 16px; -fx-padding: 5px;");
        Platform.runLater(() -> {
            searchField.lookup(".content").setStyle("-fx-background-color: #C1BCAC;");
        });
        Button searchButton = new Button("Search");
        searchButton.setStyle(buttonStyle);
        HBox searchButtonAndTextField = new HBox(10, searchField, searchButton);
        searchButtonAndTextField.setAlignment(Pos.CENTER);

        VBox searchingBox = new VBox(searchLabel, searchButtonAndTextField);
        searchingBox.setAlignment(Pos.CENTER);

        TextArea prevjournal = new TextArea("");
        prevjournal.setWrapText(true);
        prevjournal.setEditable(false);
        prevjournal.setFocusTraversable(false);
        prevjournal.setStyle(textBoxStyle);
        Platform.runLater(() -> {
            prevjournal.lookup(".content").setStyle("-fx-background-color: #C1BCAC;");
        });

        searchButton.setOnAction(e -> {
            String foundJournal = readPrevJournals(searchField.getText());
            prevjournal.setText(foundJournal);
        });

        prevjournalBox.getChildren().addAll(searchingBox, prevjournal);

        // Set Scene
        VBox layout = new VBox(20, header, prevjournalBox);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(mainStyle);
        Scene scene = new Scene(layout, 450, 600);
        DayTracksGUI.primaryStage.setScene(scene);
    }

    private static String readPrevJournals(String searchDate) {
        StringBuilder result = new StringBuilder();
        boolean isInEntry = false;

        // Search journal for pervious entry
        try (BufferedReader reader = new BufferedReader(new FileReader(TodaysJournalScene.file.getName()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check for a new entry marker
                if (line.startsWith("=== Journal " + searchDate)) {
                    isInEntry = true; // Start capturing lines
                } else if (line.startsWith("=== End ===")) {
                    isInEntry = false;
                } else if (isInEntry) {
                    result.append(line).append("\n");
                }
            }
        } catch (IOException e) {}

        if (result.toString().isEmpty()) {
            return "Error - could not find a journal entry matching the given date.";
        }
        else {
            return result.toString();
        }
    }
}
