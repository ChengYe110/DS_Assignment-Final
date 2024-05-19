/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import ds.assignment.DatabaseConnection;
import ds.assignment.Login;
import ds.assignment.Points;
import ds.assignment.SessionManager;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import ds.assignment.Students;
import ds.assignment.UserRepository;

/**
 * FXML Controller class
 *
 * @author enjye
 */
public class StudentController implements Initializable {

    @FXML
    private StackPane stackPane, ExtraStackPane;
    @FXML
    private Button DiscussionPage, EventPage, HomePage, LeaderboardPage, MenuButton, ProfilePage, QuizPage,
            CreateQuizPage, DoneCreateQuiz, DoneCreateEvent, CreateEventPage, FriendListPage, ExitFriendListPage,
            FriendRequestPage, ExitFriendRequestPage, ExitViewFriendProfilePage, CreateDiscussionPage, DoneCreateDiscussion,
            AddParentButton, AddParentPage, ExitAddParentPane, ChangeUsernameAndEmailButton, ChangePasswordButton,
            SaveChangeUsernameAndEmailButton, SaveChangePasswordButton, EditProfilePage, ExitEditProfilePage,
            PointDisplay, JoinEvent1, JoinEvent2, JoinEvent3, JoinEvent4, FilterButton;
    @FXML
    private VBox DrawerPane, FriendListVBox, FriendRequestVBox, QuizVBox, DiscussionVBox, FilterVBox;
    @FXML
    private AnchorPane MenuPane, TopPane, HomePagePane, LeaderboardPane, QuizPane, CreateQuizPane, EventPane,
            CreateEventPane, BookingPane, StudentProfilePane, FriendListPane, FriendRequestPane, ViewFriendProfilePage,
            DiscussionPane, CreateDiscussionPane, AddParentPane, ChangeUsernameAndEmailPane, ChangePasswordPane, EditProfilePane;
    @FXML
    private Text UsernameMenuPane, UsernameProfilePage, NumOfFriend, Suggested1, Suggested2, Suggested3, Suggested4, Suggested5,
            Distance1, Distance2, Distance3, Distance4, Distance5, Event1Description, Event2Description, Event3Description,
            Event4Description, Event1Venue, Event2Venue, Event3Venue, Event4Venue, Event1Date, Event2Date, Event3Date, Event4Date,
            Event1Time, Event2Time, Event3Time, Event4Time;
    @FXML
    private TextArea QuizDescriptionField, EventDescriptionField, DestinationIDField, DiscussionContentField;
    @FXML
    private Label UsernameLabel, EmailLabel, LocationLabel, Event1Title, Event2Title, Event3Title, Event4Title;
    @FXML
    private TextField NewUsername, NewEmail, ParentUsernameField, EventTitleField, EventVenueField, QuizTitleField, QuizContentField,
            DiscussionTitleField;
    @FXML
    private PasswordField OldPassword1, OldPassword2, NewPassword, ConfirmPassword;
    @FXML
    private ChoiceBox<String> QuizThemeChoiceBox, AvailableTimeSlotChoiceBox, EventTimeChoiceBox;
    @FXML
    private DatePicker EventDatePicker;
    @FXML
    private HBox MENU;
    private Button selectedButton = null;
    private ObservableList<String> theme = FXCollections.observableArrayList("SCIENCE", "TECHNOLOGY", "ENGINEERING", "MATHEMATIC");
    private ObservableList<String> time = FXCollections.observableArrayList("8 am - 10 am", "10 am - 12 pm", "12 pm - 2 pm", "2 pm - 4 pm", "4 pm - 6 pm", "6 pm - 8 pm");
    @FXML
    private TableView<Parent> ParentTable;
    @FXML
    private TableColumn<Parent, Integer> NoColumn;
    @FXML
    private TableColumn<Parent, String> ParentColumn;
    @FXML
    private TableView<Event> EventTable;
    @FXML
    private TableColumn<Event, String> DateColumn, TitleColumn, VenueColumn, TimeColumn;
    @FXML
    private TableView<BookedStudyTour> BookedStudyTourTable;
    @FXML
    private TableColumn<BookedStudyTour, String> BookedDateColumn, BookedVenueColumn;
    @FXML
    private ScrollPane FriendListScrollPane, FriendRequestScrollPane, QuizScrollPane, DiscussionScrollPane;

