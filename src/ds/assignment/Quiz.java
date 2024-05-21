/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds.assignment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class Quiz {

    private String title;
    private String description;
    private String theme;
    private String content;

    public Quiz(String title, String description, String theme, String content) {
        this.title = title;
        this.description = description;
        this.theme = theme;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTheme() {
        return theme;
    }

    public String getContent() {
        return content;
    }

    public static Quiz getQuizFromDatabase(String quizId) {
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            String query = "SELECT * FROM quiz WHERE quiz_id = ?";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, quizId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String theme = resultSet.getString("Theme");
                String content = resultSet.getString("Content");
                return new Quiz(title, description, theme, content);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public void saveQuiz(String educatorUsername) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.linkDatabase();

        try {

            String query = "INSERT INTO quiz (Title, Description, Theme, Content) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, this.title);
            preparedStatement.setString(2, this.description);
            preparedStatement.setString(3, this.theme);
            preparedStatement.setString(4, this.content);
            
            preparedStatement.executeUpdate(); //delete after execute next
            System.out.println("haha");

//            if (preparedStatement.executeUpdate() == 0) {
//                String query2 = "UPDATE educator SET NumQuiz=NumQuiz+1 WHERE Username=?";
//                PreparedStatement preparedStatement2 = connectDB.prepareStatement(query2);
//                preparedStatement2.setString(1, educatorUsername);
//                preparedStatement2.executeUpdate();
//            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectNow.endDatabase();
        }
    }
    
    public static List<Quiz> getQuizList(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.linkDatabase();
        
        List<Quiz> res = new ArrayList<>();

        try {

            String query = "SELECT * FROM quiz";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String theme = resultSet.getString("Theme");
                String content = resultSet.getString("Content");
                res.add(new Quiz(title, description, theme, content));
            }
            
            return res;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            connectNow.endDatabase();
        }
    }

}
