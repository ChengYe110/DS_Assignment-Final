package Educator;

public class Educator extends User {

    private int numQuiz, numEvent;

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
    
}
