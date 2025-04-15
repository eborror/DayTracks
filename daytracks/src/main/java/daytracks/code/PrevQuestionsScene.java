package daytracks.code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class PrevQuestionsScene {
    public static void prevQuestionsScene(String title) {
        // Creating Header
        Label titleLabel = new Label(title);
        titleLabel.setStyle(titleStyle);
    
        Button backButton = new Button("Back");
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(e -> DayTracksGUI.showMainScene());
    
        HBox header = new HBox(20, titleLabel, backButton);
        header.setAlignment(Pos.CENTER);
    
        // Create boxes for Q1 and Q2 results
        VBox responseBoxes = new VBox(10);
        responseBoxes.setAlignment(Pos.CENTER);
    
        // Load and display squares for Q1
        HBox q1Box = new HBox();
        q1Box.setAlignment(Pos.CENTER);
        Label q1Label = new Label("Q1: Rate your day (oldest to newest)");
        q1Label.setStyle(labelStyle);
        responseBoxes.getChildren().addAll(q1Label, q1Box);
    
        List<Integer> q1Responses = readResponseFile(AnsweringQuesScene.q1File.toString());
        for (int value : q1Responses) {
            q1Box.getChildren().add(createColorSquare(value));
        }
    
        // Load and display squares for Q2
        HBox q2Box = new HBox();
        q2Box.setAlignment(Pos.CENTER);
        Label q2Label = new Label("Q2: How was your mood? (oldest to newest)");
        q2Label.setStyle(labelStyle);
        responseBoxes.getChildren().addAll(q2Label, q2Box);
    
        List<Integer> q2Responses = readResponseFile(AnsweringQuesScene.q2File.toString());
        for (int value : q2Responses) {
            q2Box.getChildren().add(createColorSquare(value));
        }

        // Make boxes to search for previous entries
        VBox writtenResponsesBox = new VBox(20);
        writtenResponsesBox.setAlignment(Pos.CENTER);

        Label entrySearchLabel = new Label("Which entry number would you like to look at?");
        entrySearchLabel.setStyle(labelStyle);

        TextField entrySearchField = new TextField();
        entrySearchField.setAlignment(Pos.CENTER);
        entrySearchField.setMaxWidth(40);
        entrySearchField.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-text-fill: black; -fx-font-size: 16px; -fx-padding: 5px;");
        Platform.runLater(() -> {
            entrySearchField.lookup(".content").setStyle("-fx-background-color: #C1BCAC;");
        });

        Button searchButton = new Button("Search");
        searchButton.setStyle(buttonStyle);
        HBox searchButtonAndTextField = new HBox(entrySearchField, searchButton);
        searchButtonAndTextField.setAlignment(Pos.CENTER);

        VBox searchEntriesBox = new VBox(entrySearchLabel, searchButtonAndTextField);
        searchEntriesBox.setAlignment(Pos.CENTER);

        TextArea prevEntry = new TextArea("");
        prevEntry.setWrapText(true);
        prevEntry.setEditable(false);
        prevEntry.setFocusTraversable(false);
        prevEntry.setStyle(textBoxStyle);
        Platform.runLater(() -> {
            prevEntry.lookup(".content").setStyle("-fx-background-color: #C1BCAC;");
        });

        searchButton.setOnAction(e -> {
            String entryNumString = entrySearchField.getText();
            try {
                int entryNum = Integer.parseInt(entryNumString);
                String foundEntry = readWrittenEntries(entryNum);
                prevEntry.setText(foundEntry);
            } catch (NumberFormatException a) {
                prevEntry.setText("Error must enter a valid number.");
            }
            
        });

        writtenResponsesBox.getChildren().addAll(searchEntriesBox, prevEntry);
    
        // Layout scene
        VBox layout = new VBox(20, header, responseBoxes, writtenResponsesBox);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle(mainStyle);
    
        /*ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-padding: 10px;");*/
    
        Scene scene = new Scene(layout, 450, 600);
        DayTracksGUI.primaryStage.setScene(scene);
    }
    
    // Reads a response file and returns a list of integers
    private static List<Integer> readResponseFile(String filename) {
        List<Integer> responses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    int value = Integer.parseInt(line.trim());
                    if (value >= 1 && value <= 5) {
                        responses.add(value);
                    }
                } catch (NumberFormatException e) {
                    // Ignore bad lines
                }
            }
        } catch (IOException e) {}
        return responses;
    }
    
    // Creates a color-coded square for each rating
    private static Region createColorSquare(int rating) {
        Region square = new Region();
        square.setPrefSize(30, 30);
    
        String color;
        switch (rating) {
            case 1: color = "darkred"; break;
            case 2: color = "red"; break;
            case 3: color = "yellow"; break;
            case 4: color = "greenyellow"; break;
            case 5: color = "green"; break;
            default: color = "gray"; break;
        }
    
        square.setStyle("-fx-background-color: " + color + ";");
        return square;
    }

    private static String readWrittenEntries(int entryNum) {
        StringBuilder result = new StringBuilder();
        boolean isInEntry = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(AnsweringQuesScene.file.getName()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check for a new entry marker
                if (line.startsWith("=== Entry")) {
                    if (line.contains("Entry " + entryNum)) {
                        isInEntry = true; // Start capturing lines
                        result.append(line).append("\n");
                    } else if (isInEntry) {
                        break; // We've reached the next entry; stop reading
                    }
                } else if (isInEntry) {
                    result.append(line).append("\n");
                }
            }
        } catch (IOException e) {}

        return result.toString();
    }
}
