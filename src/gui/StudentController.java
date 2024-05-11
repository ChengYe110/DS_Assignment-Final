/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    private StackPane stackPane;
    @FXML
    private Button DiscussionPage, EventPage, HomePage, LeaderboardPage, MenuButton, ProfilePage, QuizPage, 
            CreateQuizPage, DoneCreateQuiz, DoneCreateEvent, CreateEventPage, FriendListPage, ExitFriendListPage,
            FriendRequestPage,ExitFriendRequestPage;
    @FXML
    private VBox DrawerPane,FriendListVBox,FriendRequestVBox;
    @FXML
    private AnchorPane MenuPane, TopPane, HomePagePane, LeaderboardPane, QuizPane, CreateQuizPane, EventPane, 
            CreateEventPane, BookingPane, StudentProfilePane,FriendListPane,FriendRequestPane;
    @FXML
    private Text UsernameText, Suggested1, Suggested2, Suggested3, Suggested4, Suggested5, Distance1, Distance2, Distance3, Distance4, Distance5;
    @FXML
    private TextArea QuizTitleField, QuizDescriptionField, QuizContentField, EventTitleField, EventDescriptionField, EventVenueField, EventTimeField, DestinationIDField;
    @FXML
    private ChoiceBox<String> FilterChoiceBox, QuizThemeChoiceBox, AvailableTimeSlotChoiceBox;
    @FXML
    private DatePicker EventDatePicker;
    @FXML
    private HBox MENU;
    private Button selectedButton = null;
    private ObservableList<String> theme = FXCollections.observableArrayList("SCIENCE", "TECHNOLOGY", "ENGINEERING", "MATHEMATIC");
    @FXML
    private TableView<?> QuizTable;
    @FXML
    private ScrollPane FriendListScrollPane,FriendRequestScrollPane;

    private TranslateTransition slideOutTransition, slideInTransition;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Execute after the layout pass is complete
        Platform.runLater(() -> {
            // Initially hide the MenuPane off the screen
            MenuPane.setTranslateX(-MenuPane.getWidth());
            switchHomePage();

            // Create TranslateTransitions for sliding in and out
            slideOutTransition = new TranslateTransition(Duration.seconds(0.5), MenuPane);
            slideOutTransition.setToX(0); // Slide to the final position

            slideInTransition = new TranslateTransition(Duration.seconds(0.5), MenuPane);
            slideInTransition.setToX(-MenuPane.getWidth()); // Slide back to the initial position

            // Set up the action for the MenuButton
            // Add button effect
            ButtonEffect(MenuButton);
            MenuButton.setOnAction(event -> {
                if (MenuPane.getTranslateX() != 0) {
                    slideOutTransition.play(); // Slide out the menu
                } else {
                    slideInTransition.play(); // Slide in the menu
                }
            });

            FilterChoiceBox.setItems(theme);
            FilterChoiceBox.setValue("FILTER");
            QuizThemeChoiceBox.setItems(theme);

            ButtonEffect(FriendListPage);
            ProfilePage.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                stackPane.getChildren().clear();
                stackPane.getChildren().add(StudentProfilePane);
                FriendListPane.setVisible(false);
                FriendRequestPane.setVisible(false);
            });
            
            FriendListScrollPane.setContent(FriendListVBox);
            FriendListPage.setOnAction(event -> {
                FriendListPane.setVisible(true);
            });
            
            ButtonEffect(ExitFriendListPage);
            ExitFriendListPage.setOnAction(event -> {
                FriendListPane.setVisible(false);
            });
            
            FriendRequestScrollPane.setContent(FriendRequestVBox);
            FriendRequestPage.setOnAction(event -> {
                FriendRequestPane.setVisible(true);
            });
            
            ButtonEffect(ExitFriendRequestPage);
            ExitFriendRequestPage.setOnAction(event -> {
                FriendRequestPane.setVisible(false);
            });

            DiscussionPage.setOnAction(event -> {
                // Handle Discussion button click
            });

            QuizPage.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                stackPane.getChildren().clear();
                stackPane.getChildren().add(QuizPane);
            });

            CreateQuizPage.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                stackPane.getChildren().clear();
                stackPane.getChildren().add(CreateQuizPane);
            });

            DoneCreateQuiz.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                stackPane.getChildren().clear();
                stackPane.getChildren().add(QuizPane);
            });

            EventPage.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                stackPane.getChildren().clear();
                stackPane.getChildren().add(EventPane);
            });

            CreateEventPage.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                stackPane.getChildren().clear();
                stackPane.getChildren().add(CreateEventPane);
            });

            DoneCreateEvent.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                stackPane.getChildren().clear();
                stackPane.getChildren().add(EventPane);
            });

            for (Node node : MENU.getChildren()) {
                ((Button) node).setOnAction(event -> {
                    Button btn = (Button) event.getSource();
                    if (!btn.equals(selectedButton)) {
                        if (selectedButton != null) {
                            selectedButton.setId("");
                        }
                        selectedButton = btn;
                        selectedButton.setId("selected");
                    }
                    if (btn.equals(HomePage)) {
                        switchHomePage();
                    } else if (btn.equals(LeaderboardPage)) {
                        switchLeaderboardPage();
                    }
                });
            }
            selectedButton = (Button) MENU.getChildren().get(0);
            selectedButton.setId("selected");
        });
        addFriendButton("Cindy");
        addFriendButton("Cindy");
        addFriendButton("Cindy");
        addFriendButton("Cindy");
        addFriendButton("Cindy");
        addFriendButton("Cindy");
        addFriendButton("Cindy");
        addFriendButton("Jack");
        addFriendButton("Leo");
        addFriendRequest("Jane");
        addFriendRequest("Jane");
        addFriendRequest("Jane");
        addFriendRequest("Jane");
        addFriendRequest("Jane");
        addFriendRequest("Johnny Dep");
        addFriendRequest("Johnny Dep");
        addFriendRequest("Johnny Dep");
        addFriendRequest("Johnny Dep");
        addFriendRequest("Johnny Dep");
    }

    public void switchHomePage() {
        if (MenuPane.getTranslateX() == 0) {
            slideInTransition.play();
        }
        stackPane.getChildren().clear();
        stackPane.getChildren().add(HomePagePane);
    }

    public void switchLeaderboardPage() {
        if (MenuPane.getTranslateX() == 0) {
            slideInTransition.play();
        }
        stackPane.getChildren().clear();
        stackPane.getChildren().add(LeaderboardPane);
    }

