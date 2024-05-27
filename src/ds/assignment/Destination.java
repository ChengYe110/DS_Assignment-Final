/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds.assignment;

/**
 *
<<<<<<< HEAD
 * @author Tan Zhi Wei
 */
public class Destination {
    private String destinationName;
    private double x;
    private double y;
    
    public Destination(String destination, double x, double y){
=======
 * @author enjye
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Destination {

    private String destinationName;
    private double x;
    private double y;
    private double distance;

    public Destination(String destination, double x, double y) {
>>>>>>> 2481ee194f3368bfba56fe04719c1aae894f008c
        this.destinationName = destination;
        this.x = x;
        this.y = y;
    }

<<<<<<< HEAD
=======
    public Destination(String destination, double x, double y, double targetX, double targetY) {
        this.destinationName = destination;
        this.x = x;
        this.y = y;
        double deltaX = this.x - targetX;
        double deltaY = this.y - targetY;
        double rawDistance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        this.distance = Math.round(rawDistance * 100.0) / 100.0;
    }

    public double getDistance(){
        return distance;
    }
    
>>>>>>> 2481ee194f3368bfba56fe04719c1aae894f008c
    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
<<<<<<< HEAD
    
    @Override
    public String toString(){
        return "\nDestination{" +
                "name='" + this.destinationName + '\'' +
                ", latitude=" + this.x +
                ", longitude=" + this.y +
                "}";
    }
=======

    @Override
    public String toString() {
        return "\nDestination{"
                + "name='" + this.destinationName + '\''
                + ", latitude=" + this.x
                + ", longitude=" + this.y
                + "}";
    }

>>>>>>> 2481ee194f3368bfba56fe04719c1aae894f008c
}
