package com.example.gpskeychain;

public class UserClass {
    private int id;
    private int key_id;
    private String Username;
    private String Fname;
    private String password;
    private String Lname;
    private double curr_lat;
    private double curr_long;

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public void setFname(String Fname){
        this.Fname=Fname;
    }
    public String getFname(){
        return Fname;
    }
    public void setUsername(String Username){
        this.Username=Username;
    }
    public String getUsername(){
        return Username;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public String getPassword(){
        return password;
    }
    public String getLname(){
        return Lname;
    }
    public void setLname(String Lname){
        this.Lname=Lname;
    }
    public int getKeyId(){
        return key_id;
    }
    public void setKeyId(int key_id){
        this.key_id=key_id;
    }
    public void setCurr_lat(double curr_lat){
        this.curr_lat=curr_lat;
    }
    public double getCurr_lat(){
        return curr_lat;
    }
    public void setCurr_long(double curr_long){
        this.curr_long=curr_long;
    }
    public double getCurr_long(){
        return curr_long;
    }
}
