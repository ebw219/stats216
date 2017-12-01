package edu.lehigh.cse216.lyle.admin;

public class UserBlocked{
    private int user_id1;
    private int user_id2;

    public UserBlocked(int user_id1, int user_id2) {
        this.user_id1 = user_id1;
        this.user_id2 = user_id2;
    }


    //getters below

    public int getUid1() {
        return user_id1;
    }

    public int getUid2() {
        return user_id2;
    }

    public String toString(){
        return (user_id1+"\t \t"+user_id2);
    }

}