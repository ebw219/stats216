package edu.lehigh.cse216.lyle.admin;

public class User{
    private String email;
    private String name;
    private int id;
    // may be more fields
    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
    //getters below
    //...

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

}