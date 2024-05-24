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

    public String bookingDestination(String chooseTour);//to book the tour, need readfile(d), checkCollision(d), calculate distance
    
    public void readFile();//read the coordinates inside the txt file //done
    
    public double calcDistance();//calculate the distance between all coordinates
    
    public boolean checkCollision();//booking date inside the parentheses //done
    
    public String viewEvent();///method to how the upcoming event
    
    public void displayPastBooking();//display the booking made //done
    
}
