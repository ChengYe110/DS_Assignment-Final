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
    private Button DiscussionPage, EventPage, HomePage, LeaderboardPage, MenuButton, ProfilePage, QuizPage, CreateQuizPage, DoneCreateQuiz, DoneCreateEvent, CreateEventPage;
    @FXML
    private VBox DrawerPane;
    @FXML
    private AnchorPane MenuPane, TopPane, HomePagePane, LeaderboardPane, QuizPane, CreateQuizPane, EventPane, CreateEventPane;
    @FXML
    private Text UsernameText;
    @FXML
    private TextArea QuizTitleField, QuizDescriptionField, QuizContentField, EventTitleField, EventDescriptionField, EventVenueField, EventTimeField;
    @FXML
    private ChoiceBox<String> FilterChoiceBox, QuizThemeChoiceBox;
    @FXML
    private DatePicker EventDatePicker;
    @FXML
    private HBox MENU;
    private Button selectedButton = null;
    private ObservableList<String> theme = FXCollections.observableArrayList("SCIENCE", "TECHNOLOGY", "ENGINEERING", "MATHEMATIC");
    @FXML
    private TableView<?> QuizTable;

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

            ProfilePage.setOnAction(event -> {
                // Handle Profile button click
            });

            DiscussionPage.setOnAction(event -> {
                // Handle Discussion button click
            });

//            QuizPage.setOnAction(event -> {
//                LeaderboardPane.setVisible(false);
//                HomePagePane.setVisible(false);
//                QuizPane.setVisible(true);
//                CreateQuizPane.setVisible(false);
//                EventPane.setVisible(false);
//                CreateEventPane.setVisible(false);
//                QuizPane.toFront();
//            });
//            FilterComboBox.getItems().addAll(theme);
//
//            CreateQuizPage.setOnAction(event -> {
//                LeaderboardPane.setVisible(false);
//                HomePagePane.setVisible(false);
//                QuizPane.setVisible(false);
//                CreateQuizPane.setVisible(true);
//                EventPane.setVisible(false);
//                CreateEventPane.setVisible(false);
//                CreateQuizPane.toFront();
//            });
//            QuizThemeComboBox.getItems().addAll(theme);
//
//            DoneCreateQuiz.setOnAction(event -> {
//                LeaderboardPane.setVisible(false);
//                HomePagePane.setVisible(false);
//                QuizPane.setVisible(true);
//                CreateQuizPane.setVisible(false);
//                EventPane.setVisible(false);
//                CreateEventPane.setVisible(false);
//                QuizPane.toFront();
//            });
//
//            EventPage.setOnAction(event -> {
//                LeaderboardPane.setVisible(false);
//                HomePagePane.setVisible(false);
//                QuizPane.setVisible(false);
//                CreateQuizPane.setVisible(false);
//                EventPane.setVisible(true);
//                CreateEventPane.setVisible(false);
//                EventPane.toFront();
//            });
//
//            CreateEventPage.setOnAction(event -> {
//                LeaderboardPane.setVisible(false);
//                HomePagePane.setVisible(false);
//                QuizPane.setVisible(false);
//                CreateQuizPane.setVisible(false);
//                EventPane.setVisible(false);
//                CreateEventPane.setVisible(true);
//                CreateEventPane.toFront();
//            });
//
//            DoneCreateEvent.setOnAction(event -> {
//                LeaderboardPane.setVisible(false);
//                HomePagePane.setVisible(false);
//                QuizPane.setVisible(false);
//                CreateQuizPane.setVisible(false);
//                EventPane.setVisible(true);
//                CreateEventPane.setVisible(false);
//                EventPane.toFront();
//            });
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
            selectedButton = (Button) ((HBox) MENU.getChildren().get(0)).getChildren().get(0);
            selectedButton.setId("selected");
        });
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

    public void switchEventPage() {
        if (MenuPane.getTranslateX() == 0) {
            slideInTransition.play();
        }
        stackPane.getChildren().clear();
        stackPane.getChildren().add(EventPane);
    }

    public void switchCreateEventPage() {
        if (MenuPane.getTranslateX() == 0) {
            slideInTransition.play();
        }
        stackPane.getChildren().clear();
        stackPane.getChildren().add(CreateEventPane);
    }

    public void switchQuizPage() {
        if (MenuPane.getTranslateX() == 0) {
            slideInTransition.play();
        }
        stackPane.getChildren().clear();
        stackPane.getChildren().add(QuizPane);
    }

    public void switchCreateQuizPage() {
        if (MenuPane.getTranslateX() == 0) {
            slideInTransition.play();
        }
        stackPane.getChildren().clear();
        stackPane.getChildren().add(CreateQuizPane);
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
}
