module varsidy.keke {
    requires javafx.controls;
    requires javafx.fxml;


    opens varsidy.keke to javafx.fxml;
    exports varsidy.keke;
}