//    public void switchEventPage() {
//        if (MenuPane.getTranslateX() == 0) {
//            slideInTransition.play();
//        }
//        stackPane.getChildren().clear();
//        stackPane.getChildren().add(EventPane);
//    }
//
//    public void switchCreateEventPage() {
//        if (MenuPane.getTranslateX() == 0) {
//            slideInTransition.play();
//        }
//        stackPane.getChildren().clear();
//        stackPane.getChildren().add(CreateEventPane);
//    }
//
//    public void switchQuizPage() {
//        if (MenuPane.getTranslateX() == 0) {
//            slideInTransition.play();
//        }
//        stackPane.getChildren().clear();
//        stackPane.getChildren().add(QuizPane);
//    }
//
//    public void switchCreateQuizPage() {
//        if (MenuPane.getTranslateX() == 0) {
//            slideInTransition.play();
//        }
//        stackPane.getChildren().clear();
//        stackPane.getChildren().add(CreateQuizPane);
//    }
//    
//    public void switchStudentProfilePage() {
//        if (MenuPane.getTranslateX() == 0) {
//            slideInTransition.play();
//        }
//        stackPane.getChildren().clear();
//        stackPane.getChildren().add(StudentProfilePane);
//    }
    
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

    public void getFilterChoice(ActionEvent event) {
        String theme = FilterChoiceBox.getValue();
    }

    public void setUpQuizTable() {
        TableColumn title = new TableColumn("TITLE");
        TableColumn description = new TableColumn("DESCRIPTION");
        TableColumn theme = new TableColumn("THEME");
        TableColumn content = new TableColumn("CONTENT");

        QuizTable.getColumns().addAll(title, description, theme, content);

//        
//        //Step : 1# Create a person class that will represtent data
//        
//        //Step : 2# Define data in an Observable list and add data as you want to show inside table    
//         final ObservableList<Person> data = FXCollections.observableArrayList(
//                new Person("1", "Jacob", "24", "", "jacob.smith@example.com", "jacob.smith@example.com"),
//                new Person("2","Isabella", "25", "","isabella.johnson@example.com", "jacob.smith@example.com"),
//                new Person("3","Ethan", "27","" ,"ethan.williams@example.com", "jacob.smith@example.com"),
//                new Person("4","Emma", "28","" ,"emma.jones@example.com", "jacob.smith@example.com"),                        
//                new Person("5","Michael", "29", "" ,"michael.brown@example.com", "jacob.smith@example.com"),
//                new Person("5","Michael", "29", "","michael.brown@example.com", "jacob.smith@example.com")   );
//
//        
//        //Step : 3#  Associate data with columns  
//            id.setCellValueFactory(new PropertyValueFactory<Person,String>("id"));
//        
//            name.setCellValueFactory(new PropertyValueFactory<Person,String>("name"));
//
//            age.setCellValueFactory(new PropertyValueFactory<Person,String>("age"));
//
//            primary.setCellValueFactory(new PropertyValueFactory<Person,String>("primary"));
//            
//            secondry.setCellValueFactory(new PropertyValueFactory<Person,String>("secondry"));
//            
//               
//                        
//        //Step 4: add data inside table
//           myTable.setItems(data);
    }
    
    // Method to add a friend button to the VBox
    private void addFriendButton(String friendName) {
        Button friendButton = new Button(friendName);
        friendButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 20px; ");
        ButtonEffect(friendButton);
        friendButton.setOnAction(event -> openProfilePage(friendName));
        FriendListVBox.getChildren().add(friendButton);
    }

    // Method to open the friend's profile page
    private void openProfilePage(String friendName) {
        // open the profile page using the provided name
        //need to add "add friend" button
    } 
    
    // Method to add an HBox with three buttons to the VBox
    private void addFriendRequest(String friendName) {
        // Create an HBox
        HBox FriendRequestHBox = new HBox();
        FriendRequestHBox.setSpacing(10); // Spacing between buttons

        // Create three buttons and add them to the HBox
        Button name = new Button(friendName);
        Button confirm = new Button("CONFIRM");
        Button delete = new Button("DELETE");

        name.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 20px; ");
        name.setPrefWidth(170);
        
        // Add custom styles to button2 and button3
        confirm.getStyleClass().add("confirm");
        delete.getStyleClass().add("delete");
        
        ButtonEffect(name);
        name.setOnAction(event -> openProfilePage(friendName));

        FriendRequestHBox.getChildren().addAll(name, confirm, delete);

        // Add the HBox to the VBox
        FriendRequestVBox.getChildren().add(FriendRequestHBox);
    }
}
