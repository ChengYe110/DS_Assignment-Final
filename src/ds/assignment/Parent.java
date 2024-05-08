/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds.assignment;

/**
 *
 * @author Tan Zhi Wei
 */
import ds.assignment.BookingDestination;
import ds.assignment.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLTimeoutException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import ds.assignment.Parent;


public class Parent extends User implements BookingDestination{
    //children elements?
    //linked list?(priority age-from oldest to youngest)
    
    
    public Parent(String email, String username, String password, String role){
        super(email, username, password, role);
        //the children
    }

    @Override
    public void readFile(String filename) {
        try{
            BufferedReader rd = new BufferedReader(new FileReader(filename));
            String fileinput = null;
            String read = null;
            while((read = rd.readLine())!=null){
                fileinput += rd.readLine();
                fileinput += (rd.readLine() == null)? "": ":";//if the file do not have information already then do not add things, if still have things, add :
            }
            
            String[][] destInformation = new String[1][2];
            
        }catch(FileNotFoundException e){
            System.out.println("Problem with file not found");
            
        }catch(IOException e){
            System.out.println("Problem with file input and output");
        }
    }

    @Override
    public double distance() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean checkCollision() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String bookingDestination(String chooseTour) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String viewEvent() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String displayBookingMade() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

        
}
