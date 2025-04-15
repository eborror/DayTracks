package daytracks.code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AnsweringQuesScene {
    //Styling
    private static final String BUTTON_STYLE = "-fx-padding: 3px; -fx-border-color: black; -fx-border-radius: 2px; -fx-background-radius: 2px; -fx-background-color: #979B8D; -fx-text-fill: black;";
    private static final String TEXT_STYLE = "-fx-font-size: 18px; -fx-text-fill: black;";
    private static final String BOX_STYLE = "-fx-padding: 10px; -fx-border-color: black; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-background-color: #C1BCAC; -fx-text-fill: black;";

    private static String q1Answer = "";
    private static String q2Answer = "";
    private final static List<TextField> writtenAnswers = new ArrayList<>();
    public static File file = new File("writtenResponses.txt");
    public static File q1File = new File("q1Responses.txt");
    public static File q2File = new File("q2Responses.txt");

    public static void questionsScene(String title) {
        // Creating Header
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #F5E9D5;");

        Button saveButton = new Button("Save");
        saveButton.setStyle(BUTTON_STYLE);
        saveButton.setOnAction(e -> {
            saveResponsesToFile();
            DayTracksGUI.showMainScene();
        });

        Button backButton = new Button("Back");
        backButton.setStyle(BUTTON_STYLE);
        backButton.setOnAction(e -> DayTracksGUI.showMainScene());

        HBox header = new HBox(20, titleLabel, saveButton, backButton);
        header.setAlignment(Pos.CENTER);

        VBox questionVBox = new VBox(10);

        // Question 1 - Rate your day
        questionVBox.getChildren().add(createRatingQuestion("Rate your day!", 1));

        // Question 2 - Mood
        questionVBox.getChildren().add(createRatingQuestion("How was your mood?", 2));

        // Clear previous inputs in case this scene is revisited
        writtenAnswers.clear();

        // Written Questions
        String[] questions = {
            "What did you accomplish?",
            "What went well today?",
            "What could you improve on?",
            "What was the highlight of your day?",
            "What challenged you today?",
            "What can you do better tomorrow?",
            "Did you complete your work?",
            "Give a word to sum up your day."
        };

        for (String qText : questions) {
            VBox box = new VBox(5);
            box.setStyle(BOX_STYLE);
            box.setMinWidth(300);

            Label questionLabel = new Label(qText);
            questionLabel.setStyle(TEXT_STYLE);

            TextField answer = new TextField();
            answer.setStyle("-fx-padding: 10px; -fx-border-color: black;");
            VBox.setMargin(answer, new Insets(0, 0, 0, 20));

            writtenAnswers.add(answer);

            box.getChildren().addAll(questionLabel, answer);
            questionVBox.getChildren().add(box);
        }

        VBox layout = new VBox(20, header, questionVBox);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #5C7457;");

        ScrollPane scrollLayout = new ScrollPane(layout);
        scrollLayout.setStyle("-fx-padding: 10px; -fx-background-color: #5C7457;");
        scrollLayout.setFitToWidth(true);

        Scene scene = new Scene(scrollLayout, 450, 600);
        DayTracksGUI.primaryStage.setScene(scene);
    }

    // Creates q1 and q2
    private static VBox createRatingQuestion(String questionText, int questionNumber) {
        VBox qBox = new VBox(5);
        qBox.setStyle(BOX_STYLE);
        qBox.setMinWidth(300);

        Label qLabel = new Label(questionText);
        qLabel.setStyle(TEXT_STYLE);

        HBox answerBox = new HBox(5);
        Button[] buttons = new Button[5];
        String[] labels = { "Awful", "Bad", "Ok", "Good", "Great" };

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            Button button = new Button(label);
            button.setStyle(BUTTON_STYLE);
            buttons[i] = button;

            button.setOnAction(e -> {
                switch (label) {
                    case "Awful":
                        button.setStyle("-fx-background-color: darkred;");
                        break;
                    case "Bad":
                        button.setStyle("-fx-background-color: red;");
                        break;
                    case "Ok":
                        button.setStyle("-fx-background-color: yellow;");
                        break;
                    case "Good":
                        button.setStyle("-fx-background-color: greenyellow;");
                        break;
                    case "Great":
                        button.setStyle("-fx-background-color: green;");
                        break;
                }

                for (Button b : buttons) {
                    if (b != button) b.setDisable(true);
                }

                if (questionNumber == 1) {
                    q1Answer = label;
                } else if (questionNumber == 2) {
                    q2Answer = label;
                }
            });

            answerBox.getChildren().add(button);
        }

        qBox.getChildren().addAll(qLabel, answerBox);
        return qBox;
    }

    /* Save responses to three files 
     * one for q1 answers
     * one for q2 answers 
     * the other for the written responses
     */
    private static void saveResponsesToFile() {
        int entryNumber = 1;
        int q1AnswerInt = 0;
        int q2AnswerInt = 0;

        // Count existing entries if file exists
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("=== Entry")) {
                        entryNumber++;
                    }
                }
            } catch (IOException e) {}
        }

        // Get current date
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));

        // Save new entry for written responses
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            writer.println("=== Entry " + entryNumber + " (" + date + ") ===");

            String[] questions = {
                "What did you accomplish?",
                "What went well today?",
                "What could you improve on?",
                "What was the highlight of your day?",
                "What challenged you today?",
                "What can you do better tomorrow?",
                "Did you complete your work?",
                "Give a word to sum up your day."
            };

            for (int i = 0; i < questions.length; i++) {
                writer.println((i + 3) + " - " + questions[i]);
                writer.println("Answer: " + writtenAnswers.get(i).getText());
            }

            writer.println(); // Add a newline between entries
        } catch (IOException e) {}

        //Convert Q1 answers to int
        switch (q1Answer) {
            case "Awful": q1AnswerInt = 1; break;
            case "Bad": q1AnswerInt = 2; break;
            case "Ok": q1AnswerInt = 3; break;
            case "Good": q1AnswerInt = 4; break;
            case "Great": q1AnswerInt = 5; break;
        }

        // Save new entry for Q1 responses
        try (PrintWriter writer = new PrintWriter(new FileWriter(q1File, true))) {
            writer.println(q1AnswerInt);
        } catch (IOException e) {}

        //Convert Q2 answers to int
        switch (q2Answer) {
            case "Awful": q2AnswerInt = 1; break;
            case "Bad": q2AnswerInt = 2; break;
            case "Ok": q2AnswerInt = 3; break;
            case "Good": q2AnswerInt = 4; break;
            case "Great": q2AnswerInt = 5; break;
        }

        // Save new entry for Q2 responses
        try (PrintWriter writer = new PrintWriter(new FileWriter(q2File, true))) {
            writer.println(q2AnswerInt);
        } catch (IOException e) {}
    }
}
