/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author enjye
 */
public class LeaderBoardController implements Initializable {

    @FXML
    private Text UsernameText,Winner1,Winner10,Winner10pts,Winner1pts,Winner2,Winner2pts,Winner3,Winner3pts,Winner4,Winner4pts,Winner5,Winner5pts,Winner6,Winner6pts,Winner7,Winner7pts,Winner8,Winner8pts,Winner9,Winner9pts;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        StudentHomePageController homePageController = new StudentHomePageController();
//        // Execute after the layout pass is complete
//        Platform.runLater(() -> {
//            homePageController.slideMenuPane();
//
//            // Set up the action for each button
//            homePageController.getHomePage().setOnAction(event -> {
//                homePageController.RemoveLineEffect(homePageController.getLineLeaderboardPage());
//                homePageController.AddLineEffect(homePageController.getLineHomePage());
//            });
//        });
    }    
    
}
