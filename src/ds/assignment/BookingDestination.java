/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds.assignment;

/**
 *
 * @author Tan Zhi Wei
 */

public interface BookingDestination {

    public String bookingDestination(String chooseTour);//to book the tour, need readfile, checkCollision, calculate distance
    
    public void readFile();//read the coordinates inside the txt file
    
    public double calcDistance();//calculate the distance between all coordinates
    
    public boolean checkCollision(String date);//booking date inside the parentheses
    
    public String viewEvent();///method to how the upcoming event
    
    public void displayBooking();//display the booking made
    
}
