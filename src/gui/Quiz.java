/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

/**
 *
 * @author enjye
 */
public class Quiz {
    private SimpleStringProperty title;
    private SimpleStringProperty description;
    private SimpleStringProperty theme;
    private SimpleStringProperty content;
    
    public Quiz(SimpleStringProperty title, SimpleStringProperty description,SimpleStringProperty theme, SimpleStringProperty content){
        this.title=title;
        this.description=description;
        this.theme=theme;
        this.content=content;
    }

    
}
