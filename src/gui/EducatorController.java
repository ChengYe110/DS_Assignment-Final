/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import ds.assignment.DatabaseConnection;
import ds.assignment.Discussion;
import ds.assignment.Login;
import ds.assignment.NumEventCreated;
import ds.assignment.NumQuizCreated;
import ds.assignment.Points;
import ds.assignment.Quiz;
import ds.assignment.SessionManager;
import ds.assignment.UserRepository;
import java.awt.Desktop;
import java.io.IOException;
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
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ds.assignment.Students;
import ds.assignment.User;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author enjye
 */
public class EducatorController implements Initializable {

    @FXML
    private StackPane stackPane, ExtraStackPane, ProfileImage;
    @FXML
    private Button DiscussionPage, EventPage, HomePage, LeaderboardPage, MenuButton, ProfilePage, QuizPage,
            CreateQuizPage, DoneCreateQuiz, DoneCreateEvent, CreateEventPage, CreateDiscussionPage, DoneCreateDiscussion,
            ChangeUsernameAndEmailButton, ChangePasswordButton, SaveChangeUsernameAndEmailButton, SaveChangePasswordButton,
            EditProfilePage, ExitEditProfilePage, FilterButton, LogOutButton, NextButton, PreviousButton,
            CreateQuizShortCut, CreateEventShortCut;
    @FXML
    private VBox DrawerPane, QuizVBox, DiscussionVBox, FilterVBox;
    @FXML
    private AnchorPane MenuPane, TopPane, HomePagePane, LeaderboardPane, QuizPane, CreateQuizPane, EventPane,
            CreateEventPane, EducatorProfilePane, DiscussionPane, CreateDiscussionPane, ChangeUsernameAndEmailPane,
            ChangePasswordPane, EditProfilePane;
    @FXML
    private Text UsernameMenuPane, UsernameProfilePage, Winner1, Winner1pts, Winner2, Winner2pts, Winner3, Winner3pts, Winner4,
            Winner4pts, Winner5, Winner5pts, Winner6, Winner6pts, Winner7, Winner7pts, Winner8, Winner8pts, Winner9, Winner9pts,
            Winner10, Winner10pts, totalQuizCreated, totalEventCreated;
    @FXML
    private TextArea QuizDescriptionField, EventDescriptionField, DiscussionContentField;
    @FXML
    private Label UsernameLabel, EmailLabel, LocationLabel;
    @FXML
    private TextField NewUsername, NewEmail, ParentUsernameField, EventTitleField, EventVenueField, QuizTitleField, QuizContentField,
            DiscussionTitleField;
    @FXML
    private PasswordField OldPassword1, OldPassword2, NewPassword, ConfirmPassword;
    @FXML
    private ChoiceBox<String> QuizThemeChoiceBox, EventTimeChoiceBox;
    @FXML
    private DatePicker EventDatePicker;
    @FXML
    private HBox MENU, LiveEventHBox, EventHBox1, EventHBox2, EventHBox3;
    private Button selectedButton = null;
    private ObservableList<String> theme = FXCollections.observableArrayList("SCIENCE", "TECHNOLOGY", "ENGINEERING", "MATHEMATIC");
    private ObservableList<String> time = FXCollections.observableArrayList("8 am - 10 am", "10 am - 12 pm", "12 pm - 2 pm", "2 pm - 4 pm", "4 pm - 6 pm", "6 pm - 8 pm");

    @FXML
    private ScrollPane QuizScrollPane, DiscussionScrollPane;

    private TranslateTransition slideOutTransition, slideInTransition;
    private int currentIndex;
    public static Stack<String> friendNameNavigateEducator = new Stack<>();

