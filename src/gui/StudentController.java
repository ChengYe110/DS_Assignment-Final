/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
            FriendRequestPage, ExitFriendRequestPage,ExitViewFriendProfilePage;
    @FXML
    private VBox DrawerPane, FriendListVBox, FriendRequestVBox, QuizVBox,DiscussionVBox;
    @FXML
    private AnchorPane MenuPane, TopPane, HomePagePane, LeaderboardPane, QuizPane, CreateQuizPane, EventPane,
            CreateEventPane, BookingPane, StudentProfilePane, FriendListPane, FriendRequestPane,ViewFriendProfilePage,
            DiscussionPane;
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
    private ScrollPane FriendListScrollPane, FriendRequestScrollPane, QuizScrollPane,DiscussionScrollPane;

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
                //ViewFriendProfilePage.setVisible(false);
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

            QuizScrollPane.setContent(QuizVBox);
            QuizPage.setOnAction(event -> {
                if (MenuPane.getTranslateX() == 0) {
                    slideInTransition.play();
                }
                stackPane.getChildren().clear();
                stackPane.getChildren().add(QuizPane);
            });

            FilterChoiceBox.setOnAction(event -> {
                String selectedTheme = FilterChoiceBox.getValue();
                if (selectedTheme != null && !selectedTheme.isEmpty()) {
                    filterTheme(selectedTheme); // Call your method to sort QuizVBox based on the selected theme
                }
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

        //testing
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
        addNewQuiz("MockTestQuestion", "ENGINEERING", "hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii", "https://en.wikipedia.org/wiki/Cha_Eun-woo");
        addNewQuiz("MockTestQuestion", "MATHEMATIC", "hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii", "https://en.wikipedia.org/wiki/Cha_Eun-woo");
        addNewQuiz("MockTestQuestion", "ENGINEERING", "hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii", "https://en.wikipedia.org/wiki/Cha_Eun-woo");
        addNewQuiz("MockTestQuestion", "ENGINEERING", "hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii", "https://en.wikipedia.org/wiki/Cha_Eun-woo");
        addNewQuiz("MockTestQuestion", "TECHNOLOGY", "hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii", "https://en.wikipedia.org/wiki/Cha_Eun-woo");
        addNewQuiz("MockTestQuestion", "TECHNOLOGY", "hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii", "https://en.wikipedia.org/wiki/Cha_Eun-woo");
        addNewDiscussion("HiTesting", "STUDENT", "jack", "yoyooyooyoyoyoyyyyyyyyyooyoyooyoyoyoyoyooyoyoyoooyoyoyoyoyyoooooyyoyoyoyyyyyoyoyoyoyoyoyoyoyoyoyoyoyoyoy");
        addNewDiscussion("HiTesting", "PARENT", "jack", "yoyooyooyoyoyoyyyyyyyyyooyoyooyoyoyoyoyooyoyoyoooyoyoyoyoyyoooooyyoyoyoyyyyyoyoyoyoyoyoyoyoyoyoyoyoyoyoy");
        addNewDiscussion("HiTesting", "EDUCATOR", "jack", "yoyooyooyoyoyoyyyyyyyyyooyoyooyoyoyoyoyooyoyoyoooyoyoyoyoyyoooooyyoyoyoyyyyyoyoyoyoyoyoyoyoyoyoyoyoyoyoy");
        addNewDiscussion("HiTesting", "STUDENT", "jack", "yoyooyooyoyoyoyyyyyyyyyooyoyooyoyoyoyoyooyoyoyoooyoyoyoyoyyoooooyyoyoyoyyyyyoyoyoyoyoyoyoyoyoyoyoyoyoyoy");
        addNewDiscussion("HiTesting", "PARENT", "jack", "yoyooyooyoyoyoyyyyyyyyyooyoyooyoyoyoyoyooyoyoyoooyoyoyoyoyyoooooyyoyoyoyyyyyoyoyoyoyoyoyoyoyoyoyoyoyoyoy");
        addNewDiscussion("HiTesting", "STUDENT", "jack", "yoyooyooyoyoyoyyyyyyyyyooyoyooyoyoyoyoyooyoyoyoooyoyoyoyoyyoooooyyoyoyoyyyyyoyoyoyoyoyoyoyoyoyoyoyoyoyoy");
        addNewDiscussion("HiTesting", "EDUCATOR", "jack", "yoyooyooyoyoyoyyyyyyyyyooyoyooyoyoyoyoyooyoyoyoooyoyoyoyoyyoooooyyoyoyoyyyyyoyoyoyoyoyoyoyoyoyoyoyoyoyoy");
        addNewDiscussion("HiTesting", "STUDENT", "jack", "yoyooyooyoyoyoyyyyyyyyyooyoyooyoyoyoyoyooyoyoyoooyoyoyoyoyyoooooyyoyoyoyyyyyoyoyoyoyoyoyoyoyoyoyoyoyoyoy");
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

    // Method to add an HBox containing a VBox with three texts and one button to the main VBox
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
        Text themeText = new Text("Theme: " + theme);
        Text descriptionText = new Text(description);
        descriptionText.setWrappingWidth(800);
        titleText.setStyle("-fx-text-fill: black; -fx-font-family: \"Segoe UI Black\";-fx-font-size: 30px; ");
        themeText.setStyle("-fx-text-fill: black; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 16px; ");
        descriptionText.setStyle("-fx-text-fill: black; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 16px; ");
        vBox.getChildren().addAll(titleText, themeText, descriptionText);

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
        QuizVBox.getChildren().add(hBox);
    }

    public void filterTheme(String theme) {
        // Get the children of the main VBox
        List<Node> children = new ArrayList<>(QuizVBox.getChildren());

        // Sort the children based on specificTheme
        children.sort((node1, node2) -> {
            String theme1 = getThemeText(node1);
            String theme2 = getThemeText(node2);

            // Compare theme1 and theme2
            if (theme1.equals(theme) && !theme2.equals(theme)) {
                return -1; // specificTheme should be on top
            } else if (!theme1.equals(theme) && theme2.equals(theme)) {
                return 1; // specificTheme should be on top
            } else {
                return 0; // No change in order
            }
        });

        QuizVBox.getChildren().clear();

        // Update the layout to reflect the new order
        QuizVBox.getChildren().setAll(children);
    }

    private String getThemeText(Node node) {
        if (node instanceof HBox) {
            HBox hbox = (HBox) node;
            ObservableList<Node> children = hbox.getChildren();
            if (children.size() >= 1) {
                Node vboxNode = children.get(0);
                if (vboxNode instanceof VBox) {
                    VBox vbox = (VBox) vboxNode;
                    ObservableList<Node> vboxChildren = vbox.getChildren();
                    if (vboxChildren.size() >= 2) {
                        Node themeText = vboxChildren.get(1); // Assuming theme text is at index 1
                        if (themeText instanceof Text) {
                            String fullText = ((Text) themeText).getText();
                            // Remove the "Theme: " prefix
                            if (fullText.startsWith("Theme: ")) {
                                return fullText.substring(7); // Remove "Theme: "
                            }
                        }
                    }
                }
            }
        }
        return "";
    }
    
    private void addNewDiscussion(String title, String role, String username, String content) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setSpacing(3);

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
        if (role.equals("STUDENT")){
            setColour = "-fx-background-color: #ff5757;";
        } else if (role.equals("EDUCATOR")){
            setColour = "-fx-background-color: #ffde59;";
        } else if (role.equals("PARENT")){
            setColour = "-fx-background-color: #4fc8ab;";
        } 
        roleButton.setStyle(setColour+"-fx-text-fill: white; -fx-font-family: \"Segoe UI Black\";-fx-font-size: 12px; -fx-background-radius: 20px;");

        Button usernameButton = new Button(username);
        temp.getChildren().addAll(t,roleButton, usernameButton);
        
        Text contentText = new Text(content);
        contentText.setWrappingWidth(900);
        titleText.setStyle("-fx-text-fill: black; -fx-font-family: \"Segoe UI Black\";-fx-font-size: 30px; ");
        usernameButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #737373; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 16px; -fx-padding: 0; ");
        contentText.setStyle("-fx-text-fill: black; -fx-font-family: \"Segoe UI Semibold\";-fx-font-size: 16px; ");
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
        DiscussionVBox.getChildren().add(hBox);
    }

}
