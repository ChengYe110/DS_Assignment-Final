/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import ds.assignment.DatabaseConnection;
import ds.assignment.Login;
import ds.assignment.SessionManager;
import ds.assignment.Students;
import static ds.assignment.Students.isDuplicateFriend;
import ds.assignment.UserRepository;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

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

    DatabaseConnection dbConnect = new DatabaseConnection();
    UserRepository userRepository = new UserRepository(dbConnect);
    Login login = new Login();  // Create a single instance of Login
    SessionManager sessionManager = new SessionManager(userRepository, login);

    private Stack<String> navigate = new Stack<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadStack();
        FriendListPane.setVisible(false);
        ExitFriendProfilePage.setOnAction(event -> {
            navigate.pop();
            resetStack();
            // Get the stage (window) associated with the button
            Stage stage = (Stage) ExitFriendProfilePage.getScene().getWindow();

            // Close the stage
            stage.close();
        });
        FriendListScrollPane.setContent(FriendListVBox);
        FriendListPage.setOnAction(event -> {
            FriendListPane.setVisible(true);
            FriendListPane.toFront();
            showFriendList(navigate.peek());
        });
        ExitFriendListPage.setOnAction(event -> {
            FriendListPane.setVisible(false);
        });
        setUpProfilePage(navigate.peek());
        refreshAddFriend(navigate.peek());
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
        friendButton.getStyleClass().add("friend-button");
        ButtonEffect(friendButton);
        friendButton.setOnAction(event -> {
            if (navigate.contains(friendName) || friendName.equals(sessionManager.getCurrentUser().getUsername())) {
                friendButton.setDisable(true);
            } else {
                navigate.push(friendName);
                openProfilePage(navigate.peek());
            }
        });
        FriendListVBox.getChildren().add(friendButton);
    }

    public String profilePageRole(String friendName) {
        String role = userRepository.getRole(friendName).toUpperCase();
        if (role.equals("STUDENT")) {
            return "FriendProfile.fxml";
        } else if (role.equals("EDUCATOR")) {
            return "FriendProfileEducator.fxml";
        } else if (role.equals("PARENT")) {
            return "FriendProfileParent.fxml";
        } else {
            return "";
        }
    }

    private void openProfilePage(String friendName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(profilePageRole(friendName)));
            Parent root = loader.load();

            String role = userRepository.getRole(friendName).toUpperCase();
            if (role.equals("STUDENT")) {
                FriendProfileController controller = loader.getController();
            } else if (role.equals("PARENT")) {
                FriendProfileParentController controller = loader.getController();
            } else if (role.equals("EDUCATOR")) {
                FriendProfileEducatorController controller = loader.getController();
            }

            // Create a new stage for the second view
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);

            // Ensure the scene is also transparent
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Stage) FriendListPane.getScene().getWindow()));

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showFriendList(String username) {
        FriendListVBox.getChildren().clear();
        ArrayList<String> friendList = Students.getFriendList(username);
        for (String friend : friendList) {
            addFriendList(friend);
        }
    }

    private void setUpParentTable(String username) {
        //associate data with column
        NoColumn.setCellValueFactory(new PropertyValueFactory<ParentColumn, Integer>("no"));
        ParentColumn.setCellValueFactory(new PropertyValueFactory<ParentColumn, String>("username"));

        //get chilren arraylist from parent
        ArrayList<ParentColumn> arrayList = new ArrayList<>(Students.getParent(username));
        ObservableList<ParentColumn> parentList = FXCollections.observableArrayList(arrayList);

        ParentTable.setItems(parentList);
    }

    private void setUpEventTable(String username) {
        ArrayList<EventColumn> temp = Students.getStudentRegisteredEvents(username);
        ObservableList<EventColumn> eventList = FXCollections.observableArrayList(temp);

        //associate data with column
        DateColumn.setCellValueFactory(new PropertyValueFactory<EventColumn, String>("date"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<EventColumn, String>("title"));
        VenueColumn.setCellValueFactory(new PropertyValueFactory<EventColumn, String>("venue"));
        TimeColumn.setCellValueFactory(new PropertyValueFactory<EventColumn, String>("time"));

        EventTable.setItems(eventList);
    }

    private void setUpBookedStudyTourTable(String username) {
        ArrayList<BookedStudyTourColumn> temp = Students.getStudentBookedStudyTour(username);
        ObservableList<BookedStudyTourColumn> bookedStudyTourList = FXCollections.observableArrayList(temp);

        //associate data with column
        BookedDateColumn.setCellValueFactory(new PropertyValueFactory<BookedStudyTourColumn, String>("date"));
        BookedVenueColumn.setCellValueFactory(new PropertyValueFactory<BookedStudyTourColumn, String>("venue"));

        BookedStudyTourTable.setItems(bookedStudyTourList);
    }

    private void setUpProfilePage(String username) {
        String email = userRepository.getEmailByUsername(username);
        System.out.println(email);
        String location = userRepository.getLocation(username);
        System.out.println(location);
        String totalNumOfFriend = String.valueOf(Students.getTotalFriend(username));
        System.out.println(totalNumOfFriend);

        UsernameProfilePage.setText(username);
        UsernameLabel.setText(username);
        EmailLabel.setText(email);
        LocationLabel.setText(location);
        NumOfFriend.setText(totalNumOfFriend);

        setUpParentTable(username);
        setUpEventTable(username);
        setUpBookedStudyTourTable(username);

        int point = userRepository.getPoints(username);
        PointDisplay.setText(String.valueOf(point) + " POINTS");

        showFriendList(username);
    }

    private void refreshAddFriend(String friendName) {
        if (sessionManager.getCurrentUser().getRole().equalsIgnoreCase("student")) {
            if (isDuplicateFriend(sessionManager.getCurrentUser().getUsername(), friendName) || friendName.equals(sessionManager.getCurrentUser().getUsername())) {
                AddFriendButton.setVisible(false);
            } else if (Students.checkFriendRequestPending(sessionManager.getCurrentUser().getUsername(), friendName)) {
                AddFriendButton.setText("PENDING");
                AddFriendButton.setDisable(true);
            } else {
                AddFriendButton.setOnAction(event -> {
                    Students.sendFriendRequest(sessionManager.getCurrentUser().getUsername(), friendName);
                    refreshAddFriend(friendName);
                });
            }
        } else {
            AddFriendButton.setVisible(false);
        }
    }

    public void loadStack() {
        String role = userRepository.getRole(sessionManager.getCurrentUser().getUsername()).toUpperCase();
        if (role.equals("STUDENT")) {
            navigate = StudentController.friendNameNavigate;
        } else if (role.equals("EDUCATOR")) {
            navigate = EducatorController.friendNameNavigateEducator;
        } else if (role.equals("PARENT")) {
            navigate = ParentController.friendNameNavigateParent;
        }
    }

    public void resetStack() {
        String role = userRepository.getRole(sessionManager.getCurrentUser().getUsername()).toUpperCase();
        if (role.equals("STUDENT")) {
            StudentController.friendNameNavigate = navigate;
        } else if (role.equals("EDUCATOR")) {
            EducatorController.friendNameNavigateEducator = navigate;
        } else if (role.equals("PARENT")) {
            ParentController.friendNameNavigateParent = navigate;
        }
    }

}
