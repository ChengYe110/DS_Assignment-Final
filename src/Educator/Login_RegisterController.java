package Educator;

import ds.assignment.DatabaseConnection;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ds.assignment.Login;
import ds.assignment.SessionManager;
import ds.assignment.UserRepository;
import gui.StudentController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import ds.assignment.Students;
import javax.swing.JDialog;

public class Login_RegisterController implements Initializable {

    Login userLogin = new Login();
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private AnchorPane REGISTER;

    @FXML
    private ImageView registerPage;

    @FXML
    private Button register;

    @FXML
    private TextField username_register;

    @FXML
    private TextField email_register;

    @FXML
    private PasswordField passwordHidden_register;

    @FXML
    private PasswordField confirmpasswordHidden_register;

    @FXML
    private TextField passwordText_register;

    @FXML
    private TextField confirmpasswordText_register;

    @FXML
    private CheckBox student;

    @FXML
    private CheckBox educator;

    @FXML
    private CheckBox parent;

    @FXML
    private CheckBox showPassword_register;

    @FXML
    private Button logInNow;

    @FXML
    private AnchorPane LOGIN;

    @FXML
    private ImageView loginPage;

    @FXML
    private TextField username_email_login;

    @FXML
    private PasswordField passwordHidden_login;

    @FXML
    private TextField passwordText_login;

    @FXML
    private CheckBox showPassword_login;

    @FXML
    private Button login;

    @FXML
    private Button registerNow;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Bidirectional text binding between password field and text field
        passwordHidden_register.textProperty().bindBidirectional(passwordText_register.textProperty());
        passwordHidden_login.textProperty().bindBidirectional(passwordText_login.textProperty());
        confirmpasswordHidden_register.textProperty().bindBidirectional(confirmpasswordText_register.textProperty());

        // Toggles the visibility of the password fields in Register Page based on the state of a checkbox.
        showPassword_register.selectedProperty().addListener((observable, oldValue, newValue) -> {
            passwordHidden_register.setVisible(!newValue);
            passwordText_register.setVisible(newValue);
            confirmpasswordHidden_register.setVisible(!newValue);
            confirmpasswordText_register.setVisible(newValue);
        });

        // Toggles the visibility of the password fields in Login Page based on the state of a checkbox.
        showPassword_login.selectedProperty().addListener((observable, oldValue, newValue) -> {
            passwordHidden_login.setVisible(!newValue);
            passwordText_login.setVisible(newValue);
        });

        // Change to LogIn Page
        logInNow.setOnAction(event -> {
            REGISTER.setVisible(false);
            LOGIN.setVisible(true);
            username_email_login.clear();
            passwordHidden_login.clear();
            passwordText_login.clear();
            showPassword_login.setSelected(false);
        });

        // Change to Register Page
        registerNow.setOnAction(event -> {
            REGISTER.setVisible(true);
            LOGIN.setVisible(false);
            username_register.clear();
            email_register.clear();
            passwordHidden_register.clear();
            passwordText_register.clear();
            confirmpasswordHidden_register.clear();
            confirmpasswordText_register.clear();
            student.setSelected(false);
            educator.setSelected(false);
            parent.setSelected(false);
            showPassword_register.setSelected(false);
        });

        // Set only one checkbox can be selected at any time
        student.setOnAction(event -> {
            if (student.isSelected()) {
                educator.setSelected(false);
                parent.setSelected(false);
            }
        });

        educator.setOnAction(event -> {
            if (educator.isSelected()) {
                student.setSelected(false);
                parent.setSelected(false);
            }
        });

        parent.setOnAction(event -> {
            if (parent.isSelected()) {
                student.setSelected(false);
                educator.setSelected(false);
            }
        });

