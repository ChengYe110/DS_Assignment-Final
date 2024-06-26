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
 * @author enjye
 */
public class Discussion {

    private String ID;
    private String title;
    private String content;
    private String author;
    private String authorID;
    private int like;
    private String datetime;
    
    DatabaseConnection dbConnect = new DatabaseConnection();
    UserRepository userRepository = new UserRepository(dbConnect);
    
    public Discussion(String title, String content, String author, int like, String datetime) {
        this.title = title;
        this.content = content;
        this.authorID = author;
        this.like = like;
        this.datetime = datetime;
    }

    public Discussion(String ID, String title, String content, String author, int like, String datetime) {
        this.ID = ID;
        this.title = title;
        this.content = content;
        this.authorID = author;
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
        this.author=userRepository.getUsernameByID(this.authorID);
        return this.author;
    }

    public int getLike() {
        return like;
    }

    public String getId() {
        return ID;
    }
    
    public String getDatetime(){
        return datetime;
    }
    
    public String getAuthorID(){
        return authorID;
    }
    

    public static Discussion getDiscussionFromDatabase(String DiscussionId) {
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            UserRepository userRepository = new UserRepository(connectNow);

            String query = "SELECT * FROM discussionpost WHERE id_discussionpost = ?";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, DiscussionId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("Title");
                String content = resultSet.getString("Content");
                String authorID = resultSet.getString("Author");
                int like = resultSet.getInt("LikeCount");
                String datetime = resultSet.getString("DatePublished");
                return new Discussion(title, content, authorID, like, datetime);
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
            preparedStatement.setString(3, this.authorID);
            preparedStatement.setInt(4, this.like);
            preparedStatement.setString(5, this.datetime);

            preparedStatement.executeUpdate(); //delete after execute next
            recordToDiscussionPostCSV(Username,userRepository.getRole(Username),this.title,this.content,this.datetime);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectNow.endDatabase();
        }
    }

    public void saveLike(String Username, String role) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.linkDatabase();

        try {

            String query = "UPDATE discussionpost SET LikeCount = LikeCount + 1 WHERE id_discussionpost = ?";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, this.ID);

            if (preparedStatement.executeUpdate() != 0) {
                String query2 = "UPDATE " + role + " SET LikedPost=CONCAT(IFNULL(LikedPost,''), '" + this.ID + ",') WHERE Username=?";

                PreparedStatement preparedStatement2 = connectDB.prepareStatement(query2);
                preparedStatement2.setString(1, Username);
                preparedStatement2.executeUpdate();
                
                this.like++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectNow.endDatabase();
        }
    }

    public void removeLike(String username, String role) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.linkDatabase();

        PreparedStatement selectDiscussionStmt = null;
        PreparedStatement selectRoleStmt = null;
        PreparedStatement updateLikeCountStmt = null;
        PreparedStatement updateLikedPostStmt = null;
        ResultSet discussionResultSet = null;
        ResultSet roleResultSet = null;

        try {
            // Step 1: Select from discussionpost table
            String selectDiscussionQuery = "SELECT id_discussionpost, LikeCount FROM discussionpost WHERE id_discussionpost = ?";
            selectDiscussionStmt = connectDB.prepareStatement(selectDiscussionQuery);
            selectDiscussionStmt.setString(1, this.ID);  // Assuming this.ID is the ID of the discussion post
            discussionResultSet = selectDiscussionStmt.executeQuery();

            if (discussionResultSet.next()) {
                int currentLikeCount = discussionResultSet.getInt("LikeCount");
            }

            // Step 2: Select from the role-specific table
            String selectRoleQuery = "SELECT LikedPost FROM " + role + " WHERE Username = ?";
            selectRoleStmt = connectDB.prepareStatement(selectRoleQuery);
            selectRoleStmt.setString(1, username);
            roleResultSet = selectRoleStmt.executeQuery();

            String currentLike = "";
            if (roleResultSet.next()) {
                currentLike = roleResultSet.getString("LikedPost");
            }

            // Step 3: Update the LikeCount
            String updateLikeCountQuery = "UPDATE discussionpost SET LikeCount = LikeCount - 1 WHERE id_discussionpost = ?";
            updateLikeCountStmt = connectDB.prepareStatement(updateLikeCountQuery);
            updateLikeCountStmt.setString(1, this.ID);
            updateLikeCountStmt.executeUpdate();
            
            this.like--;

            // Step 4: Remove liked post from LikedPost
            String updatedLike = removeLikedFromDatabase(currentLike, this.ID);

            // Step 5: Update the LikedPost for the user
            String updateLikedPostQuery = "UPDATE " + role + " SET LikedPost = ? WHERE Username = ?";
            updateLikedPostStmt = connectDB.prepareStatement(updateLikedPostQuery);
            updateLikedPostStmt.setString(1, updatedLike);
            updateLikedPostStmt.setString(2, username);
            updateLikedPostStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (discussionResultSet != null) {
                    discussionResultSet.close();
                }
                if (roleResultSet != null) {
                    roleResultSet.close();
                }
                if (selectDiscussionStmt != null) {
                    selectDiscussionStmt.close();
                }
                if (selectRoleStmt != null) {
                    selectRoleStmt.close();
                }
                if (updateLikeCountStmt != null) {
                    updateLikeCountStmt.close();
                }
                if (updateLikedPostStmt != null) {
                    updateLikedPostStmt.close();
                }
                connectNow.endDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String removeLikedFromDatabase(String currentLike, String id) {
        if (currentLike == null || currentLike.isEmpty()) {
            return currentLike;
        }
        // Assuming that likes are stored in a comma-separated list
        String[] likesArray = currentLike.split(",");
        StringBuilder updatedLikes = new StringBuilder();

        for (String like : likesArray) {
            if (!like.equals(id)) {
                if (updatedLikes.length() > 0) {
                    updatedLikes.append(",");
                }
                updatedLikes.append(like.trim());
            }
        }
        // If the updatedLikes is empty, return an empty string
        if (updatedLikes.length() == 0) {
            return "";
        }

        // Otherwise, append a comma at the end if there are one or more elements
        updatedLikes.append(",");

        return updatedLikes.toString();
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
                String id = resultSet.getString("id_discussionpost");
                String title = resultSet.getString("Title");
                String content = resultSet.getString("Content");
                String author = resultSet.getString("Author");
                int like = resultSet.getInt("LikeCount");
                String datetime = resultSet.getString("DatePublished");
                
                res.add(new Discussion(id, title, content, author, like, datetime));
            }

            return res;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            connectNow.endDatabase();
        }
    }

    public List<String> checkLiked(String username, String role) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.linkDatabase();
        List<String> LikedList = new ArrayList<>();
        try {
            String selectRoleQuery = "SELECT LikedPost FROM " + role + " WHERE Username = ?";
            PreparedStatement preparedStatement = connectDB.prepareStatement(selectRoleQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            String currentLike = "";
            if (resultSet.next()) {
                currentLike = resultSet.getString("LikedPost");
                if (currentLike == null) {
                    return LikedList;
                }
                String[] array = currentLike.split(",");
                for (String likes : array) {
                    String trimmedLike = likes.trim();
                    if (!trimmedLike.isEmpty()) {
                        LikedList.add(trimmedLike);
                    }
                }
                return LikedList;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return LikedList;
    }

    public void addLike(String username, String role) {
        saveLike(username, role);
    }

    public void deductLike(String username, String role) {
        removeLike(username, role);
    }
    
    public static void recordToDiscussionPostCSV(String author, String role, String title, String content, String datePublished) {
        String fileName = "Discussion_Posts.csv";
        File file = new File(fileName);
        boolean fileExists = file.exists();
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            // Write the header if the file is newly created
            if (!fileExists) {
                bw.write("Author,Role,Title,Content,DatePublished");
                bw.newLine();
            }
            // Write the discussion post data
            bw.write(escapeCSV(author) + "," + escapeCSV(role) + "," + escapeCSV(title) + "," + escapeCSV(content) + "," + escapeCSV(datePublished));
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
