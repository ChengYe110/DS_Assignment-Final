/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author enjye
 */
public class StudentController implements Initializable {

    @FXML
    private Button DiscussionPage, EventPage, HomePage, LeaderboardPage, MenuButton, ProfilePage, QuizPage;
    @FXML
    private VBox DrawerPane;
    @FXML
    private AnchorPane MenuPane, TopPane, HomePagePane,LeaderboardPane,QuizPane;
    @FXML
    private Text UsernameText;
    @FXML
    private Line LineHomePage, LineLeaderboardPage;

    private TranslateTransition slideOutTransition, slideInTransition;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public Line getLineHomePage() {
        return this.LineHomePage;
    }

    public Line getLineLeaderboardPage() {
        return this.LineLeaderboardPage;
    }

    public Button getHomePage() {
        return this.HomePage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Execute after the layout pass is complete
        Platform.runLater(() -> {
            // Initially hide the MenuPane off the screen
            MenuPane.setTranslateX(-MenuPane.getWidth());
            LeaderboardPane.setVisible(false);
            QuizPane.setVisible(false);

            // Create TranslateTransitions for sliding in and out
            slideOutTransition = new TranslateTransition(Duration.seconds(0.5), MenuPane);
            slideOutTransition.setToX(0); // Slide to the final position

            slideInTransition = new TranslateTransition(Duration.seconds(0.5), MenuPane);
            slideInTransition.setToX(-MenuPane.getWidth()); // Slide back to the initial position

            // Set up the action for the MenuButton
            MenuButton.setOnAction(event -> {
                // Add button effect
                ButtonEffect(MenuButton);
                if (MenuPane.getTranslateX() != 0) {
                    slideOutTransition.play(); // Slide out the menu
                    MenuPane.setMouseTransparent(true); // Make MenuPane non-interactive when hidden
                } else {
                    slideInTransition.play(); // Slide in the menu
                    MenuPane.setMouseTransparent(false); // Make MenuPane interactive when shown
                }
            });

            // Set initial opacity of the line to 0
            LineLeaderboardPage.setOpacity(0);
            // Set up the action for each button
            LeaderboardPage.setOnAction(event -> {
                RemoveLineEffect(LineHomePage);
                AddLineEffect(LineLeaderboardPage);
                HomePagePane.setVisible(false);
                LeaderboardPane.setVisible(true);
                QuizPane.setVisible(false);
            });

            // Add event handlers for other buttons
            HomePage.setOnAction(event -> {
                RemoveLineEffect(LineLeaderboardPage);
                AddLineEffect(LineHomePage);
                LeaderboardPane.setVisible(false);
                HomePagePane.setVisible(true);
                QuizPane.setVisible(false);
            });

            ProfilePage.setOnAction(event -> {
                // Handle Profile button click
            });

            DiscussionPage.setOnAction(event -> {
                // Handle Discussion button click
            });

            EventPage.setOnAction(event -> {
                // Handle Event button click
            });

            QuizPage.setOnAction(event -> {
                // Handle Quiz button click
            });
        });
    }

    // Button effect method
    public void ButtonEffect(Button button) {
        // Create a scale transition
        button.setScaleX(1);
        button.setScaleY(1);
        button.setOpacity(1);
        button.setTranslateZ(0);

        button.setOnMousePressed(event -> {
            button.setScaleX(0.95);
            button.setScaleY(0.95);
            button.setOpacity(0.5);
            button.setTranslateZ(-1);
        });

        button.setOnMouseReleased(event -> {
            button.setScaleX(1);
            button.setScaleY(1);
            button.setOpacity(1);
            button.setTranslateZ(0);
        });
    }

    // Method to add line effect under a button
    public void AddLineEffect(Line line) {
        // Create a fade-in animation for the line
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), line);
        fadeIn.setToValue(1); // Set final opacity to fully visible

        // Play the fade-in animation
        fadeIn.play();
    }

    // Method to remove line effect under a button
    public void RemoveLineEffect(Line line) {

        // Create a fade-in animation for the line
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.3), line);
        fadeOut.setToValue(0); // Set final opacity to fully visible

        // Play the fade-in animation
        fadeOut.play();
    }
}
