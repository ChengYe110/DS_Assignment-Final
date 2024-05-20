/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

/**
 *
 * @author enjye
 */
public class ParentColumn {
    private String username;
    private Integer no;
    
    ParentColumn(Integer no,String username){
        this.username=username;
        this.no=no;
    }
    
    public String getUsername(){
        return this.username;
    }
    
    public Integer getNo(){
        return this.no;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNo(Integer no) {
        this.no = no;
    }
    
}