    private TranslateTransition slideOutTransition, slideInTransition;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Students student;
    DatabaseConnection dbConnect = new DatabaseConnection();
    UserRepository userRepository = new UserRepository(dbConnect);
    Login login = new Login();  // Create a single instance of Login
    SessionManager sessionManager = new SessionManager(userRepository, login);  // Pass the Login instance to SessionManage
    Points pointsFromDatabase = new Points();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        student=new Students("333@gmail.com","zw","333","student");
//        student.sendFriendRequest("tjq", "cy");
//        student.sendFriendRequest("hzy", "cy");
//        student.sendFriendRequest("lydia", "zw");
        // Execute after the layout pass is complete
        slideInTransition = new TranslateTransition(Duration.seconds(0.5), MenuPane);
        slideInTransition.setToX(-MenuPane.getWidth()); // Slide back to the initial position
        Platform.runLater(() -> {
            // Initially hide the MenuPane off the screen
            MenuPane.setTranslateX(-MenuPane.getWidth());
            switchHomePage();

            // Create TranslateTransitions for sliding in and out
            slideOutTransition = new TranslateTransition(Duration.seconds(0.5), MenuPane);
            slideOutTransition.setToX(0); // Slide to the final position

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

            EventTimeChoiceBox.setItems(time);

            ButtonEffect(FriendListPage);
            ProfilePage.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                stackPane.getChildren().clear();
                stackPane.getChildren().add(StudentProfilePane);
                FriendListPane.setVisible(false);
                FriendRequestPane.setVisible(false);
                AddParentPane.setVisible(false);
                //ViewFriendProfilePage.setVisible(false);
                EditProfilePane.setVisible(false);
            });
            EditProfilePage.setOnAction(event -> {
                EditProfilePane.setVisible(true);
                EditProfilePane.toFront();
                ExtraStackPane.getChildren().clear();
                ExtraStackPane.getChildren().add(ChangeUsernameAndEmailPane);
            });
            ButtonEffect(ChangeUsernameAndEmailButton);
            ChangeUsernameAndEmailButton.setOnAction(event -> {
                ExtraStackPane.getChildren().clear();
                ExtraStackPane.getChildren().add(ChangeUsernameAndEmailPane);
                ChangeUsernameAndEmailButton.setStyle("-fx-background-color: white;");
                ChangePasswordButton.setStyle("-fx-background-color: transparent;");
            });
            ButtonEffect(ChangePasswordButton);
            ChangePasswordButton.setOnAction(event -> {
                ExtraStackPane.getChildren().clear();
                ExtraStackPane.getChildren().add(ChangePasswordPane);
                ChangeUsernameAndEmailButton.setStyle("-fx-background-color: transparent;");
                ChangePasswordButton.setStyle("-fx-background-color: white;");
            });
            ExitEditProfilePage.setOnAction(event -> {
                EditProfilePane.setVisible(false);
            });
            SaveChangeUsernameAndEmailButton.setOnAction(event -> {
                //editProfile_Username_Email(student.getUsername());
                editProfile_Username_Email(sessionManager.getCurrentUser().getUsername());
                //setUpProfilePage(student.getUsername());
            });
            SaveChangePasswordButton.setOnAction(event -> {
                //editProfile_Password(student.getUsername());
            });
            AddParentPage.setOnAction(event -> {
                AddParentPane.setVisible(true);
                AddParentPane.toFront();
            });
            AddParentButton.setOnAction(event -> {
                String parentUsername = ParentUsernameField.getText();
                if (parentUsername.isBlank()) {
                    showReminderDialog("Please fill in all information!!!");
                }
                AddParentPane.setVisible(false);
            });
            ExitAddParentPane.setOnAction(event -> {
                AddParentPane.setVisible(false);
            });
            FriendListScrollPane.setContent(FriendListVBox);
            FriendListPage.setOnAction(event -> {
                FriendListPane.setVisible(true);
                FriendListPane.toFront();
            });
            ButtonEffect(ExitFriendListPage);
            ExitFriendListPage.setOnAction(event -> {
                FriendListPane.setVisible(false);
            });
            FriendRequestScrollPane.setContent(FriendRequestVBox);
            FriendRequestPage.setOnAction(event -> {
                FriendRequestPane.setVisible(true);
                FriendRequestPane.toFront();
            });
            ButtonEffect(ExitFriendRequestPage);
            ExitFriendRequestPage.setOnAction(event -> {
                FriendRequestPane.setVisible(false);
            });
//            ButtonEffect(ExitViewFriendProfilePage);
//            ExitViewFriendProfilePage.setOnAction(event -> {
//                ViewFriendProfilePage.setVisible(false);
//            });

