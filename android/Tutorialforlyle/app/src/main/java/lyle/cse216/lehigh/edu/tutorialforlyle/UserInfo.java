package lyle.cse216.lehigh.edu.tutorialforlyle;

/**
 * Created by Kelli on 10/12/17.
 */

public class UserInfo {

    int uId;
    String username;
    String realName;


    public UserInfo(int uId, String username, String realName) {
        this.uId = uId;
        this.username = username;
        this.realName = realName;
    }

    @Override
    public String toString(){
        return "\tuId: " + uId + ", username: " + username + ", realname: " + realName;
    }

}
