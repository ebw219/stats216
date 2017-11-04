package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

import static lyle.cse216.lehigh.edu.tutorialforlyle.MainActivity.getUsernameById;

/**
 * Created by emyweston on 11/2/17.
 * Phase 4
 */

public class UserProfPageActivity extends AppCompatActivity {

    String url = "https://lyle-buzz.herokuapp.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        //get username and uId from intent
        Intent viewProfile = getIntent();
        String profusername = viewProfile.getStringExtra("username");
        Log.d("lyle", "getting username " + profusername);
        ((TextView)findViewById(R.id.profileUsername)).setText(profusername);
//        String uid = getUIdByUsername(mUsers, profusername) + "";
//        String uId = viewProfile.getIntExtra("uId", );

    } //end onCreate


//    static int getUIdByUsername(String username){
//        Log.d("lyle", "GIVEN USERNAME: " + username);
//        //for(int i = 0; i < mUsers.size(); i++){
//            //Log.d("lyle", "LOOKING AT: " + mUsers.get(i).username);
//            //if(mUsers.get(i).username.equals(username)){
//                return mUsers.get(i).uId;
//            }
//        //}
//        return -1;
//    }

//    /**
//     * GET Volley request
//     * @return the request
//     */
//    protected StringRequest getMessages() {
//        return (new StringRequest(Request.Method.GET, url + "messages/users/" + user_id",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("lyle", response);
//                        getUserInfo(response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("lyle", "That Get didn't work!");
//            }
//        }));
//    } //end getMessages

}
