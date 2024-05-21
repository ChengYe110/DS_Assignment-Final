package Educator;
import ds.assignment.DatabaseConnection;
import ds.assignment.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Educator extends User {

    private int numQuiz, numEvent;
    DatabaseConnection dbConnect = new DatabaseConnection();

    public Educator(String email, String username, String password, String role) {
        super(email, username, password, role);
        numQuiz = 0;
        numEvent = 0;
    }
    
    public int displayNumEvent(){
        return this.numEvent;
    }
    
    public int displayNumQuiz(){
        return this.numQuiz;
    }
    
    public void insertIntoDatabase() {
        if (!InfoCheck()){
        Connection connection = null;
        try {
            // Database connection details
            connection=dbConnect.linkDatabase();
            
            String insertQuery = "INSERT INTO educator (Username, Email, Password, Role, NumEvent, NumQuiz, Location) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, hashPassword(password));
            preparedStatement.setString(4, role);
            preparedStatement.setInt(5, numEvent);
            preparedStatement.setInt(6, numQuiz);
            preparedStatement.setString(7, location);
            
            preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnect.endDatabase();
        }
    }   else
                System.out.println("Username or password already exists. Please choose a different username or password.");
            
    }
    
}
