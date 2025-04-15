package daytracks.code;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class DayTracksGUI extends Application {
    public static Stage primaryStage;
    
    //Styling for boxes
    public static String mainStyle = "-fx-padding: 10px; -fx-background-color: #5C7457;";
    public static String titleStyle = "-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10px; -fx-text-fill: #F5E9D5;";
    public static String boxStyle = "-fx-padding: 10px; -fx-border-color: black; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-background-color: #C1BCAC; -fx-text-fill: black;";
    public static String textBoxStyle = "-fx-padding: 10px; -fx-border-color: black; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-background-color: #C1BCAC; -fx-text-fill: black; -fx-font-size: 16px; -fx-padding: 5px;";
    public static String smallTitleStyle = "-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10px; -fx-text-fill: black;";
    public static String buttonStyle = "-fx-padding: 3px; -fx-border-color: black; -fx-border-radius: 2px; -fx-background-radius: 2px; -fx-background-color: #979B8D; -fx-text-fill: black;";
    public static String labelStyle = "-fx-font-size: 16px; -fx-padding: 5px; -fx-text-fill: #F5E9D5;";

    @Override
    public void start(Stage primaryStage) {
        DayTracksGUI.primaryStage = primaryStage;
        showMainScene();
    }

    public static void showMainScene() {
        // Title
        Label titleLabel = new Label("DayTracks");
        titleLabel.setStyle(titleStyle);

        // Journal Section
        Label journalLabel = new Label("Journal");
        journalLabel.setStyle(smallTitleStyle);

        Button startJournalBtn = new Button("Start / Continue Journal");
        startJournalBtn.setStyle(buttonStyle);
        startJournalBtn.setOnAction(e -> TodaysJournalScene.journalClass("Start / Continue Journal"));

        Button viewJournalBtn = new Button("View Previous Journals");
        viewJournalBtn.setStyle(buttonStyle);
        viewJournalBtn.setOnAction(e -> PrevJournalScene.prevJournals("View Previous Journals"));

        VBox journalBox = new VBox(10, journalLabel, startJournalBtn, viewJournalBtn);
        journalBox.setAlignment(Pos.TOP_CENTER);
        journalBox.setStyle(boxStyle);

        // Daily Questions Section
        Label questionsLabel = new Label("Daily Questions");
        questionsLabel.setStyle(smallTitleStyle);

        Button answerQuestionsBtn = new Button("Answer Today's Questions");
        answerQuestionsBtn.setStyle(buttonStyle);
        answerQuestionsBtn.setOnAction(e -> AnsweringQuesScene.questionsScene("Answer Today's Questions"));

        Button viewPastAnswersBtn = new Button("View Previous Days");
        viewPastAnswersBtn.setStyle(buttonStyle);
        viewPastAnswersBtn.setOnAction(e -> PrevQuestionsScene.prevQuestionsScene("View Previous Days"));

        VBox questionsBox = new VBox(10, questionsLabel, answerQuestionsBtn, viewPastAnswersBtn);
        questionsBox.setAlignment(Pos.TOP_CENTER);
        questionsBox.setStyle(boxStyle);

        // Main Layout
        VBox mainContent = new VBox(50, journalBox, questionsBox);
        mainContent.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, titleLabel, mainContent);
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle(mainStyle);

        Scene scene = new Scene(root, 450, 600);
        primaryStage.setTitle("DayTracks");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}