    DatabaseConnection dbConnect = new DatabaseConnection();
    UserRepository userRepository = new UserRepository(dbConnect);
    Login login = new Login();  // Create a single instance of Login
    SessionManager sessionManager = new SessionManager(userRepository, login);  // Pass the Login instance to SessionManage
    NumQuizCreated numQuizCreatedFromDataBase = new NumQuizCreated();
    NumEventCreated numEventCreatedFromDataBase = new NumEventCreated();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Execute after the layout pass is complete
        Platform.runLater(() -> {
            // Initially hide the MenuPane off the screen
            MenuPane.setTranslateX(-MenuPane.getWidth());
            switchHomePage();
            setProfileImage();

            // Create TranslateTransitions for sliding in and out
            slideOutTransition = new TranslateTransition(Duration.seconds(0.5), MenuPane);
            slideOutTransition.setToX(0); // Slide to the final position

            slideInTransition = new TranslateTransition(Duration.seconds(0.5), MenuPane);
            slideInTransition.setToX(-MenuPane.getWidth()); // Slide back to the initial position

            ButtonEffect(MenuButton);
            MenuButton.setOnAction(event -> {
                if (MenuPane.getTranslateX() != 0) {
                    slideOutTransition.play(); // Slide out the menu
                } else {
                    slideInTransition.play(); // Slide in the menu
                }
            });

            EventTimeChoiceBox.setItems(time);
            ProfilePage.setOnAction(event -> {
                selectedButton.setId("");
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                stackPane.getChildren().clear();
                stackPane.getChildren().add(EducatorProfilePane);
                EditProfilePane.setVisible(false);
                setUpProfilePage(sessionManager.getCurrentUser().getUsername());
            });
            EditProfilePage.setOnAction(event -> {
                EditProfilePane.setVisible(true);
                EditProfilePane.toFront();
                ExtraStackPane.getChildren().clear();
                ExtraStackPane.getChildren().add(ChangeUsernameAndEmailPane);
                NewUsername.setText(sessionManager.getCurrentUser().getUsername()); //get username from database
                NewEmail.setText(userRepository.getEmailByUsername(sessionManager.getCurrentUser().getUsername())); //get email from database   
                ChangeUsernameAndEmailButton.setStyle("-fx-background-color: white;");
                ChangePasswordButton.setStyle("-fx-background-color: transparent;");
            });
            ButtonEffect(CreateQuizShortCut);
            CreateQuizShortCut.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                clearCreateQuiz();
                stackPane.getChildren().clear();
                stackPane.getChildren().add(CreateQuizPane);
            });
            CreateEventShortCut.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                clearCreateEvent();
                stackPane.getChildren().clear();
                stackPane.getChildren().add(CreateEventPane);
            });
            ButtonEffect(ChangeUsernameAndEmailButton);
            ChangeUsernameAndEmailButton.setOnAction(event -> {
                ExtraStackPane.getChildren().clear();
                ExtraStackPane.getChildren().add(ChangeUsernameAndEmailPane);
                NewUsername.setText(sessionManager.getCurrentUser().getUsername()); //get username from database
                NewEmail.setText(userRepository.getEmailByUsername(sessionManager.getCurrentUser().getUsername())); //get email from database
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
                String oldPassword = OldPassword1.getText();
                String newUsername = NewUsername.getText();
                String newEmail = NewEmail.getText();
                if (oldPassword.isBlank() || newUsername.isBlank() || newEmail.isBlank()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all information!!!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    editProfile_Username_Email(sessionManager.getCurrentUser().getUsername());
                }
            });
            SaveChangePasswordButton.setOnAction(event -> {
                String oldPassword = OldPassword2.getText();
                String newPassword = NewPassword.getText();
                String confirmPassword = ConfirmPassword.getText();
                if (oldPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all information!!!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    editProfile_Password(sessionManager.getCurrentUser().getUsername());
                }
            });

            DiscussionScrollPane.setContent(DiscussionVBox);
            DiscussionPage.setOnAction(event -> {
                selectedButton.setId("");
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                refreshDiscussion();
                stackPane.getChildren().clear();
                stackPane.getChildren().add(DiscussionPane);
            });
            CreateDiscussionPage.setOnAction(event -> {
                selectedButton.setId("");
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                clearCreateDiscussion();
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
                    JOptionPane.showMessageDialog(null, "Please fill in all information!!!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    createDiscussion();
                    stackPane.getChildren().clear();
                    stackPane.getChildren().add(DiscussionPane);
                }
            });

            QuizScrollPane.setContent(QuizVBox);
            createFilterDropdown(theme);
            QuizPage.setOnAction(event -> {
                selectedButton.setId("");
                ObservableList<Node> children = FilterVBox.getChildren();
                for (int i = 1; i < children.size(); i++) { // Start from 1 to skip the FilterButton
                    Node node = children.get(i);
                    if (node instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) node;
                        checkBox.setVisible(false);
                        checkBox.setSelected(false); // Ensure the checkbox is unselected
                    }
                }
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                refreshQuiz();
                stackPane.getChildren().clear();
                stackPane.getChildren().add(QuizPane);
            });
            QuizThemeChoiceBox.setItems(theme);
            CreateQuizPage.setOnAction(event -> {
                selectedButton.setId("");
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                clearCreateQuiz();
                stackPane.getChildren().clear();
                stackPane.getChildren().add(CreateQuizPane);
            });
            DoneCreateQuiz.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                createQuiz();
                ObservableList<Node> children = FilterVBox.getChildren();
                for (int i = 1; i < children.size(); i++) { // Start from 1 to skip the FilterButton
                    Node node = children.get(i);
                    if (node instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) node;
                        checkBox.setVisible(false);
                        checkBox.setSelected(false); // Ensure the checkbox is unselected
                    }
                }
            });

            EventPage.setOnAction(event -> {
                selectedButton.setId("");
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                refreshEvent();
                stackPane.getChildren().clear();
                stackPane.getChildren().add(EventPane);
            });
            CreateEventPage.setOnAction(event -> {
                selectedButton.setId("");
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                clearCreateEvent();
                stackPane.getChildren().clear();
                stackPane.getChildren().add(CreateEventPane);
            });
            currentIndex = 0;
            ButtonEffect(PreviousButton);
            ButtonEffect(NextButton);

            DoneCreateEvent.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                createEvent();
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
                        selectedButton.setId("selected");
                    } else if (btn.equals(LeaderboardPage)) {
                        switchLeaderboardPage();
                        selectedButton.setId("selected");
                    }
                });
            }
            selectedButton = (Button) MENU.getChildren().get(0);
            selectedButton.setId("selected");
        }
        );
        LogOutButton.setOnAction(event
                -> {
            try {
                // Load the login page FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Educator/Login_Register.fxml"));
                Parent loginRoot = loader.load();

                // Get the current stage
                Stage stage = (Stage) LogOutButton.getScene().getWindow();

                // Set the scene with the login layout
                Scene scene = new Scene(loginRoot);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        );
        setUsername(sessionManager.getCurrentUser().getUsername());
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
        refreshPoints();
        stackPane.getChildren().clear();
        stackPane.getChildren().add(LeaderboardPane);
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
            stage.initOwner(((Stage) MENU.getScene().getWindow()));

            stage.setResizable(false);
            stage.show();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addNewQuiz(Quiz quiz) {
        // Create the HBox
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setSpacing(10);

        // Create the VBox
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setPadding(new Insets(0, 0, 0, 20));

        // Add three text elements to the VBox
        Text titleText = new Text(quiz.getTitle());

        HBox temp = new HBox();
        temp.setSpacing(5);
        temp.setAlignment(Pos.TOP_LEFT);
        Text t = new Text("Theme: ");
        t.setStyle("-fx-fill: #737373; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 16px; ");
        Button themeButton = new Button(quiz.getTheme());
        String setColour = "";
        if (quiz.getTheme().equals("SCIENCE")) {
            setColour = "-fx-background-color: #c3dbc2;";
        } else if (quiz.getTheme().equals("TECHNOLOGY")) {
            setColour = "-fx-background-color: #a7d1df;";
        } else if (quiz.getTheme().equals("ENGINEERING")) {
            setColour = "-fx-background-color: #e4cfb4;";
        } else if (quiz.getTheme().equals("MATHEMATIC")) {
            setColour = "-fx-background-color: #f0c9dc;";
        }
        themeButton.setStyle(setColour + "-fx-text-fill: white; -fx-font-family: \"Segoe UI Black\";-fx-font-size: 12px; -fx-background-radius: 20px;");
        temp.getChildren().addAll(t, themeButton);

        Text descriptionText = new Text(quiz.getDescription());
        descriptionText.setWrappingWidth(800);
        titleText.setStyle("-fx-text-fill: black; -fx-font-family: \"Segoe UI Black\";-fx-font-size: 30px; ");
        descriptionText.setStyle("-fx-text-fill: black; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 14px; ");
        vBox.getChildren().addAll(titleText, temp, descriptionText);

        // Create a button
        Button button = new Button("ATTEMPT QUIZ");
        button.setPrefWidth(150);
        button.setStyle("-fx-background-color: linear-gradient( to right,#8c52ff, #5ce1e6); -fx-text-fill: white; -fx-font-family: \"Segoe UI Black\";-fx-font-size: 16px; -fx-background-radius: 20px;");
        button.setDisable(true);

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

    private void addNewDiscussion(Discussion discussion) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setSpacing(10);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setPadding(new Insets(0, 0, 0, 20));

        Text titleText = new Text(discussion.getTitle());

        HBox temp = new HBox();
        temp.setSpacing(5);
        temp.setAlignment(Pos.TOP_LEFT);
        Text t = new Text("Posted By: ");
        t.setStyle("-fx-fill: #737373; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 16px; ");
        String role = userRepository.getRole(discussion.getAuthor()).toUpperCase();
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

        String username = discussion.getAuthor();
        Button usernameButton = new Button(username);
        usernameButton.getStyleClass().add("username-button");
        usernameButton.setOnAction(event -> {
            if (friendNameNavigateEducator.contains(username) || username.equals(sessionManager.getCurrentUser().getUsername())) {
                usernameButton.setDisable(true);
            } else {
                friendNameNavigateEducator.push(username);
                openProfilePage(friendNameNavigateEducator.peek());
            }
        });
        Text dateText = new Text("(" + discussion.getDatetime() + ")");
        dateText.setStyle("-fx-fill: #EDAB5E; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 16px; ");
        temp.getChildren().addAll(t, roleButton, usernameButton, dateText);

        Text contentText = new Text(discussion.getContent());
        contentText.setWrappingWidth(880);
        titleText.setStyle("-fx-text-fill: black; -fx-font-family: \"Segoe UI Black\";-fx-font-size: 30px; ");
        contentText.setStyle("-fx-text-fill: black; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 14px; ");
        vBox.getChildren().addAll(titleText, temp, contentText);

        // Create a heart shape using SVGPath
        SVGPath heartShape = new SVGPath();
        heartShape.setContent("M 5 15 Q 15 0 25 15 Q 35 0 45 15 Q 47.5 20 25 40 Q 2.5 20 5 15");
        heartShape.setFill(Color.BLACK); // Initial color

        //Check if liked then red; if never like then black;
        List<String> LikedPost = discussion.checkLiked(sessionManager.getCurrentUser().getUsername(), userRepository.getRole(sessionManager.getCurrentUser().getUsername()));
        if (LikedPost.contains(discussion.getId())) {
            heartShape.setFill(Color.RED); // If the post is liked, set color to red
        } else {
            heartShape.setFill(Color.BLACK); // If the post is not liked, set color to black
        }
        //likes num
        Text NumOfLike = new Text(String.valueOf(discussion.getLike()));
        NumOfLike.setStyle("-fx-text-fill: black; -fx-font-family: \"Segoe UI Black\";-fx-font-size: 12px; ");

        // Create a button with the heart shape
        Button loveButton = new Button();
        loveButton.setStyle("-fx-background-color: transparent;");
        loveButton.setGraphic(heartShape);
        loveButton.setOnAction(event -> {
            // Toggle button color between red and black
            if (heartShape.getFill().equals(Color.BLACK)) {
                discussion.addLike(sessionManager.getCurrentUser().getUsername(), userRepository.getRole(sessionManager.getCurrentUser().getUsername()));
                heartShape.setFill(Color.RED);
                NumOfLike.setText(String.valueOf(discussion.getLike()));
            } else {
                discussion.removeLike(sessionManager.getCurrentUser().getUsername(), userRepository.getRole(sessionManager.getCurrentUser().getUsername()));
                heartShape.setFill(Color.BLACK);
                NumOfLike.setText(String.valueOf(discussion.getLike()));
            }
        });

        // Add the VBox and the button to the HBox
        hBox.getChildren().addAll(vBox, loveButton, NumOfLike);

        // Add the HBox to the main VBox
        DiscussionVBox.getChildren().add(0, hBox);
    }

    private void setUsername(String username) {
        UsernameMenuPane.setText(username);
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
            String currentUsername = sessionManager.getCurrentUser().getUsername(); // Store the current username
            String currentEmail = userRepository.getEmailByUsername(currentUsername); // Store the current email

            // Check the conditions and update
            if (!currentUsername.equals(newUsernameChange) && currentEmail.equals(newEmailChange)) {
                // if only username changes
                try {
                    // Assuming setUsername may throw an exception on failure
                    if (login.isPasswordCorrectForUser(oldPassword1)) {
                        // Check if a user is currently logged in
                        if (sessionManager.getCurrentUser() != null) {
                            if (userRepository.isUsernameTaken(newUsernameChange)) {
                                NewUsername.setText(sessionManager.getCurrentUser().getUsername());
                            }
                            boolean updateSuccess = userRepository.updateUsernameInDatabase(currentEmail, newUsernameChange, oldPassword1);
                            if (updateSuccess) {
                                EditProfilePane.setVisible(false);
                                OldPassword1.setText("");
                                setUpProfilePage(sessionManager.getCurrentUser().getUsername());
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "No logged-in user. Username not updated.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        OldPassword1.setText("");
                        JOptionPane.showMessageDialog(null, "Incorrect password. Username not updated.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Failed to change username. Please check your inputs.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } else if (currentUsername.equals(newUsernameChange) && !currentEmail.equals(newEmailChange)) {
                // if only email changes
                String emailRegex = "^(.+)@(gmail\\.com|hotmail\\.com|yahoo\\.com|siswa\\.um\\.edu\\.my)$";
                if (!newEmailChange.matches(emailRegex)) {
                    JOptionPane.showMessageDialog(null, "Invalid email format. Please use a valid email address.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        // Assuming setUsername may throw an exception on failure
                        if (login.isPasswordCorrectForUser(oldPassword1)) {
                            // Check if a user is currently logged in
                            if (sessionManager.getCurrentUser() != null) {
                                if (!userRepository.isEmailTaken(newEmailChange)) {
                                    // Update email for the current user
                                    userRepository.updateEmailInDatabase(sessionManager.getCurrentUser().getUsername(), newEmailChange, oldPassword1);
                                    JOptionPane.showMessageDialog(null, "Email changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                    setUpProfilePage(sessionManager.getCurrentUser().getUsername());
                                    OldPassword1.setText("");
                                    EditProfilePane.setVisible(false);
                                } else {
                                    NewEmail.setText(userRepository.getEmailByUsername(sessionManager.getCurrentUser().getUsername()));
                                    JOptionPane.showMessageDialog(null, "Email already exists. Please choose a different email.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "No logged-in user. Username not updated.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            OldPassword1.setText("");
                            JOptionPane.showMessageDialog(null, "Incorrect password. Username not updated.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Failed to change email. Please check your inputs.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else if (!currentUsername.equals(newUsernameChange) && !currentEmail.equals(newEmailChange)) {

                if (login.isPasswordCorrectForUser(oldPassword1)) {
                    if (!userRepository.isUsernameTaken(newUsernameChange) && !userRepository.isEmailTaken(newEmailChange)) {
                        //username and email not taken
                        if (userRepository.updateUsernameInDatabaseNew(currentEmail, newUsernameChange, oldPassword1) && userRepository.updateEmailInDatabaseNew(newUsernameChange, newEmailChange, oldPassword1)) {
                            JOptionPane.showMessageDialog(null, "Username and Email has been updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else if (userRepository.isUsernameTaken(newUsernameChange) && !userRepository.isEmailTaken(newEmailChange)) {
                        //username taken only
                        if (userRepository.updateEmailInDatabaseNew(newUsernameChange, newEmailChange, oldPassword1)) {
                            JOptionPane.showMessageDialog(null, "Email updated succesfully! Username already exists. Please choose a different username.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else if (!userRepository.isUsernameTaken(newUsernameChange) && (userRepository.isEmailTaken(newEmailChange))) {
                        //email taken only
                        if (userRepository.updateUsernameInDatabaseNew(newEmailChange, newUsernameChange, oldPassword1)) {
                            JOptionPane.showMessageDialog(null, "Username updated succesfully! Email already exists. Please choose a different Email.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                        //clear email or reset to the initial
                        //set username with new one
                    } else {
                        //username and email  taken
                        JOptionPane.showMessageDialog(null, "Both Username and Email already exists. Please choose a different Email and Username.", "Error", JOptionPane.ERROR_MESSAGE);
                        //clear both or reset to the initial
                    }
                    NewUsername.setText(sessionManager.getCurrentUser().getUsername()); //get username from database
                    NewEmail.setText(userRepository.getEmailByUsername(sessionManager.getCurrentUser().getUsername())); //get email from database        
                    EditProfilePane.setVisible(false);
                    OldPassword1.setText("");
                    setUpProfilePage(sessionManager.getCurrentUser().getUsername());
                } else {
                    OldPassword1.setText("");
                    JOptionPane.showMessageDialog(null, "Incorrect password. Username and Email not updated.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please enter new Username or Email.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            // Close connection
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }

    public void editProfile_Password(String username) {
        // Get old password, new password, and confirmation password from text fields
        String oldPassword2 = OldPassword2.getText();
        String newPassword = NewPassword.getText();
        String confirmPassword = ConfirmPassword.getText();

        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Matcher hasUpperCase = upperCasePattern.matcher(newPassword);

        Pattern digitPattern = Pattern.compile("[0-9]");
        Matcher hasDigit = digitPattern.matcher(newPassword);

        if (login.isPasswordCorrectForUser(oldPassword2)) {
            // Check if the new password and confirmation match
            if (!newPassword.equals(confirmPassword)) {
                NewPassword.setText("");
                ConfirmPassword.setText("");
                JOptionPane.showMessageDialog(null, "New password and confirmation do not match. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (newPassword.length() < 8) {
                NewPassword.setText("");
                ConfirmPassword.setText("");
                JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!hasUpperCase.find()) {
                NewPassword.setText("");
                ConfirmPassword.setText("");
                JOptionPane.showMessageDialog(null, "Password must contain at least one uppercase letter.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!hasDigit.find()) {
                NewPassword.setText("");
                ConfirmPassword.setText("");
                JOptionPane.showMessageDialog(null, "Password must contain at least one digit.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (newPassword.equals(confirmPassword)) {
                // Update the password for the current user
                userRepository.updatePasswordInDatabase(sessionManager.getCurrentUser().getUsername(), newPassword);
                sessionManager.getCurrentUser().setPassword(newPassword); // Update the password field in the User object
                JOptionPane.showMessageDialog(null, "Password changed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                OldPassword2.setText("");
                NewPassword.setText("");
                ConfirmPassword.setText("");
                EditProfilePane.setVisible(false);
            }
        } else {
            OldPassword2.setText("");
            NewPassword.setText("");
            ConfirmPassword.setText("");
            JOptionPane.showMessageDialog(null, "Incorrect old password. Password not changed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public HBox createEventHBox(EventHBoxElement e) {
        String eventTitle = e.getEventTitle();
        String eventDescription = e.getEventDescription();
        String eventVenue = e.getEventVenue();
        String eventDate = e.getEventDateS();
        String eventTime = e.getEventTime();
        // Create the main HBox
        HBox hbox = new HBox();
        hbox.setPrefHeight(87.0);
        hbox.setPrefWidth(985.0);
        hbox.setStyle("-fx-background-color: white;");
        hbox.setAlignment(javafx.geometry.Pos.CENTER);

        // Create the first VBox for title and description
        VBox vbox1 = new VBox();
        vbox1.setPrefHeight(87.0);
        vbox1.setPrefWidth(512.0);

        Label title = new Label(eventTitle);
        title.setPrefHeight(32.0);
        title.setMaxWidth(519.0);
        title.setWrapText(true); // Enable text wrapping
        title.setFont(new Font("Segoe UI Black", 24.0));

        Text description = new Text(eventDescription);
        description.setWrappingWidth(519.0);
        description.setFont(new Font("Segoe UI Semibold", 12.0));

        vbox1.getChildren().addAll(title, description);

        // Create the second VBox for venue, date, and time
        VBox vbox2 = new VBox();
        vbox2.setAlignment(javafx.geometry.Pos.CENTER);
        vbox2.setPrefHeight(87.0);
        vbox2.setPrefWidth(282.0);

        HBox venueHBox = createLabelWithTextHBox("VENUE", "#ff5757", eventVenue);
        HBox dateHBox = createLabelWithTextHBox("DATE", "#4fc8ab", eventDate);
        HBox timeHBox = createLabelWithTextHBox("TIME", "#ffd230", eventTime);

        vbox2.getChildren().addAll(venueHBox, dateHBox, timeHBox);

        // Create the Join button
        Button joinButton = new Button("JOIN");
        joinButton.setPrefHeight(43.0);
        joinButton.setPrefWidth(137.0);
        joinButton.setStyle("-fx-background-radius: 25px;");
        joinButton.getStyleClass().add("colorGradientButton");
        joinButton.setTextFill(javafx.scene.paint.Color.WHITE);
        joinButton.setFont(new Font("Segoe UI Black", 20.0));
        joinButton.setDisable(true);
        // Add all children to the main HBox
        hbox.getChildren().addAll(vbox1, vbox2, joinButton);

        return hbox;
    }

    private HBox createLabelWithTextHBox(String labelText, String bgColor, String textValue) {
        HBox hbox = new HBox();
        hbox.setAlignment(javafx.geometry.Pos.CENTER);
        hbox.setPrefHeight(32.0);
        hbox.setPrefWidth(260.0);
        hbox.setSpacing(10.0);

        Label label = new Label(labelText);
        label.setAlignment(javafx.geometry.Pos.CENTER);
        label.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
        label.setPrefHeight(23.0);
        label.setPrefWidth(87.0);
        label.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 20px;");
        label.setTextFill(javafx.scene.paint.Color.WHITE);
        label.setFont(new Font("Segoe UI Black", 16.0));

        Text text = new Text(textValue);
        text.setWrappingWidth(150.0);
        text.setFont(new Font("Segoe UI Semibold", 14.0));

        hbox.getChildren().addAll(label, text);

        return hbox;
    }

    public void addEventHBoxToParent(HBox parent, EventHBoxElement e) {
        HBox eventHBox = createEventHBox(e);
        parent.getChildren().add(eventHBox);
    }

    private void reloadLiveEventHBox(ArrayList<EventHBoxElement> list) {
        LiveEventHBox.getChildren().clear(); // Clear previous content
        if (!list.isEmpty()) {
            // Add current event to parentHBox
            addEventHBoxToParent(LiveEventHBox, list.get(currentIndex));
            PreviousButton.setOnAction(e -> showPreviousEvent(list));
            NextButton.setOnAction(e -> showNextEvent(list));
            // Update button visibility based on currentIndex
            PreviousButton.setVisible(currentIndex > 0);
            NextButton.setVisible(currentIndex < list.size() - 1);
        } else {
            PreviousButton.setVisible(false);
            NextButton.setVisible(false);
        }
    }

    private void showPreviousEvent(ArrayList<EventHBoxElement> list) {
        if (currentIndex > 0) {
            currentIndex--;
            reloadLiveEventHBox(list);
        }
    }

    private void showNextEvent(ArrayList<EventHBoxElement> list) {
        if (currentIndex < list.size() - 1) {
            currentIndex++;
            reloadLiveEventHBox(list);
        }
    }

    private void createQuiz() {
        String title = QuizTitleField.getText();
        String description = QuizDescriptionField.getText();
        String theme = QuizThemeChoiceBox.getValue();
        String content = QuizContentField.getText();

        if (title.isBlank() || description.isBlank() || theme == null || content.isBlank()) {
            JOptionPane.showMessageDialog(null, "Please fill in all information!!!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Quiz quiz = new Quiz(title, description, theme, content);
            quiz.saveQuiz(sessionManager.getCurrentUser().getUsername());
            numQuizCreatedFromDataBase.addQuizCreated(1);
            refreshQuiz();
            stackPane.getChildren().clear();
            stackPane.getChildren().add(QuizPane);
            clearCreateQuiz();
        }
    }

    private void refreshQuiz() {
        List<Quiz> quizList = Quiz.getQuizList();
        QuizVBox.getChildren().clear();
        for (Quiz quiz : quizList) {
            addNewQuiz(quiz);
        }
    }

    private void clearCreateQuiz() {
        QuizTitleField.clear();
        QuizDescriptionField.clear();
        QuizThemeChoiceBox.setValue(null);
        QuizContentField.clear();
    }

    public static Date convertToDate(LocalDate localDate) {
        // Convert LocalDate to Instant
        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        // Convert Instant to Date
        return Date.from(instant);
    }

    private void createEvent() {
        String title = EventTitleField.getText();
        String description = EventDescriptionField.getText();
        String venue = EventVenueField.getText();
        LocalDate dateA = EventDatePicker.getValue();
        String time = EventTimeChoiceBox.getValue();

        if (title.isBlank() || description.isBlank() || venue.isBlank() || dateA == null || time.isBlank()) {
            JOptionPane.showMessageDialog(null, "Please fill in all information!!!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (dateA.isBefore(LocalDate.now()) || dateA.equals(LocalDate.now())) {
            JOptionPane.showMessageDialog(null, "Please choose the date after " + LocalDate.now(), "Error", JOptionPane.ERROR_MESSAGE);
            EventDatePicker.setValue(null);
        } else {
            EventHBoxElement eventHboxElement = new EventHBoxElement(title, description, venue, dateA, time);
            eventHboxElement.saveEvent(sessionManager.getCurrentUser().getUsername());
            numEventCreatedFromDataBase.addEventCreated(1);
            refreshEvent();
            stackPane.getChildren().clear();
            stackPane.getChildren().add(EventPane);
            clearCreateEvent();
        }
    }

    private void refreshEvent() {
        ArrayList<EventHBoxElement> EventHBoxElementList = User.getLiveEventList();
        LiveEventHBox.getChildren().clear();
        reloadLiveEventHBox(EventHBoxElementList);

        ArrayList<EventHBoxElement> LatestEventList = User.getLatestEventList();
        EventHBox1.getChildren().clear();
        EventHBox2.getChildren().clear();
        EventHBox3.getChildren().clear();
        if (LatestEventList.size() >= 1) {
            addEventHBoxToParent(EventHBox1, LatestEventList.get(0));
            if (LatestEventList.size() >= 2) {
                addEventHBoxToParent(EventHBox2, LatestEventList.get(1));
                if (LatestEventList.size() >= 3) {
                    addEventHBoxToParent(EventHBox3, LatestEventList.get(2));
                }
            }
        }
    }

    private void clearCreateEvent() {
        EventTitleField.clear();
        EventDescriptionField.clear();
        EventVenueField.clear();
        EventDatePicker.setValue(null);
        EventTimeChoiceBox.setValue("");
    }

    private void createDiscussion() {
        String title = DiscussionTitleField.getText();
        String content = DiscussionContentField.getText();
        int like = 0;
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDateTime = currentDateTime.format(dateTimeFormatter);

        if (title.isBlank() || content.isBlank()) {
            JOptionPane.showMessageDialog(null, "Please fill in all information!!!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Discussion discussion = new Discussion(title, content, userRepository.getID(sessionManager.getCurrentUser().getUsername()), like, formattedDateTime);
            discussion.saveDiscussion(sessionManager.getCurrentUser().getUsername());
            refreshDiscussion();
            stackPane.getChildren().clear();
            stackPane.getChildren().add(DiscussionPane);
            clearCreateDiscussion();
        }
    }

    private void refreshDiscussion() {
        List<Discussion> discussionList = Discussion.getDiscussionList();
        DiscussionVBox.getChildren().clear();
        for (Discussion discussion : discussionList) {
            addNewDiscussion(discussion);
        }
    }

    private void clearCreateDiscussion() {
        DiscussionTitleField.clear();
        DiscussionContentField.clear();
    }

    public void refreshPoints() {
        sessionManager.timestampPoints();
        updateGlobalLeaderboard();
    }

    public void updateGlobalLeaderboard() {
        Winner1.setText(sessionManager.getTopUsername(0));
        Winner2.setText(sessionManager.getTopUsername(1));
        Winner3.setText(sessionManager.getTopUsername(2));
        Winner4.setText(sessionManager.getTopUsername(3));
        Winner5.setText(sessionManager.getTopUsername(4));
        Winner6.setText(sessionManager.getTopUsername(5));
        Winner7.setText(sessionManager.getTopUsername(6));
        Winner8.setText(sessionManager.getTopUsername(7));
        Winner9.setText(sessionManager.getTopUsername(8));
        Winner10.setText(sessionManager.getTopUsername(9));
        Winner1pts.setText(sessionManager.getTopPoints(0) + " pts");
        Winner2pts.setText(sessionManager.getTopPoints(1) + " pts");
        Winner3pts.setText(sessionManager.getTopPoints(2) + " pts");
        Winner4pts.setText(sessionManager.getTopPoints(3) + " pts");
        Winner5pts.setText(sessionManager.getTopPoints(4) + " pts");
        Winner6pts.setText(sessionManager.getTopPoints(5) + " pts");
        Winner7pts.setText(sessionManager.getTopPoints(6) + " pts");
        Winner8pts.setText(sessionManager.getTopPoints(7) + " pts");
        Winner9pts.setText(sessionManager.getTopPoints(8) + " pts");
        Winner10pts.setText(sessionManager.getTopPoints(9) + " pts");
    }

    private void setProfileImage() {
        // Load the image
        Image image = new Image(getClass().getResource("../png/EducatorProfile.jpg").toExternalForm()); // Replace with your image path

        // Create a circle with a specific radius
        Circle circle = new Circle(100); // Radius 100 pixels

        // Set the fill of the circle to the image using an ImagePattern
        circle.setFill(new ImagePattern(image));

        // Create a border circle
        Circle borderCircle = new Circle(101); // Slightly larger radius for the border
        borderCircle.setStroke(Color.BLACK); // Set the border color
        borderCircle.setStrokeWidth(3); // Set the border width
        borderCircle.setFill(null); // Ensure the border circle is transparent

        // Create a layout pane and add the circle to it
        ProfileImage.getChildren().addAll(circle, borderCircle);
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
}
