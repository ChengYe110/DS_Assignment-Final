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
import java.sql.SQLException;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.PrintWriter;
import java.io.FileOutputStream;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parent extends User implements BookingDestination{
    private String children;
    HashMap<LocalDate,String> bookingDate = new HashMap<>();//for date
    List<Destination> destination = new ArrayList<>();//for calculate distance
    //linked list?(priority age-from oldest to youngest)
    ArrayList<String> childrenList = new ArrayList<>();
    
    public Parent(String email, String username, String password, String role){
        super(email, username, password, role);
        
        //to assign date to the bookingDestination
        int year = LocalDate.now().getYear();
        LocalDate date = LocalDate.of(year, 1, 1);
        
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
                bookingDate.put(date, "Pusat Sains & Kreativiti Terengganu");
                bookingDate.put(date, "Biomedical Museum");
            }else if(date.getDayOfWeek() == DayOfWeek.SUNDAY){
                bookingDate.put(date, "Telegraph Museum");
                bookingDate.put(date, "Penang Science Cluster");            
            }else{
                System.out.println("No such day");
            } 
            date = date.plusDays(1);
        }
        //the children?
        
    }

    @Override
    public void readFile() {
        String line;        
        
        try{
            BufferedReader br = new BufferedReader(new FileReader("BookingDestination.txt"));     
            while((line = br.readLine()) != null){
                String locationName = line.trim();
                if((line = br.readLine()) != null){
                String[] parts = line.trim().split(",");
                    if(parts.length == 2){
                        double XValue = Double.parseDouble(parts[0].trim());
                        double YValue = Double.parseDouble(parts[1].trim());
                        destination.add(new Destination(locationName,XValue,YValue));
                    }else{
                        System.out.println("problem inside");
                    }
                }else{
                    System.out.println("problem outside");
                }
                
            }
            System.out.println(destination.toString());
            br.close();
        
        }catch(FileNotFoundException e){
            System.out.println("Problem with file not found");
            
        }catch(IOException e){
            System.out.println("Problem with file input and output");
        }
    }

    @Override
    public double calcDistance(String destinationName) {
        /*inside the column type the destination name
        invoke readfile method, then take the matched destination name and calculate distance
        or maybe this method no need and direct calculate distance at the readfile method*/

        readFile();
        Connection connectDB = null;
        try{
            DatabaseConnection dbConnection = new DatabaseConnection(); 
            connectDB = dbConnection.linkDatabase();
            String connectQuery = "SELECT Location FROM parent";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            if(queryOutput.next()){
                String location = queryOutput.getString("Location");
                String[] locationToCalc = location.split(",");
                double parentX = Double.parseDouble(locationToCalc[0]);
                double parentY = Double.parseDouble(locationToCalc[1]);
                double distance = 0;
                for (Destination dest : destination) {
                    if(destinationName.equals(dest.getDestinationName())){
                        distance = euclideanDistance(parentX, parentY, dest.getX(), dest.getY());
                    }
                }
                return distance;
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return -1;
    }

    @Override
    public boolean checkCollision() {
        //return true when have collision

        try{
            //get the event date from database and compare with the date of bookingTour, if equal then return true
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connectDB = dbConnection.linkDatabase();
            
            String connectQuery = "SELECT Date FROM event";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            while(queryOutput.next()){
                LocalDate eventDate = LocalDate.parse(queryOutput.getString("Date"));
                if(bookingDate.containsKey(eventDate)){
                    connectDB.close();
                    statement.close();
                    queryOutput.close();
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

//    public void displayNextSevenBookingDate(){
//        for (int i = 0; i < 10; i++) {
//            System.out.println("\n" + bookingDate.);
//        }
//    }
    
    
    @Override
    public String bookingDestination(String chooseTour) {
        //check whether all the children have event at that day or not //done but need modify
        //if success  - database connection
        //insert that particular time into the past booking column
        readFile();
        if(checkCollision() || checkEventAllChildren()){
            return "Booking unsuccessful because clash with other event"; // better throw exception(?
        }else{
            try{
                DatabaseConnection dbConnection = new DatabaseConnection(); 
                Connection connectDB = dbConnection.linkDatabase();
                
                //Fetch data from parent
                String connectQuery = "SELECT PastBooking, id_parent FROM parent";
                Statement statement = connectDB.createStatement();
                ResultSet queryOutput = statement.executeQuery(connectQuery);
                
                //Fetch data from student
                String connectQuery1 = "SELECT id_student, RegisteredBooking FROM student";
                Statement statement1 = connectDB.createStatement();
                ResultSet queryOutput1 = statement1.executeQuery(connectQuery1);
                
                while(queryOutput.next()){
                    String booking = queryOutput.getString("Booking");
                    String id_parent = queryOutput.getString("id_parent");
                    String updateParentBookingQuery = "UPDATE Parent SET PastBooking = ? WHERE id_parent = ?";
                    PreparedStatement updateParentBooking = connectDB.prepareStatement(updateParentBookingQuery);
                    updateParentBooking.setString(1, chooseTour + ",");
                    updateParentBooking.setString(2, id_parent);
                    updateParentBooking.executeUpdate();
                }
                
                while(queryOutput1.next()){
                    String id_student = queryOutput1.getString("id_student");
                    String updateChildrenBookingQuery = "UPDATE Children SET RegisteredBooking(chooseTour)";
                    PreparedStatement updateChildrenBooking = connectDB.prepareStatement(updateChildrenBookingQuery);
                    updateChildrenBooking.setString(1, chooseTour + ",");
                    updateChildrenBooking.setString(2, id_student);
                    updateChildrenBooking.executeUpdate();
                }
                
                //INSERT Booking information
                String insertBookingQuery = "INSERT INTO Booking(Place, Distance, id_parent, id_student, Date) VALUES (?,?,?,?,?)";
                PreparedStatement insertBooking = connectDB.prepareStatement(insertBookingQuery);
                
                if(queryOutput1.next() && queryOutput.next()){
                    String id_parent = queryOutput.getString("id_parent");
                    String id_student = queryOutput1.getString("id_student");
                    insertBooking.setString(1, chooseTour);
                    insertBooking.setDouble(2, calcDistance(chooseTour)); // Implement this method to calculate the distance
                    insertBooking.setString(3, id_parent);
                    insertBooking.setString(4, id_student);
                    insertBooking.setString(5, bookingDate.get("chooseTour"));//?
                    insertBooking.executeUpdate();
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
            String connectQuery = "SELECT id_event, Title, Description, Venue, Date, Time FROM parent";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
        
            while(queryOutput.next()){
                
            }
            
            connectDB.close();
            statement.close();
            queryOutput.close();
        
        
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    private boolean checkEventAllChildren(){
        //this is a method to check whether all child have event at that particular date or not
        //return true if gt
        try{
            DatabaseConnection dbConnection = new DatabaseConnection(); 
            Connection connectDB = dbConnection.linkDatabase();
            String connectQuery = "SELECT RegisteredEvent FROM student";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            if(queryOutput.next()){
                String children = queryOutput.getString("Username");
                String eventMade = queryOutput.getString("RegisteredEvent");
                if(!eventMade.isEmpty()){
                    return true;
                }
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void displayPastBooking() {
        //this method is to display the pastbooking
        try{
            DatabaseConnection dbConnection = new DatabaseConnection(); 
            Connection connectDB = dbConnection.linkDatabase();
            String connectQuery = "SELECT PastBooking FROM parent";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
        
            String pastBooking = null, parent = null;
            while(queryOutput.next()){
                pastBooking = queryOutput.getString("PastBooking");
                parent = queryOutput.getString("Username");
            }
            
            System.out.println("Parent - " + parent + " had made past booking at " + pastBooking);
            
            connectDB.close();
            statement.close();
            queryOutput.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    

    public void displayParentInfo(String username){
        try{
            String usernameParent = null, emailParent = null, roleParent = null, locationParent = null, booking = null;
            
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connectDB = dbConnection.linkDatabase();
            String connectQuery = "SELECT Username, Email, Role, Location, PastBooking FROM parent WHERE Username = '"+ username + "'";;
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            while(queryOutput.next()){
                usernameParent = queryOutput.getString("Username");
                emailParent = queryOutput.getString("Email");
                roleParent = queryOutput.getString("role");
                locationParent = queryOutput.getString("location");
                booking = queryOutput.getString("PastBooking");
            }
            
            System.out.println("=====Parent=====");
            System.out.println("Username: " + usernameParent);
            System.out.println("Email: " + emailParent);
            System.out.println("Role: " + roleParent);
            System.out.println("Location: " + locationParent);
            System.out.println("Booking Made: " + booking);
            
            connectDB.close();
            statement.close();
            queryOutput.close();
            
        }catch(Exception e){
            System.out.println("Problem with database connection");
            e.printStackTrace();
        }
    }
   
    public void displayChildren(){
        try{
            String chidlren = null, parent = null;
            
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connectDB = dbConnection.linkDatabase();
            String connectQuery = "SELECT Children FROM parent";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            
            while(queryOutput.next()){
                children = queryOutput.getString("Children");
                parent = queryOutput.getString("Username");
            }
            
            System.out.println("Parent: " + parent + "Children: " + children);
            
            
            connectDB.close();
            statement.close();
            queryOutput.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    private static double euclideanDistance(double x1, double y1, double x2, double y2){
        //to calculate distance
        return Math.sqrt((Math.pow((x2-x1), 2)) + Math.pow((y2-y1), 2));
    }

    private LocalDate updateDate(){
        LocalDate current = LocalDate.now();
        return current;
    }
    
    public void addChildren(String childName, String parentName) {
        int idChildren = getChildrenIdByChildrenUsername(childName);
        if (!isExistingChildren(idChildren)) {
            System.out.println("The children name you are trying to add does not exists.");
            JOptionPane.showMessageDialog(null, "The children name you are trying to add does not exists. ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection connectDB = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.linkDatabase();
            String connectStudentQuery = "SELECT id_sudent FROM student WHERE id_student = ?";

            preparedStatement = connectDB.prepareStatement(connectStudentQuery);
            preparedStatement.setInt(1, idChildren);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String currentChildren = resultSet.getString("Children");
                if (currentChildren == null) {
                    currentChildren = "";
                }
                String updatedChildren = currentChildren + childName + ",";

                // Update the database with the new children list
                String updateParentQuery = "UPDATE parent SET Children = ? WHERE id_parent = ?";
                PreparedStatement updateStatement = connectDB.prepareStatement(updateParentQuery);
                updateStatement.setString(1, updatedChildren);
                updateStatement.setInt(2, idChildren);
                updateStatement.executeUpdate();
                updateStatement.close();

                // update student table: parentNums and parentEmail
                updateParentInfoAfterAddChildren(childName);
            }

            resultSet.close();
            preparedStatement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        }
            
    
    public static boolean isExistingChildren(int idChildren) {
        boolean exists = false;

        try {
            // Connect to database
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // Check if the parent exists using id_parent
            String query = "SELECT COUNT(*) FROM student WHERE id_student = ?";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setInt(1, idChildren);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                exists = count > 0; // Child exists if count is greater than 0
            }

            resultSet.close();
            preparedStatement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        return exists;
    }
    
    public static String getIdParentFromName(String parentName) {
        String idParent = null;

        if (parentName == null || parentName.trim().isEmpty()) {
            return null;
        }

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            String selectQuery = "SELECT id_parent FROM parent WHERE LOWER(Username) = LOWER(?) LIMIT 1";
            PreparedStatement preparedStatement = connectDB.prepareStatement(selectQuery);
            preparedStatement.setString(1, parentName.trim());
            ResultSet queryOutput = preparedStatement.executeQuery();

            if (queryOutput.next()) {
                idParent = queryOutput.getString("id_parent");
            }

            queryOutput.close();
            preparedStatement.close();
            connectDB.close();
        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return idParent;
    }
    
    public static int getChildrenIdByChildrenUsername(String childrenUsername) {
        int childrenId = -1;

        try {
            // Connect to database
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // Create SQL query
            String query = "SELECT id_student FROM student WHERE Username = ?";

            // Use PreparedStatement to prevent SQL injection
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, childrenUsername);

            // Execute query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Retrieve the children ID
            if (resultSet.next()) {
                childrenId = resultSet.getInt("id_student");
            }

            // Close resources
            resultSet.close();
            preparedStatement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return childrenId;
    }
    
    public void relationshipFile(){
        try{
            Connection connectDB;
            Statement statement;
            ResultSet queryOutput;
            
            try (PrintWriter outputStream = new PrintWriter(new FileOutputStream("ParentChild.txt"))) {
                
                //Database Connection
                DatabaseConnection dbConnection = new DatabaseConnection();
                connectDB = dbConnection.linkDatabase();
                String connectQuery = "SELECT Username, Children FROM parents";
                statement = connectDB.createStatement();
                queryOutput = statement.executeQuery(connectQuery);
                
                String parent = null, children = null;
                
                while(queryOutput.next()){
                    parent = queryOutput.getString("Username");
                    children = queryOutput.getString("Children");
                }
                
                System.out.println("Parent: " + parent + " Children: " + children);
                outputStream.print(parent + "," + children);
            
            }
            connectDB.close();
            statement.close();
            queryOutput.close();
            
        }catch(IOException e){
            System.out.println("Problem with writing file");
        }catch(Exception e){
            System.out.println("Problem with Database Connetion");
            e.printStackTrace();
        }
    }
    
    public static String getChildrenEmailById(int idChildren) {
        String studentEmail = null;
        Connection connectDB = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.linkDatabase();

            String query = "SELECT Email FROM student WHERE id_student = ?";
            preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setInt(1, idChildren);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                studentEmail = resultSet.getString("Email");
            }

            resultSet.close();
            preparedStatement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        return studentEmail;
    }
    
    // update parent table
    public static void updateParentInfoAfterAddChildren(String childName) {
        Connection connectDB = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.linkDatabase();

            String connectStudentQuery = "SELECT Email FROM student WHERE Username = ?";
            preparedStatement = connectDB.prepareStatement(connectStudentQuery);
            preparedStatement.setString(1, childName);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String currentParentsEmail = resultSet.getString("ParentsEmail");
                
                // Update the database with the new children information
                String updateParentQuery = "UPDATE parent SET Children WHERE Username = ?";
                PreparedStatement updateStatement = connectDB.prepareStatement(updateParentQuery);
                updateStatement.setString(1, childName);
                updateStatement.executeUpdate();
                
                updateStatement.close();
                resultSet.close();
                preparedStatement.close();
                connectDB.close();
                
                JOptionPane.showMessageDialog(null, "New child is added! ", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }
}