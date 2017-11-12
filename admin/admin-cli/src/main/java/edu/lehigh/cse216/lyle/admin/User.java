package edu.lehigh.cse216.lyle.admin;

public class User{
    private String email;
    private String username;
    private int id;
    private String name;

    public User(int id, String name, String username, String email) {
        this.email = email;
        this.username = username;
        this.id = id;
        this.name = name;
    }


    //getters below

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString(){
        return (id+"\t"+username+"\t\t"+name+"\t"+email);
    }

}