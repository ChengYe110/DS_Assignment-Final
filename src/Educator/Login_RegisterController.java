package Educator;

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

public class Login_RegisterController implements Initializable {

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
            
        });
    }
}
