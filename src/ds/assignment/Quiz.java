/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds.assignment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

}
