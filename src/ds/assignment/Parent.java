/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds.assignment;

import ds.assignment.BookingDestination;
import ds.assignment.User;
import ds.assignment.Parent;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parent extends User implements BookingDestination{
    private String children;
    Map<LocalDate,String> bookingDate = new HashMap<>();//for date
    List<String> destination = new ArrayList<>();//for calculate distance
    List<double[]> coordinates = new ArrayList<>();//for calculate distance
    //linked list?(priority age-from oldest to youngest)
    
    
    public Parent(String email, String username, String password, String role){
        super(email, username, password, role);
        //the children
    }

    @Override
    public void readFile() {
        String line;        
        
        try{
            BufferedReader br = new BufferedReader(new FileReader("BookingDestination.txt"));     
            while((line = br.readLine()) != null){
                String[] parts = line.trim().split(",");
                if(parts.length == 2){
                    double XValue = Double.parseDouble(parts[0].trim());
                    double YValue = Double.parseDouble(parts[1].trim());
                    coordinates.add(new double[] {XValue,YValue});              
                }else if(parts.length == 1){
                    destination.add(parts[0].trim());
                }else{
                    System.out.println("Problem");
                }
            }
            br.close();
        
        }catch(FileNotFoundException e){
            System.out.println("Problem with file not found");
            
        }catch(IOException e){
            System.out.println("Problem with file input and output");
        }
    }

    @Override
    public double calcDistance() {
        /*inside the column type the destination name
        invoke readfile method, then take the matched destination name and calculate distance
        or maybe this method no need and direct calculate distance at the readfile method*/
//        return euclideanDiatance(this.coordinateX, this.coordinateY);
        readFile();
        return 0;
    }

    @Override
    public boolean checkCollision() {
        //return true when have collision
        int year = LocalDate.now().getYear();
        
        LocalDate date = LocalDate.of(year, 1, 1);
        date = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        while(date.getYear() == year){
            if(date.getDayOfWeek() == DayOfWeek.MONDAY){
                bookingDate.put(date, "Petrosains Science Discovery Centre");
            }else if(date.getDayOfWeek() == DayOfWeek.TUESDAY){
                bookingDate.put(date, "Tech Dome Penang");
            }else if(date.getDayOfWeek() == DayOfWeek.WEDNESDAY){
                bookingDate.put(date, "Agro Technology Park in MARDI");
            }else if(date.getDayOfWeek() == DayOfWeek.THURSDAY){
                bookingDate.put(date, "National Science Centre");
            }else if(date.getDayOfWeek() == DayOfWeek.FRIDAY){
                bookingDate.put(date, "Marine Aquarium and Museum");;
            }else if(date.getDayOfWeek() == DayOfWeek.SATURDAY){
                bookingDate.put(date, "Biomedical Museum");
                bookingDate.put(date, "Telegraph Museum");
            }else if(date.getDayOfWeek() == DayOfWeek.SUNDAY){
                bookingDate.put(date, "TPenang Science Cluster");
                bookingDate.put(date, "Pusat Sains & Kreativiti Terengganu");
            }else{
                System.out.println("No such day");
            }          
            date = date.plusDays(1);
        }
        
        try{
            //get the event date from database, then compare with the date of bookingTour, if equal(return false)
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connectDB = dbConnection.linkDatabase();
            String connectQuery = "SELECT Date FROM event";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            while(queryOutput.next()){
                LocalDate eventDate = LocalDate.parse(queryOutput.getString("Date"));
                if(bookingDate.containsKey(eventDate)){
                    return true;
                }
            }
            
            connectDB.close();
            statement.close();
            queryOutput.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String bookingDestination(String chooseTour) {
        readFile();
        if(checkCollision()){
            return "Booking unsuccessful because clash with other event";
        }else{
            try{
                DatabaseConnection dbConnection = new DatabaseConnection(); 
                Connection connectDB = dbConnection.linkDatabase();
                String connectQuery = "SELECT Booking FROM parents";
                Statement statement = connectDB.createStatement();
                ResultSet queryOutput = statement.executeQuery(connectQuery);
                
                if(queryOutput.next()){
                    String booking = queryOutput.getString("Booking");
                if(booking.isEmpty()){
                    booking = "";
                }
                String newChild = booking + chooseTour + ",";
                PreparedStatement stmt = connectDB.prepareStatement("INSERT INTO PastBooking(chooseTour) FROM parent");
                PreparedStatement stmt2 = connectDB.prepareStatement("INSERT INTO RegisteredBooking(chooseTour) FROM student");
            }
            }catch(Exception e){
                e.printStackTrace();
            }
        } 
        return null;
    }

    @Override
    public String viewEvent() {
        try{
            //check the upcoming 3 event
            DatabaseConnection dbConnection = new DatabaseConnection(); 
            Connection connectDB = dbConnection.linkDatabase();
            String connectQuery = "SELECT Booking FROM oarents";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
        
            //add things
            
            connectDB.close();
            statement.close();
            queryOutput.close();
        
        
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void displayBooking() {
        try{
            DatabaseConnection dbConnection = new DatabaseConnection(); 
            Connection connectDB = dbConnection.linkDatabase();
            String connectQuery = "SELECT Booking FROM oarents";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
        
            //add things
            if(queryOutput.next()){
                String booking = queryOutput.getString("Booking");
                if(booking.isEmpty()){
                    booking = "";
                }
                String newChild = booking + ",";
                }
            
            
            connectDB.close();
            statement.close();
            queryOutput.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    

    public void displayInfo(){
        try{
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connectDB = dbConnection.linkDatabase();
            String connectQuery = "SELECT Username, Email, Role, Location, Booking FROM parent";;
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            if(queryOutput.next()){
                String username = queryOutput.getString("Username");
                String email = queryOutput.getString("Email");
                String role = queryOutput.getString("role");
                String location = queryOutput.getString("location");
                String booking = queryOutput.getString("Booking");
            }
            
            connectDB.close();
            statement.close();
            queryOutput.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
   
    public void displayChildren(){
        try{
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connectDB = dbConnection.linkDatabase();
            String connectQuery = "SELECT Children FROM parent";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            //add things
            if(queryOutput.next()){
                String children = queryOutput.getString("Children");
            }
            
            connectDB.close();
            statement.close();
            queryOutput.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public static double euclideanDistance(double x1, double y1, double x2, double y2){
        //to calculate distance
        return Math.sqrt((Math.pow((x2-x1), 2)) + Math.pow((y2-y1), 2));
    }

    public static void updateDate(){
        //method to get current date and plus (1-7) to get the next week's date and assign as the booking touur date
        LocalDate current = LocalDate.now();
        LocalDate eventDate = current.plus(7,ChronoUnit.DAYS);
    }
    
    public void addChildren(String children){
        //method to add children
        try{
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connectDB = dbConnection.linkDatabase();
            String connectQuery = "SELECT Children FROM parents";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            //add things
            if(queryOutput.next()){
                String currentChild = queryOutput.getString("Children");
                if(currentChild.isEmpty()){
                    currentChild = "";
                }
                String newChild = currentChild + children + ",";
                
                String updateQuery = "UPDATE Children SET Parents = '" + newChild + "' WHERE Username = '" + username + "'";
                statement.executeUpdate(updateQuery);
            }
            
            connectDB.close();
            statement.close();
            queryOutput.close();;
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}