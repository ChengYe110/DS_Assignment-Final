/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author enjye
 */
public class FriendProfileController implements Initializable {

    @FXML
    private Button ExitFriendProfilePage, AddFriendButton, FriendListPage, PointDisplay, ExitFriendListPage;
    @FXML
    private AnchorPane FriendListPane;
    @FXML
    private VBox FriendListVBox;
    @FXML
    private Label LocationLabel, UsernameLabel, EmailLabel;
    @FXML
    private Text NumOfFriend, UsernameProfilePage;
    @FXML
    private TableView<ParentColumn> ParentTable;
    @FXML
    private TableColumn<ParentColumn, Integer> NoColumn;
    @FXML
    private TableColumn<ParentColumn, String> ParentColumn;
    @FXML
    private TableView<EventColumn> EventTable;
    @FXML
    private TableColumn<EventColumn, String> DateColumn, TitleColumn, VenueColumn, TimeColumn;
    @FXML
    private TableView<BookedStudyTourColumn> BookedStudyTourTable;
    @FXML
    private TableColumn<BookedStudyTourColumn, String> BookedDateColumn, BookedVenueColumn;
    @FXML
    private ScrollPane FriendListScrollPane;

    private StudentController studentController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        FriendListPane.setVisible(false);
        ExitFriendProfilePage.setOnAction(event -> {
            // Get the stage (window) associated with the button
            Stage stage = (Stage) ExitFriendProfilePage.getScene().getWindow();

            // Close the stage
            stage.close();
        });
        FriendListScrollPane.setContent(FriendListVBox);
            FriendListPage.setOnAction(event -> {
                FriendListPane.setVisible(true);
                FriendListPane.toFront();
            });
        ExitFriendListPage.setOnAction(event -> {
            FriendListPane.setVisible(false);
        });
        addFriendList("Cindy");
        addFriendList("Cindy");
        addFriendList("Cindy");
        addFriendList("Cindy");
        addFriendList("Cindy");
        addFriendList("Cindy");
        addFriendList("Cindy");
        addFriendList("Jack");
        addFriendList("Leo");
    }

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

    // Method to add a friend to the friendlist
    private void addFriendList(String friendName) {
        Button friendButton = new Button(friendName);
        friendButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 20px; ");
        ButtonEffect(friendButton);
        friendButton.setOnAction(event -> openProfilePage(friendName));
        FriendListVBox.getChildren().add(friendButton);
    }

    private void openProfilePage(String friendName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FriendProfile.fxml"));
            Parent root = loader.load();

            FriendProfileController controller = loader.getController();

            // Create a new stage for the second view
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
