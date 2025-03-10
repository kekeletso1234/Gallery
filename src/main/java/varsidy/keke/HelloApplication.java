package varsidy.keke;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloApplication extends Application {

    private int currentIndex = 0;
    private final String[] images = new String[19];

    @Override
    public void start(Stage mainStage) {
        mainStage.setTitle("Interactive Image Viewer");

        FlowPane galleryPane = new FlowPane();
        galleryPane.setVgap(25);
        galleryPane.setHgap(25);
        galleryPane.setPadding(new Insets(15));

        // Set a publicly accessible background image
        galleryPane.setStyle("-fx-background-image: url('https://www.w3schools.com/w3images/forestbridge.jpg'); -fx-background-size: cover; " +
                "-fx-opacity: 0.9");

        for (int i = 0; i < 19; i++) {
            String imgPath = "file:Images/keke" + (i + 1) + ".jpg";
            images[i] = imgPath;

            ImageView thumbView = new ImageView();
            try {
                Image thumbImage = new Image(imgPath);
                thumbView.setImage(thumbImage);
            } catch (Exception e) {
                System.err.println("Error loading image: " + imgPath);
            }

            thumbView.setFitWidth(150);
            thumbView.setFitHeight(100);
            thumbView.setStyle("-fx-opacity: 0.7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 6, 0.0, 3, 3);");

            int finalI = i;

            thumbView.setOnMouseClicked(event -> {
                applyClickTransition(thumbView);
                openImageView(finalI);
            });

            thumbView.setOnMouseEntered(event -> {
                applyHoverTransition(thumbView, 1.2, 1.2); // Grow slightly
                thumbView.setStyle("-fx-opacity: 1.0; -fx-effect: dropshadow(gaussian, rgba(215,239,120,0.8), 8, 0.0, 4, 4);");
            });

            thumbView.setOnMouseExited(event -> {
                applyHoverTransition(thumbView, 1.0, 1.0); // Shrink back to normal
                thumbView.setStyle("-fx-opacity: 0.7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 6, 0.0, 3, 3);");
            });

            galleryPane.getChildren().add(thumbView);
        }

        Button exitGalleryButton = new Button("Exit Gallery");
        exitGalleryButton.setFont(Font.font("Arial", 18));
        exitGalleryButton.setStyle("-fx-background-color:linear-gradient(from 60% 90% to 100% 100%, #650707 , #904fca); -fx-text-fill: #d1a9a9; -fx-padding: 8px;");
        exitGalleryButton.setOnAction(event -> mainStage.close());

        galleryPane.getChildren().add(exitGalleryButton);

        Scene mainScene = new Scene(galleryPane, 850, 650);
        mainStage.setScene(mainScene);
        mainStage.show();
    }
    private void applyHoverTransition(ImageView thumbView, double toX, double toY) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), thumbView);
        scaleTransition.setToX(toX);
        scaleTransition.setToY(toY);
        scaleTransition.play();
    }

    private void applyClickTransition(ImageView thumbView) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(300), thumbView);
        translateTransition.setByY(-10);
        translateTransition.setAutoReverse(true);
        translateTransition.setCycleCount(2);
        translateTransition.play();
    }

    private void openImageView(int index) {
        currentIndex = index;

        Stage imageStage = new Stage();
        imageStage.setTitle("View Full Image");

        BorderPane layout = new BorderPane();

        // Use the same image for background on the full image view
        layout.setStyle("-fx-background-image: url('https://www.w3schools.com/w3images/forestbridge.jpg'); -fx-background-size: cover; " +
                "-fx-opacity: 0.9");

        ImageView largeImageView = new ImageView();
        loadImage(largeImageView, currentIndex);

        Button exitButton = createNavigationButton("Exit Full Size");
        Button prevButton = createNavigationButton("Previous");
        Button nextButton = createNavigationButton("Next");

        updateButtonState(exitButton);
        updateButtonState(prevButton);
        updateButtonState(nextButton);

        exitButton.setOnAction(event -> imageStage.close());
        prevButton.setOnAction(event -> {
            if (currentIndex > 0) {
                currentIndex--;
                loadImage(largeImageView, currentIndex);
                updateButtonState(prevButton);
                updateButtonState(nextButton);
            }
        });

        nextButton.setOnAction(event -> {
            if (currentIndex < images.length - 1) {
                currentIndex++;
                loadImage(largeImageView, currentIndex);
                updateButtonState(prevButton);
                updateButtonState(nextButton);
            }
        });

        HBox navigationButtons = new HBox(15, prevButton, nextButton, exitButton);
        navigationButtons.setAlignment(Pos.CENTER);
        navigationButtons.setPadding(new Insets(10));

        layout.setCenter(largeImageView);
        layout.setBottom(navigationButtons);

        Scene imageScene = new Scene(layout, 950, 700);
        imageStage.setScene(imageScene);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), layout);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        imageStage.show();
    }

    private Button createNavigationButton(String label) {
        Button button = new Button(label);
        button.setFont(Font.font("Arial", 18));
        button.setStyle("-fx-background-color: #315063; -fx-text-fill: white; -fx-padding: 8px;");
        return button;
    }

    private void updateButtonState(Button button) {
        if ((button.getText().equals("Previous") && currentIndex == 0) || (button.getText().equals("Next") && currentIndex == images.length - 1)) {
            button.setDisable(true);
        } else {
            button.setDisable(false);
        }
    }

    private void loadImage(ImageView view, int index) {
        try {
            String path = images[index];
            Image fullImage = new Image(path);
            view.setImage(fullImage);
            view.setFitWidth(800);
            view.setFitHeight(650);
            view.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Error loading image at index " + index);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
