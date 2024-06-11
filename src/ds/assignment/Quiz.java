/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds.assignment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    private String ID;
    private String title;
    private String description;
    private String theme;
    private String content;
    
    DatabaseConnection dbConnect = new DatabaseConnection();
    UserRepository userRepository = new UserRepository(dbConnect);
    Login login = new Login();  // Create a single instance of Login
    SessionManager sessionManager = new SessionManager(userRepository, login);  // Pass the Login instance to SessionManage

    public Quiz(String title, String description, String theme, String content) {
        this.title = title;
        this.description = description;
        this.theme = theme;
        this.content = content;
    }
    
    public Quiz(String ID, String title, String description, String theme, String content) {
        this.ID = ID;
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
    
    public String getID(){
        return this.ID;
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
            recordToQuizCreatedCSV(educatorUsername, this.title, this.theme, this.description, this.content);
            
            preparedStatement.executeUpdate(); //delete after execute next

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
                String ID = resultSet.getString("id_quiz");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String theme = resultSet.getString("Theme");
                String content = resultSet.getString("Content");
                res.add(new Quiz(ID, title, description, theme, content));
            }
            
            return res;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            connectNow.endDatabase();
        }
    }

    public static void recordToQuizCreatedCSV(String author, String title, String theme, String description, String content) {
        String fileName = "Quiz_Created.csv";
        File file = new File(fileName);
        boolean fileExists = file.exists();
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            // Write the header if the file is newly created
            if (!fileExists) {
                bw.write("Author,Title,Theme,Description,Content");
                bw.newLine();
            }
            // Write the quiz data
            bw.write(escapeCSV(author) + "," + escapeCSV(title) + "," + escapeCSV(theme) + "," + escapeCSV(description) + "," + escapeCSV(content));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            value = "\"" + value + "\"";
        }
        return value;
    }
}