            DiscussionScrollPane.setContent(DiscussionVBox);
            DiscussionPage.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                stackPane.getChildren().clear();
                stackPane.getChildren().add(DiscussionPane);
            });
            CreateDiscussionPage.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                stackPane.getChildren().clear();
                stackPane.getChildren().add(CreateDiscussionPane);
            });
            DoneCreateDiscussion.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                String discussionTitle = DiscussionTitleField.getText();
                String discussionContent = DiscussionContentField.getText();
                if (discussionTitle.isBlank() || discussionContent.isBlank()) {
                    showReminderDialog("Please fill in all information!!!");
                } else {
                    stackPane.getChildren().clear();
                    stackPane.getChildren().add(DiscussionPane);
                }
            });

            QuizScrollPane.setContent(QuizVBox);
            createFilterDropdown(theme);
            QuizPage.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                stackPane.getChildren().clear();
                stackPane.getChildren().add(QuizPane);
            });
            QuizThemeChoiceBox.setItems(theme);
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
                String quizTitle = QuizTitleField.getText();
                String quizDescription = QuizDescriptionField.getText();
                String quizTheme = QuizThemeChoiceBox.getValue();
                String quizContent = QuizContentField.getText();
                if (quizTitle.isBlank() || quizDescription.isBlank() || quizTheme.isBlank() || quizContent.isBlank()) {
                    showReminderDialog("Please fill in all information!!!");
                } else {
                    stackPane.getChildren().clear();
                    stackPane.getChildren().add(QuizPane);
                }
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
            JoinEvent1.setOnAction(event -> {
                String title = Event1Title.getText();
                String description = Event1Description.getText();
                String venue = Event1Venue.getText();
                String date = Event1Title.getText();
                String time = Event1Time.getText();
                //check clashing
            });
            JoinEvent2.setOnAction(event -> {
                String title = Event2Title.getText();
                String description = Event2Description.getText();
                String venue = Event2Venue.getText();
                String date = Event2Title.getText();
                String time = Event2Time.getText();
                //check clashing
            });
            JoinEvent3.setOnAction(event -> {
                String title = Event3Title.getText();
                String description = Event3Description.getText();
                String venue = Event3Venue.getText();
                String date = Event3Title.getText();
                String time = Event3Time.getText();
                //check clashing
            });
            JoinEvent4.setOnAction(event -> {
                String title = Event4Title.getText();
                String description = Event4Description.getText();
                String venue = Event4Venue.getText();
                String date = Event4Title.getText();
                String time = Event4Time.getText();
                //check clashing
            });
            DoneCreateEvent.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                String eventTitle = EventTitleField.getText();
                String eventDescription = EventDescriptionField.getText();
                String eventVenue = EventVenueField.getText();
                String eventDate = EventDatePicker.getValue() != null ? EventDatePicker.getValue().toString() : "";
                String eventTime = EventTimeChoiceBox.getValue();
                if (eventTitle.isBlank() || eventDescription.isBlank() || eventVenue.isBlank() || eventDate.isBlank() || eventTime.isBlank()) {
                    showReminderDialog("Please fill in all information!!!");
                } else {
                    stackPane.getChildren().clear();
                    stackPane.getChildren().add(EventPane);
                }
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

        //testing
        showFriendRequests(sessionManager.getCurrentUser().getUsername());
        //showFriendList("zw");