        login.setOnAction(event -> {
            DatabaseConnection dbConnect = new DatabaseConnection();
            UserRepository userRepository = new UserRepository(dbConnect);
            Login login2 = new Login();  // Create a single instance of Login
            SessionManager sessionManager = new SessionManager(userRepository, login2);

            // Get the entered email and password
            String enteredEmailUsername = username_email_login.getText();
            String enteredPassword = passwordHidden_login.getText();

            if (enteredEmailUsername.isEmpty() || enteredPassword.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter both email and password.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return; // Stop further processing
            }

            // Authenticate the user
            if (userLogin.ExistingUser(enteredEmailUsername)) {
                System.out.println("exist");
                boolean isAuthenticated = userLogin.authenticateUser(enteredEmailUsername, enteredPassword);
                if (isAuthenticated) {
                    // Open the new window or perform other actions
                    sessionManager.login(enteredEmailUsername, enteredPassword);
//                    Timestamp lastCheckinTimestamp = sessionManager.getLastTimestampFromDatabase();
//                    DailyUpdateDayCount.updateDayCount();
//                    System.out.println(lastCheckinTimestamp);
//                    multiplePanel m1 = new multiplePanel();
//                    m1.setVisible(true);
//                    System.out.println(lastCheckinTimestamp);
//                    SwingUtilities.getWindowAncestor((Component) ae.getSource()).dispose();
                    // Create a dialog for JOptionPane to ensure it is on top of m1
                    JDialog dialog = new JDialog();
                    dialog.setAlwaysOnTop(true);

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Student.fxml"));
                        root = loader.load();

                        StudentController HPController = loader.getController();
                        
                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle("Main Page");
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    
                  
                    }


//                    if (lastCheckinTimestamp == null || !isSameDay(new Timestamp(System.currentTimeMillis()), lastCheckinTimestamp)) {
//                        sessionManager.saveTimestampToDatabase();
//                        pointsFromDataBase.getPointsFromDatabase();
//                        pointsFromDataBase.addPoints(1);
//                        m1.refreshPoints();
//                        sessionManager.resetAttemptsForAllUsers();
//                        JOptionPane.showMessageDialog(dialog, "You've signed in successfully. Congratulations, you've earned 1 point.", "Success", JOptionPane.INFORMATION_MESSAGE);
//                    } else {
//                        sessionManager.saveTimestampToDatabase();
//                        pointsFromDataBase.getPointsFromDatabase();
//                        JOptionPane.showMessageDialog(dialog, "You've signed in successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
//                    }
                } else {
                    // Show error message for authentication failure
                    System.out.println("Invalid email or password. Please try again.");
                    JOptionPane.showMessageDialog(null, "Invalid email or password. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("efaf");
                JOptionPane.showMessageDialog(null, "User not exist. Please register first.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }

        }
        );

        register.setOnAction(event -> {

            // Get user input
            String username = username_register.getText();
            String email = email_register.getText();
            String password = passwordHidden_register.getText();

            DatabaseConnection dbConnect = new DatabaseConnection();
            UserRepository userRepository = new UserRepository(dbConnect);
            Login loginRegister = new Login();
            SessionManager sessionManager = new SessionManager(userRepository, loginRegister);  // Pass the Login instance to SessionManager
            loginRegister.setSessionManager(sessionManager);

            // Input validation
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return; // Stop further processing
            }

            // Validate email using a regex
            String emailRegex = "^(.+)@(gmail\\.com|hotmail\\.com|yahoo\\.com|siswa\\.um\\.edu\\.my)$";
            if (!email.matches(emailRegex)) {
                JOptionPane.showMessageDialog(null, "Invalid email format. Please use a valid email address.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return; // Stop further processing
            }

            try {

                if ((userRepository.isUsernameTaken(username))) {
                    JOptionPane.showMessageDialog(null, "Username has been taken", "Error", JOptionPane.ERROR_MESSAGE);
                } // Provide user feedback for successful insertion
                else if ((userRepository.isEmailTaken(email))) {
                    JOptionPane.showMessageDialog(null, "Email existed. Please use another email", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "User created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                    String role = (student.isSelected()) ? "student"
                            : (parent.isSelected()) ? "parent"
                            : "educator";

                    // Insert the user's data into the database
                    if (student.isSelected()) {
                        //Student.insertIntoDatabase();
                        Students newStud = new Students(email, username, password, role);
                        newStud.insertIntoDatabase();
                    } else if (parent.isSelected()) {
                        System.out.println("save parent");
                    } else {
                        // Create a Educator object with the user's input
                        Educator newEdu = new Educator(email, username, password, role);
                        newEdu.insertIntoDatabase();
                    }
                }
            } catch (Exception e) {
                // Handle specific exceptions (e.g., SQLException) and provide user feedback
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error creating user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }
}
