/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;


import ds.assignment.DatabaseConnection;
import ds.assignment.Login;
import ds.assignment.Parents;
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
 * FXML Controller class
 *
 * @author enjye
 */
public class FriendProfileEducatorController implements Initializable {

    @FXML
    private Button ExitFriendProfilePage;
    @FXML
    private Label LocationLabel, UsernameLabel, EmailLabel;
    @FXML
    private Text UsernameProfilePage,totalQuizCreated, totalEventCreated;;

    DatabaseConnection dbConnect = new DatabaseConnection();
    UserRepository userRepository = new UserRepository(dbConnect);
    Login login = new Login();  // Create a single instance of Login
    SessionManager sessionManager = new SessionManager(userRepository, login);

    private Stack<String> navigate = new Stack<>();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadStack();
        ExitFriendProfilePage.setOnAction(event -> {
            navigate.pop();
            resetStack();
            // Get the stage (window) associated with the button
            Stage stage = (Stage) ExitFriendProfilePage.getScene().getWindow();

            // Close the stage
            stage.close();
        });
        setUpProfilePage(navigate.peek());
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

    private void setUpProfilePage(String username) {
        String email = userRepository.getEmailByUsername(username);
        System.out.println(email);
        String location = userRepository.getLocation(username);
        System.out.println(location);
        int quizCreated = userRepository.getNumQuizCreated(username);
        int eventCreated = userRepository.getNumEventCreated(username);

        UsernameProfilePage.setText(username);
        UsernameLabel.setText(username);
        EmailLabel.setText(email);
        LocationLabel.setText(location);

        totalQuizCreated.setText(String.valueOf(quizCreated)); //setTotalQuiz
        totalEventCreated.setText(String.valueOf(eventCreated)); //setTotalEvent

    }
    
    public void loadStack(){
        String role = userRepository.getRole(sessionManager.getCurrentUser().getUsername()).toUpperCase();
        if (role.equals("STUDENT")) {
            navigate = StudentController.friendNameNavigate;
        } else if (role.equals("EDUCATOR")) {
            navigate = EducatorController.friendNameNavigateEducator;
        } else if (role.equals("PARENT")) {
            navigate = ParentController.friendNameNavigateParent;
        } 
    }
    
    public void resetStack(){
        String role = userRepository.getRole(sessionManager.getCurrentUser().getUsername()).toUpperCase();
        if (role.equals("STUDENT")) {
            StudentController.friendNameNavigate = navigate;
        } else if (role.equals("EDUCATOR")) {
            EducatorController.friendNameNavigateEducator = navigate;
        } else if (role.equals("PARENT")) {
            ParentController.friendNameNavigateParent= navigate;
        } 
    }

}


