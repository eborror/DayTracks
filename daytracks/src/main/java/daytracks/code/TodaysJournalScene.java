package daytracks.code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static daytracks.code.DayTracksGUI.buttonStyle;
import static daytracks.code.DayTracksGUI.mainStyle;
import static daytracks.code.DayTracksGUI.titleStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TodaysJournalScene {
    public static File file = new File("journal.txt");

    // Get current date
    private static final String DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")); //04-15-2025

    public static void journalClass(String title) {
        boolean isInEntry = false;
        String foundEntry;
        StringBuilder result = new StringBuilder();

        // Creating Header
        Label titleLabel = new Label(title);
        titleLabel.setStyle(titleStyle);

        Button saveButton = new Button("Save");
        saveButton.setStyle(buttonStyle);

        Button backButton = new Button("Back");
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(e -> DayTracksGUI.showMainScene());

        HBox headerButtons = new HBox(20, saveButton, backButton);
        headerButtons.setAlignment(Pos.CENTER);
        VBox header = new VBox(titleLabel, headerButtons);
        header.setAlignment(Pos.CENTER);

        TextArea journalArea = new TextArea();
        journalArea.setWrapText(true);
        journalArea.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-text-fill: black; -fx-font-size: 16px; -fx-padding: 5px;");

        // Write existing entry into box
        try (BufferedReader reader = new BufferedReader(new FileReader(file.getName()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check for a new entry marker
                if (line.startsWith("=== Journal " + DATE)) {
                    isInEntry = true; // Start capturing lines
                } else if (line.startsWith("=== End ===")) {
                    isInEntry = false;
                } else if (isInEntry) {
                    result.append(line).append("\n");
                }
                
            }
        } catch (IOException e) {}

        foundEntry = result.toString();        
        journalArea.setText(foundEntry);

        saveButton.setOnAction(e -> {
            String input = journalArea.getText();
            saveJournal(input);
            DayTracksGUI.showMainScene();
        });

        VBox journalVBox = new VBox(20, journalArea);
        journalVBox.setPadding(new Insets(20));

        VBox layout = new VBox(20, header, journalVBox);
        layout.setStyle(mainStyle);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 450, 600);
        DayTracksGUI.primaryStage.setScene(scene);
    }

    private static void saveJournal(String input) {
        StringBuilder fileContent = new StringBuilder();
    
        // Read the file and store the content that isn't today's entry
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isInEntry = false;
            boolean oneMore = false;
    
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("=== Journal " + DATE)) {
                    isInEntry = true; // Skip today's entry
                } 
                if (isInEntry && line.startsWith("=== End ===")) {
                    isInEntry = false; // End of today's entry
                    oneMore = true;
                    continue; // Skip the end marker
                }
                if (!isInEntry && oneMore && line.contains("\n")) {
                    fileContent.append(line);
                }
                if (!isInEntry && !oneMore) {
                    fileContent.append(line).append("\n");
                }
            }
        } catch (IOException e) {}
    
        // Ensure input ends with a newline before appending "=== End ==="
        if (!input.endsWith("\n")) {
            input += "\n";
        }
    
        // Append today's entry
        fileContent.append("=== Journal ").append(DATE).append(" ===\n");
        fileContent.append(input);
        fileContent.append("=== End ===\n");
    
        // Write updated content back to the file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(fileContent.toString());
        } catch (IOException e) {}
    }
}
