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
public class NumQuizCreated {

    private int numQuizCreated;

    DatabaseConnection dbConnect = new DatabaseConnection();
    UserRepository userRepository = new UserRepository(dbConnect);

    public NumQuizCreated() {
        numQuizCreated = 0;
    }

    public int getNumQuizCreatedFromDatabase() {
        return userRepository.getNumQuizCreated(SessionManager.currentUser.getUsername());
    }

    public void setNumQuizCreated(int numQuizCreated) {
        this.numQuizCreated = numQuizCreated;
    }

    public void addQuizCreated(int numQuiz) {
        numQuizCreated = getNumQuizCreatedFromDatabase();
        numQuizCreated += numQuiz;
        userRepository.updateNumQuizCreated(currentUser.getUsername(), numQuizCreated);
    }

    public void deductQuizCreated(int numQuiz) {
        numQuizCreated = getNumQuizCreatedFromDatabase();
        numQuizCreated -= numQuiz;
        userRepository.updateNumEventCreated(currentUser.getUsername(), numQuizCreated);
    }
}
