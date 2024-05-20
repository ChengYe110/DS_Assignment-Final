/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ds.assignment;

/**
 *
 * @author Tan Zhi Wei
 */
public class Destination {
    private String destinationName;
    private double x;
    private double y;
    
    public Destination(String destination, double x, double y){
        this.destinationName = destination;
        this.x = x;
        this.y = y;
    }

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
    
    @Override
    public String toString(){
        return "\nDestination{" +
                "name='" + this.destinationName + '\'' +
                ", latitude=" + this.x +
                ", longitude=" + this.y +
                "}";
    }
}
