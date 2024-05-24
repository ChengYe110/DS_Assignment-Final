/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import ds.assignment.DatabaseConnection;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
/**
 *
 * @author enjye
 */
public class Booking {
    private String ID;
    private String venue;
    private String distance;
    private LocalDate date;
    private String dateS;
    private String id_parent;
    private String id_student;

    public Booking(String venue, String distance, LocalDate date, String id_parent, String id_student) {
        this.venue = venue;
        this.distance = distance;
        this.date = date;
        this.id_parent = id_parent;
        this.id_student = id_student;
    }
    
    public Booking(String ID, String venue, String distance, LocalDate date, String id_parent, String id_student) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.ID = ID;
        this.venue = venue;
        this.distance = distance;
        this.date = date;
        this.id_parent = id_parent;
        this.id_student = id_student;
        this.dateS = date.format(dateFormatter);
    }

    public String getVenue() {
        return venue;
    }

    public String getDistance() {
        return distance;
    }

    public String getIdParent() {
        return id_parent;
    }

    public String getIdStudent() {
        return id_student;
    }
    
    public String getID(){
        return ID;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public String getDateS() {
        return dateS;
    }

    public void saveBooking(String parentUsername) {
        System.out.println("hi");
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.linkDatabase();

        try {

            String query = "INSERT INTO booking (Venue, Distance, Date, id_parent, id_student) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, this.venue);
            preparedStatement.setString(2, this.distance);
            Date a = Date.valueOf(date);
            preparedStatement.setDate(3, a);
            preparedStatement.setString(4, this.id_parent);
            preparedStatement.setString(5, this.id_student);

            preparedStatement.executeUpdate(); //delete after execute next
            System.out.println("haha");

//            if (preparedStatement.executeUpdate() == 0) {
//                String query2 = "UPDATE educator SET NumQuiz=NumQuiz+1 WHERE Username=?";
//                PreparedStatement preparedStatement2 = connectDB.prepareStatement(query2);
//                preparedStatement2.setString(1, parentUsername);
//                preparedStatement2.executeUpdate();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectNow.endDatabase();
        }
    }

//    public static ArrayList<LocalDate> getPastBookingDateList() {
//        ArrayList<LocalDate> pastBookingDateList = new ArrayList<>();
//        try {
//            DatabaseConnection connectNow = new DatabaseConnection();
//            Connection connectDB = connectNow.linkDatabase();
//            String query = "SELECT Date FROM booking";
//            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                LocalDate date = resultSet.getDate("Date").toLocalDate();
//                pastBookingDateList.add(date);
//            }
//            Collections.sort(pastBookingDateList);
//
//            resultSet.close();
//            preparedStatement.close();
//            connectDB.close();
//        } catch (Exception e) {
//            System.out.println("SQL query failed.");
//            e.printStackTrace();
//        }
//
//        return eventDateList;
//    }
    
    //parent username
    public List<String> getPastBookingList(String username) {
        ArrayList<String> pastBookingList = new ArrayList<>();

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT PastBooking FROM parent WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String pastBooking = queryOutput.getString("PastBooking");
                if (pastBooking != null && !pastBooking.isEmpty()) {
                    String[] pastBookingArray = pastBooking.split(",");
                    for (String book : pastBookingArray) {
                        pastBookingList.add(book.trim()); // Trim to remove any extra spaces
                    }
                }
            }

            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }

        return pastBookingList;
    }
    
    //parent username and booking id
    public void addPastBooking(String username, String pastBooking) {

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.linkDatabase();
            String connectQuery = "SELECT PastBooking FROM parent WHERE Username = '" + username + "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            if (queryOutput.next()) {
                String currentBook = queryOutput.getString("PastBooking");
                if (currentBook == null) {
                    currentBook = "";
                }
                String updatedPastBook = currentBook + pastBooking + ",";

                // Update the database with the new friend list
                String updateQuery = "UPDATE parent SET PastBooking = '" + updatedPastBook + "' WHERE Username = '" + username + "'";
                statement.executeUpdate(updateQuery);
            }
            statement.close();
            connectDB.close();
        } catch (Exception e) {
            System.out.println("SQL query failed.");
            e.printStackTrace();
        }
    }
    
    public static List<Booking> getBookingList(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.linkDatabase();
        
        List<Booking> res = new ArrayList<>();

        try {

            String query = "SELECT * FROM booking";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String ID = resultSet.getString("id_booking");
                String venue = resultSet.getString("Venue");
                String distance = resultSet.getString("Distance");
                Date date = resultSet.getDate("Date");
                LocalDate localdate = date.toLocalDate();
                String idp = resultSet.getString("id_parent");
                String ids = resultSet.getString("id_student");
                res.add(new Booking(ID, venue, distance, localdate, idp, ids));
            }
            
            return res;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            connectNow.endDatabase();
        }
    }
}
