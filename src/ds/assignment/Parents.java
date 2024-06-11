/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds.assignment;

import ds.assignment.Destination;
import static ds.assignment.Students.getNumberOfParents;
import static ds.assignment.Students.updateStudentInfoAfterAddParent;
import gui.ChildrenColumn;
import gui.PastBookingColumn;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class Parents extends User {

    private String children;
    HashMap<LocalDate, String> bookingDate = new HashMap<>();//for date
    //List<Destination> destination = new ArrayList<>();//for calculate distance
    //linked list?(priority age-from oldest to youngest)
    ArrayList<String> childrenList = new ArrayList<>();

    public Parents(String email, String username, String password, String role) {
        super(email, username, password, role);

        //to assign date to the bookingDestination
        int year = LocalDate.now().getYear();
        LocalDate date = LocalDate.of(year, 1, 1);

        while (date.getYear() == year) {
            if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
                bookingDate.put(date, "Petrosains Science Discovery Centre");
            } else if (date.getDayOfWeek() == DayOfWeek.TUESDAY) {
                bookingDate.put(date, "Tech Dome Penang");
            } else if (date.getDayOfWeek() == DayOfWeek.WEDNESDAY) {
                bookingDate.put(date, "Agro Technology Park in MARDI");
            } else if (date.getDayOfWeek() == DayOfWeek.THURSDAY) {
                bookingDate.put(date, "National Science Centre");
            } else if (date.getDayOfWeek() == DayOfWeek.FRIDAY) {
                bookingDate.put(date, "Marine Aquarium and Museum");;
            } else if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                bookingDate.put(date, "Pusat Sains & Kreativiti Terengganu");
                bookingDate.put(date, "Biomedical Museum");
            } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                bookingDate.put(date, "Telegraph Museum");
                bookingDate.put(date, "Penang Science Cluster");
            } else {
                System.out.println("No such day");
            }
            date = date.plusDays(1);
        }
        //the children?

    }

    public void insertIntoDatabase() {
        if (!InfoCheck()) {
            Connection connection = null;
            try {
                // Database connection details
                connection = dbConnect.linkDatabase();

                String insertQuery = "INSERT INTO parent (Username, Email, Password, Role,  Location) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, hashPassword(password));
                preparedStatement.setString(4, role);
                preparedStatement.setString(5, location);

                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                dbConnect.endDatabase();
            }
        } else {
            System.out.println("Username or password already exists. Please choose a different username or password.");
        }

    }

