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
 * @author enjye
 */
public class Discussion {
    private String title;
    private String content;
    private String author;
    private int like;
    private String datetime;

    public Discussion(String title, String content, String author, int like, String datetime) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.like = like;
        this.datetime = datetime;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }
    
    public int getLike() {
        return like;
    }
    
    public String getDatetime(){
        return datetime;
    }

    public static Discussion getDiscussionFromDatabase(String DiscussionId) {
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            String query = "SELECT * FROM discussionpost WHERE id_discussionpost = ?";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, DiscussionId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String content = resultSet.getString("Content");
                String author = resultSet.getString("Author");
                int like = resultSet.getInt("LikeCount");
                String datetime = resultSet.getString("DatePublished");
                return new Discussion(title, content, author, like, datetime);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void saveDiscussion(String Username) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.linkDatabase();

        try {

            String query = "INSERT INTO discussionpost (Title, Content, Author, LikeCount, DatePublished) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, this.title);
            preparedStatement.setString(2, this.content);
            preparedStatement.setString(3, this.author);
            preparedStatement.setInt(4, this.like);
            preparedStatement.setString(5,this.datetime);

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

    public static List<Discussion> getDiscussionList() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.linkDatabase();

        List<Discussion> res = new ArrayList<>();

        try {

            String query = "SELECT * FROM discussionpost";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                String content = resultSet.getString("Content");
                String author = resultSet.getString("Author");
                int like = resultSet.getInt("LikeCount");
                String datetime = resultSet.getString("DatePublished");
                res.add(new Discussion(title, content, author, like, datetime));
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