//        addFriendList("Cindy");
//        addFriendList("Cindy");
//        addFriendList("Cindy");
//        addFriendList("Cindy");
//        addFriendList("Cindy");
//        addFriendList("Cindy");
//        addFriendList("Cindy");
//        addFriendList("Jack");
//        addFriendList("Leo");
//        addFriendRequest("Jane");
//        addFriendRequest("Jane");
//        addFriendRequest("Jane");
//        addFriendRequest("Jane");
//        addFriendRequest("Jane");
//        addFriendRequest("Johnny Dep");
//        addFriendRequest("Johnny Dep");
//        addFriendRequest("Johnny Dep");
//        addFriendRequest("Johnny Dep");
//        addFriendRequest("Johnny Dep");
        addNewQuiz("MockTestQuestion", "ENGINEERING", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", "https://en.wikipedia.org/wiki/Cha_Eun-woo");
        addNewQuiz("MockTestQuestion", "MATHEMATIC", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", "https://en.wikipedia.org/wiki/Cha_Eun-woo");
        addNewQuiz("MockTestQuestion", "SCIENCE", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", "https://en.wikipedia.org/wiki/Cha_Eun-woo");
        addNewQuiz("MockTestQuestion", "ENGINEERING", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", "https://en.wikipedia.org/wiki/Cha_Eun-woo");
        addNewQuiz("MockTestQuestion", "SCIENCE", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", "https://en.wikipedia.org/wiki/Cha_Eun-woo");
        addNewQuiz("MockTestQuestion", "TECHNOLOGY", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", "https://en.wikipedia.org/wiki/Cha_Eun-woo");
        addNewDiscussion("HiTesting", "STUDENT", "jack", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        addNewDiscussion("OKOK", "PARENT", "lily", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        addNewDiscussion("BangBangBang", "EDUCATOR", "janice", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        addNewDiscussion("EatShit", "STUDENT", "leo", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        addNewDiscussion("TomyamNice", "PARENT", "david", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        addNewDiscussion("HiTesting", "STUDENT", "jason", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        addNewDiscussion("YeahGoSleep", "EDUCATOR", "anson", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        addNewDiscussion("SOS", "STUDENT", "lydia", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        setUpProfilePage(sessionManager.getCurrentUser().getUsername());
        Node quizNode = QuizVBox.getChildren().get(0); // Get the first quiz HBox
        String themeText = getThemeText(quizNode); // Call the method
        System.out.println("Theme: " + themeText);
    }

    //modified
//    public void setStudent(Students student) {
//        this.student = student;
//    }

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

    // Method to add a friend to the friendlist
    private void addFriendList(String friendName) {
        Button friendButton = new Button(friendName);
        friendButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 20px; ");
        ButtonEffect(friendButton);
        friendButton.setOnAction(event -> openProfilePage(friendName));
        FriendListVBox.getChildren().add(friendButton);
    }

    //modified
    public void showFriendList(String username) {
        //ArrayList<String> friendList = student.getFriendList(username);
        ArrayList<String> friendList = Students.getFriendList(sessionManager.getCurrentUser().getUsername());
        for (String friend : friendList) {
            addFriendList(friend);
        }
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

        confirm.setOnAction(event -> {
            addFriendList(friendName);
            //modified
            Students.acceptFriendRequest(sessionManager.getCurrentUser().getUsername(), friendName);
            // Retrieve the parent HBox of the confirm button
            HBox parentHBox = (HBox) confirm.getParent();
            // Remove the parent HBox from the FriendRequestVBox
            FriendRequestVBox.getChildren().remove(parentHBox);
        });

        delete.setOnAction(event -> {
            Students.rejectFriendRequest(sessionManager.getCurrentUser().getUsername(), friendName);
            // Retrieve the parent HBox of the confirm button
            HBox parentHBox = (HBox) confirm.getParent();
            // Remove the parent HBox from the FriendRequestVBox
            FriendRequestVBox.getChildren().remove(parentHBox);
        });

        FriendRequestHBox.getChildren().addAll(name, confirm, delete);

        // Add the HBox to the VBox
        FriendRequestVBox.getChildren().add(FriendRequestHBox);
    }

    //modified
    // Method to show friend requests
    public void showFriendRequests(String username) {
        ArrayList<String> friendRequestList = Students.getFriendRequestList(username);
        if (friendRequestList != null) {
            for (String friendRequest : friendRequestList) {
                addFriendRequest(friendRequest);
            }
        } else {
            System.out.println("Friend request list is null.");
        }
    }

    private void addNewQuiz(String title, String theme, String description, String content) {
        // Create the HBox
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setSpacing(10);

        // Create the VBox
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setPadding(new Insets(0, 0, 0, 20));

        // Add three text elements to the VBox
        Text titleText = new Text(title);

        HBox temp = new HBox();
        temp.setSpacing(5);
        temp.setAlignment(Pos.TOP_LEFT);
        Text t = new Text("Theme: ");
        t.setStyle("-fx-fill: #737373; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 16px; ");
        Button themeButton = new Button(theme);
        String setColour = "";
        if (theme.equals("SCIENCE")) {
            setColour = "-fx-background-color: #c3dbc2;";
        } else if (theme.equals("TECHNOLOGY")) {
            setColour = "-fx-background-color: #a7d1df;";
        } else if (theme.equals("ENGINEERING")) {
            setColour = "-fx-background-color: #e4cfb4;";
        } else if (theme.equals("MATHEMATIC")) {
            setColour = "-fx-background-color: #f0c9dc;";
        }
        themeButton.setStyle(setColour + "-fx-text-fill: white; -fx-font-family: \"Segoe UI Black\";-fx-font-size: 12px; -fx-background-radius: 20px;");
        temp.getChildren().addAll(t, themeButton);

        Text descriptionText = new Text(description);
        descriptionText.setWrappingWidth(800);
        titleText.setStyle("-fx-text-fill: black; -fx-font-family: \"Segoe UI Black\";-fx-font-size: 30px; ");
        descriptionText.setStyle("-fx-text-fill: black; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 14px; ");
        vBox.getChildren().addAll(titleText, temp, descriptionText);

        // Create a button
        Button button = new Button("ATTEMPT QUIZ");
        button.setPrefWidth(150);
        button.setStyle("-fx-background-color: linear-gradient( to right,#8c52ff, #5ce1e6); -fx-text-fill: white; -fx-font-family: \"Segoe UI Black\";-fx-font-size: 16px; -fx-background-radius: 20px;");
        button.setOnAction(event -> {
            try {
                // Open the link in the default browser
                Desktop.getDesktop().browse(new URI(content));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Add the VBox and the button to the HBox
        hBox.getChildren().addAll(vBox, button);

        // Add the HBox to the main VBox
        QuizVBox.getChildren().add(0, hBox);
    }

    public void createFilterDropdown(ObservableList<String> themes) {
        FilterVBox.setAlignment(Pos.CENTER);

        // Create checkboxes for each theme
        for (String theme : themes) {
            CheckBox checkBox = new CheckBox(theme);
            checkBox.setStyle("-fx-background-color: white;-fx-font-family: \"Arial Black\";-fx-font-size: 14px;-fx-background-color: rgba(255, 255, 255, 0.7);");
            checkBox.setVisible(false); // Initially hide checkboxes
            checkBox.setMinWidth(150);
            FilterVBox.getChildren().add(checkBox);
        }

        // Add click event handler to toggle checkboxes visibility
        FilterButton.setOnMouseClicked(event -> {
            ObservableList<Node> children = FilterVBox.getChildren();
            for (int i = 1; i < children.size(); i++) {
                Node node = children.get(i);
                node.setVisible(!node.isVisible());
            }
        });
        // Add listener to each checkbox to trigger filterTheme method
        for (Node node : FilterVBox.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                checkBox.setOnAction(event -> filterTheme());
            }
        }

    }

    private String getThemeText(Node node) {
        if (node instanceof HBox) {
            HBox hbox = (HBox) node;
            ObservableList<Node> children = hbox.getChildren();
            for (Node child : children) {
                if (child instanceof VBox) {
                    VBox vbox = (VBox) child;
                    ObservableList<Node> vboxChildren = vbox.getChildren();
                    for (Node vboxChild : vboxChildren) {
                        if (vboxChild instanceof HBox) {
                            HBox tempHBox = (HBox) vboxChild;
                            ObservableList<Node> tempHBoxChildren = tempHBox.getChildren();
                            for (Node tempChild : tempHBoxChildren) {
                                if (tempChild instanceof Button) {
                                    return ((Button) tempChild).getText();
                                }
                            }
                        }
                    }
                }
            }
        }
        return "";
    }

    public void filterTheme() {
        // Get the children of the main VBox
        ObservableList<Node> children = QuizVBox.getChildren();

        // Check if any checkbox is selected
        boolean anyCheckboxSelected = false;
        for (Node filterChild : FilterVBox.getChildren()) {
            if (filterChild instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) filterChild;
                if (checkBox.isSelected()) {
                    anyCheckboxSelected = true;
                    break;
                }
            }
        }

        // Iterate over the children
        for (Node child : children) {
            if (child instanceof HBox) {
                HBox hbox = (HBox) child;
                boolean showQuiz = true; // Default to true, display the quiz

                // Get the theme of the current quiz
                String theme = getThemeText(child);

                // If any checkbox is selected, proceed with filtering
                if (anyCheckboxSelected) {
                    // If the theme is not empty
                    if (!theme.isEmpty()) {
                        // Check if any checkbox with the corresponding theme is selected
                        for (Node filterChild : FilterVBox.getChildren()) {
                            if (filterChild instanceof CheckBox) {
                                CheckBox checkBox = (CheckBox) filterChild;
                                if (checkBox.isSelected() && checkBox.getText().equals(theme)) {
                                    showQuiz = true;
                                    break;
                                } else {
                                    showQuiz = false;
                                }
                            }
                        }
                    } else {
                        showQuiz = false; // If the theme is empty, don't show the quiz
                    }
                }

                // Set visibility of the quiz based on showQuiz flag
                hbox.setVisible(showQuiz);
                hbox.setManaged(showQuiz);
            }
        }
    }

    private void addNewDiscussion(String title, String role, String username, String content) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setSpacing(10);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setPadding(new Insets(0, 0, 0, 20));

        Text titleText = new Text(title);

        HBox temp = new HBox();
        temp.setSpacing(5);
        temp.setAlignment(Pos.TOP_LEFT);
        Text t = new Text("Posted By: ");
        t.setStyle("-fx-fill: #737373; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 16px; ");
        Button roleButton = new Button(role);
        String setColour = "";
        if (role.equals("STUDENT")) {
            setColour = "-fx-background-color: #ff5757;";
        } else if (role.equals("EDUCATOR")) {
            setColour = "-fx-background-color: #ffde59;";
        } else if (role.equals("PARENT")) {
            setColour = "-fx-background-color: #4fc8ab;";
        }
        roleButton.setStyle(setColour + "-fx-text-fill: white; -fx-font-family: \"Segoe UI Black\";-fx-font-size: 12px; -fx-background-radius: 20px;");

        Button usernameButton = new Button(username);
        temp.getChildren().addAll(t, roleButton, usernameButton);

        Text contentText = new Text(content);
        contentText.setWrappingWidth(900);
        titleText.setStyle("-fx-text-fill: black; -fx-font-family: \"Segoe UI Black\";-fx-font-size: 30px; ");
        usernameButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #737373; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 16px; -fx-padding: 0; ");
        contentText.setStyle("-fx-text-fill: black; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 14px; ");
        vBox.getChildren().addAll(titleText, temp, contentText);

        // Create a heart shape using SVGPath
        SVGPath heartShape = new SVGPath();
        heartShape.setContent("M 5 15 Q 15 0 25 15 Q 35 0 45 15 Q 47.5 20 25 40 Q 2.5 20 5 15");
        heartShape.setFill(Color.BLACK); // Initial color

        // Create a button with the heart shape
        Button loveButton = new Button();
        loveButton.setStyle("-fx-background-color: transparent;");
        loveButton.setGraphic(heartShape);
        loveButton.setOnAction(event -> {
            // Toggle button color between red and black
            if (heartShape.getFill().equals(Color.BLACK)) {
                heartShape.setFill(Color.RED);
            } else {
                heartShape.setFill(Color.BLACK);
            }
        });

        // Add the VBox and the button to the HBox
        hBox.getChildren().addAll(vBox, loveButton);

        // Add the HBox to the main VBox
        DiscussionVBox.getChildren().add(0, hBox);
    }

    private void setUsername(String username) {
        UsernameMenuPane.setText(username);
    }

    private void setUpParentTable(String username) {
        ObservableList<Parent> parentList = FXCollections.observableArrayList(new Parent(1, "Father"), new Parent(2, "Mother"));

        //associate data with column
        NoColumn.setCellValueFactory(new PropertyValueFactory<Parent, Integer>("no"));
        ParentColumn.setCellValueFactory(new PropertyValueFactory<Parent, String>("username"));

        //modified
        //student.getParentUsernameList();
//        // Replace the connection URL, username, and password with your database credentials
//        String url = "jdbc:mysql://localhost:3306/your_database";
//        String username = "your_username";
//        String password = "your_password";
//
//        try (Connection connection = DriverManager.getConnection(url, username, password)) {
//            // Fetch data from the database
//            String query = "SELECT no, parent FROM your_table";
//            PreparedStatement statement = connection.prepareStatement(query);
//            ResultSet resultSet = statement.executeQuery();
//
//            // Add fetched data to the TableView
//            while (resultSet.next()) {
//                int no = resultSet.getInt("no");
//                String parent = resultSet.getString("parent");
//                parentList.add(new Parent(no, parent));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        ParentTable.setItems(parentList);
    }

    private void setUpEventTable(String username) {
        ArrayList<Event> list = Students.getStudentRegisteredEvents(username);
        ObservableList<Event> eventList = FXCollections.observableArrayList(list);
        //ObservableList<Event> eventList = FXCollections.observableArrayList(new Event("01/01/2024", "Happy New Year", "Alor Setar", "9am-11am"), new Event("02/01/2024", "Happy New Year", "Alor Setar", "9am-11am"), new Event("03/01/2024", "Happy New Year", "Alor Setar", "9am-11am"), new Event("04/01/2024", "Happy New Year", "Alor Setar", "9am-11am"), new Event("05/01/2024", "Happy New Year", "Alor Setar", "9am-11am"));

        //associate data with column
        DateColumn.setCellValueFactory(new PropertyValueFactory<Event, String>("date"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<Event, String>("title"));
        VenueColumn.setCellValueFactory(new PropertyValueFactory<Event, String>("venue"));
        TimeColumn.setCellValueFactory(new PropertyValueFactory<Event, String>("time"));
        EventTable.setItems(eventList);
//        // Replace the connection URL, username, and password with your database credentials
//        String url = "jdbc:mysql://localhost:3306/your_database";
//        String username = "your_username";
//        String password = "your_password";
//
//        try (Connection connection = DriverManager.getConnection(url, username, password)) {
//            // Fetch data from the database
//            String query = "SELECT no, parent FROM your_table";
//            PreparedStatement statement = connection.prepareStatement(query);
//            ResultSet resultSet = statement.executeQuery();
//
//            // Add fetched data to the TableView
//            while (resultSet.next()) {
//                int no = resultSet.getInt("no");
//                String parent = resultSet.getString("parent");
//                parentList.add(new Parent(no, parent));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        //EventTable.setItems(eventList);
        //
    }

    private void setUpBookedStudyTourTable(String username) {
        ArrayList<BookedStudyTour> list = Students.getStudentBookedStudyTour(username);
        ObservableList<BookedStudyTour> bookedStudyTourList = FXCollections.observableArrayList(list);
        
        //ObservableList<BookedStudyTour> bookedStudyTourList = FXCollections.observableArrayList(new BookedStudyTour("01/01/2024", "Alor Setar"), new BookedStudyTour("02/01/2024", "Alor Setar"), new BookedStudyTour("03/01/2024", "Alor Setar"), new BookedStudyTour("04/01/2024", "Alor Setar"), new BookedStudyTour("05/01/2024", "Alor Setar"));

        //associate data with column
        BookedDateColumn.setCellValueFactory(new PropertyValueFactory<BookedStudyTour, String>("date"));
        BookedVenueColumn.setCellValueFactory(new PropertyValueFactory<BookedStudyTour, String>("venue"));
        BookedStudyTourTable.setItems(bookedStudyTourList);
        
//        // Replace the connection URL, username, and password with your database credentials
//        String url = "jdbc:mysql://localhost:3306/your_database";
//        String username = "your_username";
//        String password = "your_password";
//
//        try (Connection connection = DriverManager.getConnection(url, username, password)) {
//            // Fetch data from the database
//            String query = "SELECT no, parent FROM your_table";
//            PreparedStatement statement = connection.prepareStatement(query);
//            ResultSet resultSet = statement.executeQuery();
//
//            // Add fetched data to the TableView
//            while (resultSet.next()) {
//                int no = resultSet.getInt("no");
//                String parent = resultSet.getString("parent");
//                parentList.add(new Parent(no, parent));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        //BookedStudyTourTable.setItems(bookedStudyTourList);
    }

    private void setUpProfilePage(String username) {
//        String email = "email"; //get email 
//        String location = "location"; //get location 
//        String totalNumOfFriend = "10"; //get total num of friend 

        //modified
//        student.displayStudentInfo(username);
//        student.displayFriendInfo(username);
        Students.displayStudentInfo(username);
//        String totalNumOfFriend = String.valueOf(student.getPoints());
//        UsernameProfilePage.setText(student.getUsername());
//        UsernameLabel.setText(student.getUsername());

        //cannot getEmail
        //EmailLabel.setText(student.getEmail());
//        LocationLabel.setText(student.getLocation());
//        NumOfFriend.setText(totalNumOfFriend);

        //modified
        UsernameProfilePage.setText(username);
        UsernameLabel.setText(username);
        EmailLabel.setText("123");
        LocationLabel.setText("");
        NumOfFriend.setText("");
        //modified
        //ArrayList<String> friendNames = student.getFriendList(username);
        //List<String> friendNames = Arrays.asList("Friend 1", "Friend 2", "Friend 3"); // Retrieve friend data from the database, assuming it returns a list of friend names
        // Add each friend to the friend list
//        for (String friendName : friendNames) {
//            addFriendList(friendName);
//        }

            setUpParentTable(username);
            setUpEventTable(username);
            setUpBookedStudyTourTable(username);

            //modified
            //int point = student.getPoints();
            //int point = 20; //retrive from database
            //PointDisplay.setText(String.valueOf(point) + " POINTS");
        }
    

    public static void showReminderDialog(String warningContent) {
        // Create a new alert dialog
        Alert alert = new Alert(Alert.AlertType.WARNING);

        // Set the title and content text
        alert.setTitle("Reminder");
        alert.setHeaderText(null);
        alert.setContentText(warningContent);

        // Show the alert and wait for the user's response
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK);
    }

    public void editProfile_Username_Email(String username) {

        try {
            // Connect to database
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // Get new username and email from text fields
            String oldPassword1 = OldPassword1.getText();
            String newUsernameChange = NewUsername.getText();
            String newEmailChange = NewEmail.getText();
            String currentUsername = ""; // Store the current username
            String currentEmail = ""; // Store the current email

            if (oldPassword1.isBlank() || newUsernameChange.isBlank() || newEmailChange.isBlank()) {
                showReminderDialog("Please fill in all information!!!");
                return;
            }

            // Check if old password matches the one in the database
            String checkQuery = "SELECT Username, Email FROM student WHERE Password = ?";
            PreparedStatement checkStatement = connectDB.prepareStatement(checkQuery);
            checkStatement.setString(1, oldPassword1);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                currentUsername = resultSet.getString("Username");
                currentEmail = resultSet.getString("Email");
            } else {
                System.out.println("Old password does not match.");
                showReminderDialog("Old password does not match.");
                OldPassword1.setText("");
                NewUsername.setText("");
                NewEmail.setText("");
                checkStatement.close();
                connectDB.close();
                return;
            }
            checkStatement.close();

            // Check the conditions and update
            if (!currentUsername.equals(newUsernameChange) && currentEmail.equals(newEmailChange)) {
                // if only username changes
                String updateQuery = "UPDATE student SET Username = ? WHERE Password = ?";
                PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                updateStatement.setString(1, newUsernameChange);
                updateStatement.setString(2, oldPassword1);
                updateStatement.executeUpdate();
                updateStatement.close();
                System.out.println("Username updated successfully.");
                showReminderDialog("Username updated successfully.");
            } else if (currentUsername.equals(newUsernameChange) && !currentEmail.equals(newEmailChange)) {
                // if only email changes
                String updateQuery = "UPDATE student SET Email = ? WHERE Password = ?";
                PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                updateStatement.setString(1, newEmailChange);
                updateStatement.setString(2, oldPassword1);
                updateStatement.executeUpdate();
                updateStatement.close();
                System.out.println("Email updated successfully.");
                showReminderDialog("Email updated successfully.");
            } else if (!currentUsername.equals(newUsernameChange) && !currentEmail.equals(newEmailChange)) {
                // if both username and email changes
                String updateQuery = "UPDATE student SET Username = ?, Email = ? WHERE Password = ?";
                PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                updateStatement.setString(1, newUsernameChange);
                updateStatement.setString(2, newEmailChange);
                updateStatement.setString(3, oldPassword1);
                updateStatement.executeUpdate();
                updateStatement.close();
                System.out.println("Username and Email updated successfully.");
                showReminderDialog("Username and Email updated successfully.");
            } else {
                System.out.println("No changes.");
                showReminderDialog("No changes.");
            }
            OldPassword1.setText("");
            NewUsername.setText("");
            NewEmail.setText("");
            // Close connection
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }

//    public void editProfile_Password(String username) {
//        try {
//            // Connect to database
//            DatabaseConnection connectNow = new DatabaseConnection();
//            Connection connectDB = connectNow.linkDatabase();
//
//            // Get old password, new password, and confirmation password from text fields
//            String oldPassword2 = OldPassword2.getText();
//            String newPassword = NewPassword.getText();
//            String confirmPassword = ConfirmPassword.getText();
//            
//            if (oldPassword2.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
//                    showReminderDialog("Please fill in all information!!!");
//                    return;
//                }
//            
//            // Verify old password
//            String checkQuery = "SELECT Password FROM student WHERE Username = ?";
//            PreparedStatement checkStatement = connectDB.prepareStatement(checkQuery);
//            checkStatement.setString(1, username);
//            ResultSet resultSet = checkStatement.executeQuery();
//
//            if (resultSet.next()) {
//                String currentPassword = resultSet.getString("Password");
//                if (!currentPassword.equals(oldPassword2)) {
//                    System.out.println("Old password does not match.");
//                    showReminderDialog("Old password does not match.");
//                    checkStatement.close();
//                    connectDB.close();
//                    return;
//                }
//            } else {
//                System.out.println("Username not found.");
//                checkStatement.close();
//                connectDB.close();
//                return;
//            }
//            checkStatement.close();
//
//            // Check if new password and confirm password match
//            if (!newPassword.equals(confirmPassword)) {
//                System.out.println("New password and confirmation password do not match. Please enter again. ");
//                showReminderDialog("New password and confirmation password do not match. Please enter again. ");
//                connectDB.close();
//                return;
//            }
//            
//            // Update password in the database
//            String updateQuery = "UPDATE student SET Password = ? WHERE Username = ?";
//            PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
//            updateStatement.setString(1, newPassword);
//            updateStatement.setString(2, username);
//            updateStatement.executeUpdate();
//
//            updateStatement.close();
//            connectDB.close();
//
//            System.out.println("Password updated successfully.");
//            showReminderDialog("Password updated successfully.");
//
//        } catch (Exception e) {
//            System.out.println("SQL query failed.");
//            e.printStackTrace();
//        }
//    }
//    public void editProfile_Password(String username) {
//        //Get old password, new password, and confirmation password from text fields
//        String oldPassword2 = OldPassword2.getText();
//        String newPassword = NewPassword.getText();
//        String confirmPassword = ConfirmPassword.getText();
//
//        if (oldPassword2.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
//            showReminderDialog("Please fill in all information!!!");
//            return;
//        }
//        sessionManager.changePassword(oldPassword2, newPassword, confirmPassword);
//    }
}