//    public void readFile() {
//      //this method is to read the bookingDestination File
//        String line;        
//        
//        try{
//            BufferedReader br = new BufferedReader(new FileReader("BookingDestination.txt"));     
//            while((line = br.readLine()) != null){
//                String locationName = line.trim();
//                if((line = br.readLine()) != null){
//                String[] parts = line.trim().split(",");
//                    if(parts.length == 2){
//                        double XValue = Double.parseDouble(parts[0].trim());
//                        double YValue = Double.parseDouble(parts[1].trim());
//                        destination.add(new Destination(locationName,XValue,YValue));
//                    }else{
//                        System.out.println("problem inside");
//                    }
//                }else{
//                    System.out.println("problem outside");
//                }
//                
//            }
//            System.out.println(destination.toString());
//            br.close();
//        
//        }catch(FileNotFoundException e){
//            System.out.println("Problem with file not found");
//            
//        }catch(IOException e){
//            System.out.println("Problem with file input and output");
//        }
//    }
//    
//    public void calcDistance() {
//        String line;
//        try {
//            double parentX = 0, parentY = 0;
//            Connection connectDB = null;
//            try {
//                DatabaseConnection dbConnection = new DatabaseConnection();
//                connectDB = dbConnection.linkDatabase();
//                String connectQuery = "SELECT Location FROM parent";
//                Statement statement = connectDB.createStatement();
//                ResultSet queryOutput = statement.executeQuery(connectQuery);
//
//                if (queryOutput.next()) {
//                    String location = queryOutput.getString("Location");
//                    String[] locationToCalc = location.split(",");
//                    parentX = Double.parseDouble(locationToCalc[0]);
//                    parentY = Double.parseDouble(locationToCalc[1]);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            BufferedReader br = new BufferedReader(new FileReader("BookingDestination.txt"));
//            while ((line = br.readLine()) != null) {
//                String locationName = line.trim();
//                if ((line = br.readLine()) != null) {
//                    String[] parts = line.trim().split(",");
//                    if (parts.length == 2) {
//                        double XValue = Double.parseDouble(parts[0].trim());
//                        double YValue = Double.parseDouble(parts[1].trim());
//                        destination.add(new Destination(locationName, XValue, YValue, parentX, parentY));
//                    } else {
//                        System.out.println("problem inside");
//                    }
//                } else {
//                    System.out.println("problem outside");
//                }
//
//            }
//            br.close();
//
//        } catch (FileNotFoundException e) {
//            System.out.println("Problem with file not found");
//
//        } catch (IOException e) {
//            System.out.println("Problem with file input and output");
//        }
//
//    }
    public boolean checkCollision() {
        //return true when have collision

        try {
            //get the event date from database and compare with the date of bookingTour, if equal then return true
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connectDB = dbConnection.linkDatabase();

            String connectQuery = "SELECT Date FROM event";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                LocalDate eventDate = LocalDate.parse(queryOutput.getString("Date"));
                if (bookingDate.containsKey(eventDate)) {
                    connectDB.close();
                    statement.close();
                    queryOutput.close();
                    return true;
                }
            }

            connectDB.close();
            statement.close();
            queryOutput.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//    public void displayNextSevenBookingDate(){
//        for (int i = 0; i < 10; i++) {
//            System.out.println("\n" + bookingDate.);
//        }
//    }
    
    public String bookingDestination(String chooseTour) {
        //check whether all the children have event at that day or not //done but need modify
        //if success  - database connection
        //insert that particular time into the past booking column
        //readFile();
        if (checkCollision() || checkEventAllChildren()) {
            return "Booking unsuccessful because clash with other event"; // better throw exception(?
        } else {
            try {
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

                while (queryOutput.next()) {
                    String booking = queryOutput.getString("Booking");
                    String id_parent = queryOutput.getString("id_parent");
                    String updateParentBookingQuery = "UPDATE Parent SET PastBooking = ? WHERE id_parent = ?";
                    PreparedStatement updateParentBooking = connectDB.prepareStatement(updateParentBookingQuery);
                    updateParentBooking.setString(1, chooseTour + ",");
                    updateParentBooking.setString(2, id_parent);
                    updateParentBooking.executeUpdate();
                }

                while (queryOutput1.next()) {
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

                if (queryOutput1.next() && queryOutput.next()) {
                    String id_parent = queryOutput.getString("id_parent");
                    String id_student = queryOutput1.getString("id_student");
                    insertBooking.setString(1, chooseTour);
                    //insertBooking.setDouble(2, calcDistance(chooseTour)); // Implement this method to calculate the distance
                    insertBooking.setString(3, id_parent);
                    insertBooking.setString(4, id_student);
                    insertBooking.setString(5, bookingDate.get("chooseTour"));//?
                    insertBooking.executeUpdate();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String viewEvent() {
        try {
            //check the upcoming 3 event
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connectDB = dbConnection.linkDatabase();
            String connectQuery = "SELECT id_event, Title, Description, Venue, Date, Time FROM parent";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {

            }

            connectDB.close();
            statement.close();
            queryOutput.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean checkEventAllChildren() {
        //this is a method to check whether all child have event at that particular date or not
        //return true if gt
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connectDB = dbConnection.linkDatabase();
            String connectQuery = "SELECT RegisteredEvent FROM student";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String children = queryOutput.getString("Username");
                String eventMade = queryOutput.getString("RegisteredEvent");
                if (!eventMade.isEmpty()) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void displayPastBooking() {
        //this method is to display the pastbooking
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connectDB = dbConnection.linkDatabase();
            String connectQuery = "SELECT PastBooking FROM parent";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            String pastBooking = null, parent = null;
            while (queryOutput.next()) {
                pastBooking = queryOutput.getString("PastBooking");
                parent = queryOutput.getString("Username");
            }

            System.out.println("Parent - " + parent + " had made past booking at " + pastBooking);

            connectDB.close();
            statement.close();
            queryOutput.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayParentInfo(String username) {
        try {
            String usernameParent = null, emailParent = null, roleParent = null, locationParent = null, booking = null;

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connectDB = dbConnection.linkDatabase();
            String connectQuery = "SELECT Username, Email, Role, Location, PastBooking FROM parent WHERE Username = '" + username + "'";;
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
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

        } catch (Exception e) {
            System.out.println("Problem with database connection");
            e.printStackTrace();
        }
    }

    public void displayChildren() {
        try {
            String chidlren = null, parent = null;

            DatabaseConnection dbConnection = new DatabaseConnection();
            Connection connectDB = dbConnection.linkDatabase();
            String connectQuery = "SELECT Children FROM parent";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                children = queryOutput.getString("Children");
                parent = queryOutput.getString("Username");
            }

            System.out.println("Parent: " + parent + "Children: " + children);

            connectDB.close();
            statement.close();
            queryOutput.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static double euclideanDistance(double x1, double y1, double x2, double y2) {
        //to calculate distance
        return Math.sqrt((Math.pow((x2 - x1), 2)) + Math.pow((y2 - y1), 2));
    }

    private LocalDate updateDate() {
        LocalDate current = LocalDate.now();
        return current;
    }

    public static void addChildren(String childName, String parentName) {
        String idChildren = getChildrenIdByChildrenUsername(childName);
        
        DatabaseConnection dbConnect = new DatabaseConnection();
        UserRepository userRepository = new UserRepository(dbConnect);
        
        if (parentName.equals(childName)){
            System.out.println("You cannot add yourself as child!!!");
            JOptionPane.showMessageDialog(null, "You cannot add yourself as child!!!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!isExistingChildren(idChildren)) {
            System.out.println("The children name you are trying to add does not exists.");
            JOptionPane.showMessageDialog(null, "The children name you are trying to add does not exists. ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!userRepository.getRole(childName).equalsIgnoreCase("Student")){
            System.out.println("You cannot add "+userRepository.getRole(childName)+" as child!!!");
            JOptionPane.showMessageDialog(null, "You cannot add "+userRepository.getRole(childName)+" as child!!!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }    
        
        int numberOfParents = getNumberOfParents(childName);
        if (numberOfParents >= 2) {
            System.out.println(childName+" already have 2 parents!!!");
            JOptionPane.showMessageDialog(null, childName+" already have 2 parents!!!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }    

        Connection connectDB = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.linkDatabase();
            String connectStudentQuery = "SELECT Children FROM parent WHERE Username = ?";

            preparedStatement = connectDB.prepareStatement(connectStudentQuery);
            preparedStatement.setString(1, parentName);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String currentChildren = resultSet.getString("Children");
                if (currentChildren == null) {
                    currentChildren = "";
                }
                String updatedChildren = currentChildren + idChildren + ",";

                // Update the database with the new children list
                String updateParentQuery = "UPDATE parent SET Children = ? WHERE Username = ?";
                PreparedStatement updateStatement = connectDB.prepareStatement(updateParentQuery);
                updateStatement.setString(1, updatedChildren);
                updateStatement.setString(2, parentName);
                updateStatement.executeUpdate();
                updateStatement.close();
                         
                recordToParentChildTxt(parentName,childName);
                JOptionPane.showMessageDialog(null, "You've successfully add "+childName+" as your child!!!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // update student table: parentNums and parentEmail
                updateStudentInfoAfterAddParent(childName,parentName);
            }

            resultSet.close();
            preparedStatement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }
    
    public static void recordToParentChildTxt(String parentUsername, String childName) {
        String filename = "ParentChild.txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            String line = parentUsername + ", " + childName;
            writer.write(line);
            writer.newLine();
            System.out.println("Successfully written to the file: " + line);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
    
    public static ArrayList<String> getChildList(String username) {
        ArrayList<String> childList = new ArrayList<>();

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT Children FROM parent WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String child = queryOutput.getString("Children");
                if (child != null && !child.isEmpty()) {
                    String[] childArray = child.split(",");
                    for (String c : childArray) {
                        childList.add(c.trim()); // Trim to remove any extra spaces
                    }
                }
            }

            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return childList;
    }

    public static boolean isExistingChildren(String idChildren) {
        boolean exists = false;

        try {
            // Connect to database
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // Check if the parent exists using id_parent
            String query = "SELECT COUNT(*) FROM student WHERE id_student = ?";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, idChildren);
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

    public static String getChildrenIdByChildrenUsername(String childrenUsername) {
        String childrenId = "";

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
                childrenId = resultSet.getString("id_student");
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

    public void relationshipFile() {
        try {
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

                while (queryOutput.next()) {
                    parent = queryOutput.getString("Username");
                    children = queryOutput.getString("Children");
                }

                System.out.println("Parent: " + parent + " Children: " + children);
                outputStream.print(parent + "," + children);

            }
            connectDB.close();
            statement.close();
            queryOutput.close();

        } catch (IOException e) {
            System.out.println("Problem with writing file");
        } catch (Exception e) {
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

    public static List<Destination> getTop5ShortestDistances() {
        List<Destination> destination = new ArrayList<>();
        String line;
        try {
            double parentX = 0, parentY = 0;
            Connection connectDB = null;
            try {
                DatabaseConnection dbConnection = new DatabaseConnection();
                connectDB = dbConnection.linkDatabase();
                String connectQuery = "SELECT Location FROM parent";
                Statement statement = connectDB.createStatement();
                ResultSet queryOutput = statement.executeQuery(connectQuery);

                if (queryOutput.next()) {
                    String location = queryOutput.getString("Location");
                    String[] locationToCalc = location.split(",");
                    parentX = Double.parseDouble(locationToCalc[0]);
                    parentY = Double.parseDouble(locationToCalc[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            BufferedReader br = new BufferedReader(new FileReader("BookingDestination.txt"));
            while ((line = br.readLine()) != null) {
                String locationName = line.trim();
                if ((line = br.readLine()) != null) {
                    String[] parts = line.trim().split(",");
                    if (parts.length == 2) {
                        double XValue = Double.parseDouble(parts[0].trim());
                        double YValue = Double.parseDouble(parts[1].trim());
                        destination.add(new Destination(locationName, XValue, YValue, parentX, parentY));
                    } else {
                        System.out.println("problem inside");
                    }
                } else {
                    System.out.println("problem outside");
                }

            }
            br.close();

        } catch (FileNotFoundException e) {
            System.out.println("Problem with file not found");

        } catch (IOException e) {
            System.out.println("Problem with file input and output");
        }
        List<Destination> sortedDestinations = new ArrayList<>(destination);
        Collections.sort(sortedDestinations, Comparator.comparingDouble(Destination::getDistance));
        return sortedDestinations.subList(0, Math.min(5, sortedDestinations.size()));
    }

    public static ArrayList<PastBookingColumn> getPastBooking(String parentUsername) {
        ArrayList<PastBookingColumn> pastBookingList = new ArrayList<>();

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // get id_booking from PastBooking column in parent table
            String getIdPastBookingQuery = "SELECT PastBooking FROM parent WHERE Username = ?";
            PreparedStatement getIdPastBookingStmt = connectDB.prepareStatement(getIdPastBookingQuery);
            getIdPastBookingStmt.setString(1, parentUsername);
            System.out.println(parentUsername);
            ResultSet pastBookingResultSet = getIdPastBookingStmt.executeQuery();

            if (pastBookingResultSet.next()) {
                String pastBooking = pastBookingResultSet.getString("PastBooking");
                System.out.println(pastBooking);
                if (pastBooking != null && !pastBooking.isEmpty()) {
                    String[] bookingIds = pastBooking.split(",");

                    // get past booking details for each id_booking in booking table row by row
                    // PastBookingColumn with integer no, date, children, place, distance
                    String getPastBookingDetailsQuery = "SELECT Date, id_student, Venue, Distance FROM booking WHERE id_booking = ?";
                    PreparedStatement getPastBookingStmt = connectDB.prepareStatement(getPastBookingDetailsQuery);

                    int numberOfBookingId = 1;
                    for (String bookingId : bookingIds) {
                        getPastBookingStmt.setString(1, bookingId.trim());
                        ResultSet pastBookingDetailsResultSet = getPastBookingStmt.executeQuery();

                        if (pastBookingDetailsResultSet.next()) {
                            Date dateBook = pastBookingDetailsResultSet.getDate("Date");
                            //String dateBookString = dateBook != null ? dateBook.toString() : null;
                            LocalDate localDate = dateBook.toLocalDate();
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            String dateS = localDate.format(dateFormatter).toString();
                            String id_student = pastBookingDetailsResultSet.getString("id_student");
                            String venue = pastBookingDetailsResultSet.getString("Venue");
                            String distance = pastBookingDetailsResultSet.getString("Distance");

                            // get child name from student table based on the id_student
                            String getStudentNameQuery = "SELECT Username FROM student WHERE id_student = ?";
                            PreparedStatement getStudentNameStmt = connectDB.prepareStatement(getStudentNameQuery);
                            getStudentNameStmt.setString(1, id_student.trim());
                            ResultSet getStudentResultSet = getStudentNameStmt.executeQuery();

                            String nameStudent = null;
                            if (getStudentResultSet.next()) {
                                nameStudent = getStudentResultSet.getString("Username");
                            }

                            getStudentResultSet.close();
                            getStudentNameStmt.close();

                            PastBookingColumn booking = new PastBookingColumn(numberOfBookingId++, dateS, nameStudent, venue, distance);
                            pastBookingList.add(booking);
                        }
                        pastBookingDetailsResultSet.close();
                    }
                    getPastBookingStmt.close();
                }
            }
            pastBookingResultSet.close();
            getIdPastBookingStmt.close();
            connectDB.close();

        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        return pastBookingList;
    }

    public static ArrayList<ChildrenColumn> getChildren(String parentUsername) {
        ArrayList<ChildrenColumn> childrenList = new ArrayList<>();

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // get id_student from Children column in parent table
            String getIdChildrenQuery = "SELECT Children FROM parent WHERE Username = ?";
            PreparedStatement getIdChildrenStmt = connectDB.prepareStatement(getIdChildrenQuery);
            getIdChildrenStmt.setString(1, parentUsername);
            ResultSet idChildrenResultSet = getIdChildrenStmt.executeQuery();

            if (idChildrenResultSet.next()) {
                String idChildren = idChildrenResultSet.getString("Children");
                if (idChildren != null && !idChildren.isEmpty()) {
                    String[] childrenIds = idChildren.split(",");

                    // get children name details for each id_student in student table row by row
                    String getNameStudentQuery = "SELECT Username FROM student WHERE id_student = ?";
                    PreparedStatement getNameStudentStmt = connectDB.prepareStatement(getNameStudentQuery);

                    Integer numberOfChildren = 1;
                    for (String childId : childrenIds) {
                        getNameStudentStmt.setString(1, childId.trim());
                        ResultSet nameStudentResultSet = getNameStudentStmt.executeQuery();

                        if (nameStudentResultSet.next()) {
                            String nameChildren = nameStudentResultSet.getString("Username");
                            ChildrenColumn child = new ChildrenColumn(numberOfChildren++, nameChildren);
                            childrenList.add(child);
                        }
                        nameStudentResultSet.close();  // Close the ResultSet for each event ID                  
                    }
                    getNameStudentStmt.close();
                }
            }
            idChildrenResultSet.close();
            getIdChildrenStmt.close();
            connectDB.close();

        } catch (SQLException e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
        return childrenList;
    }

    public static ArrayList<LocalDate> getJoinedEventDates(String username) {
        ArrayList<String> joinedEventList = new ArrayList<>();
        ArrayList<LocalDate> eventDates = new ArrayList<>();

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // Fetch registered event IDs for the user
            String connectQuery = "SELECT RegisteredEvent FROM student WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String registeredEvent = queryOutput.getString("RegisteredEvent");
                if (registeredEvent != null && !registeredEvent.isEmpty()) {
                    String[] eventsArray = registeredEvent.split(",");
                    for (String event : eventsArray) {
                        joinedEventList.add(event.trim()); // Trim to remove any extra spaces
                    }
                }
            }

            // Build the query to fetch event dates
            if (!joinedEventList.isEmpty()) {
                StringBuilder idQueryBuilder = new StringBuilder("SELECT id_event, Date FROM event WHERE id_event IN (");
                for (int i = 0; i < joinedEventList.size(); i++) {
                    idQueryBuilder.append("'").append(joinedEventList.get(i)).append("'");
                    if (i < joinedEventList.size() - 1) {
                        idQueryBuilder.append(", ");
                    }
                }
                idQueryBuilder.append(")");

                // Execute the query to fetch event dates
                ResultSet dateResultSet = statement.executeQuery(idQueryBuilder.toString());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Adjust the pattern as needed
                while (dateResultSet.next()) {
                    String eventDate = dateResultSet.getString("Date");
                    if (eventDate != null && !eventDate.isEmpty()) {
                        LocalDate localDate = LocalDate.parse(eventDate, formatter);
                        eventDates.add(localDate);
                    }
                }
                dateResultSet.close();
            }

            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return eventDates;
    }

    public static ArrayList<LocalDate> getBookingDates(String username) {
        ArrayList<String> bookingIDs = new ArrayList<>();
        ArrayList<LocalDate> bookingDates = new ArrayList<>();

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();

            // Fetch registered booking IDs for the user
            String connectQuery = "SELECT RegisteredBooking FROM student WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String bookedDestinations = queryOutput.getString("RegisteredBooking");
                if (bookedDestinations != null && !bookedDestinations.isEmpty()) {
                    String[] bookingsArray = bookedDestinations.split(",");
                    for (String booking : bookingsArray) {
                        bookingIDs.add(booking.trim()); // Trim to remove any extra spaces
                    }
                }
            }

            // Build the query to fetch booking dates
            if (!bookingIDs.isEmpty()) {
                StringBuilder idQueryBuilder = new StringBuilder("SELECT id_booking, Date FROM booking WHERE id_booking IN (");
                for (int i = 0; i < bookingIDs.size(); i++) {
                    idQueryBuilder.append("'").append(bookingIDs.get(i)).append("'");
                    if (i < bookingIDs.size() - 1) {
                        idQueryBuilder.append(", ");
                    }
                }
                idQueryBuilder.append(")");

                // Execute the query to fetch booking dates
                ResultSet dateResultSet = statement.executeQuery(idQueryBuilder.toString());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Adjust the pattern as needed
                while (dateResultSet.next()) {
                    String bookingDate = dateResultSet.getString("Date");
                    if (bookingDate != null && !bookingDate.isEmpty()) {
                        LocalDate localDate = LocalDate.parse(bookingDate, formatter);
                        bookingDates.add(localDate);
                    }
                }
                dateResultSet.close();
            }

            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return bookingDates;
    }

    //the username should be the children username
    public static ArrayList<String> compareDate(String username) {
        LocalDate today = LocalDate.now();
        ArrayList<LocalDate> bookingDates = getBookingDates(username);
        ArrayList<LocalDate> joinedEventDates = getJoinedEventDates(username);
        ArrayList<String> displayDates = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Iterate through the next 7 days
        for (int i = 1; i <= 7; i++) {
            LocalDate futureDate = today.plusDays(i);
            boolean dateExists = false;

            // Check if the date exists in bookingDates
            for (LocalDate bookingDate : bookingDates) {
                if (futureDate.isEqual(bookingDate)) {
                    dateExists = true;
                    break;
                }
            }

            // Check if the date exists in joinedEventDates
            if (!dateExists) {
                for (LocalDate joinedEventDate : joinedEventDates) {
                    if (futureDate.isEqual(joinedEventDate)) {
                        dateExists = true;
                        break;
                    }
                }
            }

            // If the date does not exist in either list, add it to the displayDates
            if (!dateExists) {
                displayDates.add(futureDate.format(formatter)); // Convert LocalDate to String
            }
        }

        return displayDates;
    }
}
