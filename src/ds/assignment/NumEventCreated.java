/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds.assignment;

import static ds.assignment.SessionManager.currentUser;

/**
 *
 * @author User
 */
public class NumEventCreated {

    private int numEventCreated;

    DatabaseConnection dbConnect = new DatabaseConnection();
    UserRepository userRepository = new UserRepository(dbConnect);

    public NumEventCreated() {
        numEventCreated = 0;
    }

    public int getNumEventCreatedFromDatabase() {
        return userRepository.getNumEventCreated(SessionManager.currentUser.getUsername());
    }

    public void setNumEventCreated(int numEventCreated) {
        this.numEventCreated = numEventCreated;
    }

    public void addEventCreated(int numEvent) {
        numEventCreated = getNumEventCreatedFromDatabase();
        numEventCreated += numEvent;
        userRepository.updateNumEventCreated(currentUser.getUsername(), numEventCreated);
    }

    public void deductEventCreated(int numEvent) {
        numEventCreated = getNumEventCreatedFromDatabase();
        numEventCreated -= numEvent;
        userRepository.updateNumEventCreated(currentUser.getUsername(), numEventCreated);
    }
}
