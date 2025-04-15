module daytracks.code {
    requires javafx.controls;
    requires javafx.fxml;

    opens daytracks.code to javafx.fxml;
    exports daytracks.code;
}
