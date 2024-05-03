package ds.assignment;

import ds.assignment.DatabaseConnection;
import static ds.assignment.SessionManager.currentUser;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author enjye
 */
public class Points {

    private int points;

    DatabaseConnection dbConnect = new DatabaseConnection();
    UserRepository userRepository = new UserRepository(dbConnect);
    
    
//    public Points() {
//        points = currentUser.getCurrentPoints(currentUser.getUsername());
//    }
    
    public Points() {
        points = 0;
    }

    public int getPointsFromDatabase() {
        return currentUser.getCurrentPoints(currentUser.getUsername());
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int anotherPoints) {
        points = getPointsFromDatabase();
        points += anotherPoints;
        userRepository.updatePoints(currentUser.getUsername(), points);
    }

    public void deductPoints(int anotherPoints) {
        points = getPointsFromDatabase();
        points -= anotherPoints;
        userRepository.updatePoints(currentUser.getUsername(), points);
    }
}
