package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static lyle.cse216.lehigh.edu.tutorialforlyle.MainActivity.getUsernameById;

/**
 * Created by emyweston on 11/2/17.
 * Phase 4
 */

public class UserProfPageActivity extends AppCompatActivity {

    String url = "https://lyle-buzz.herokuapp.com/";
    String uid;
    ArrayList<Titles> titles = new ArrayList<>();
    static RecyclerView rv;
    MessageTitleListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        //get username and uId from intent
        Intent viewProfile = getIntent();
        String profusername = viewProfile.getStringExtra("usernameProf");
        Log.d("lyle", "getting username " + profusername);
        ((TextView)findViewById(R.id.profileUsername)).setText(profusername);
        //String uid = getUIdByUsername(mUsers, profusername) + "";
        uid = viewProfile.getStringExtra("uId");//ItemListAdapter.viewProfile.getUId());
        Log.d("lyle", "getting uid " + uid);

        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        queue.add(getMessages());



//        // Instantiate the RequestQueue
//        //how to add messages created by the user into the items_by_user list
        rv = (RecyclerView) findViewById(R.id.messages_by_user);
        adapter = new MessageTitleListAdapter(this, titles);
        MySingleton.getInstance(this).addToRequestQueue(getMessages());


    } //end onCreate

    /**
     * GET Volley request
     * @return the request
     */
    protected StringRequest getMessages() {
        Log.d("lyle", "entered getMessages");
        return (new StringRequest(Request.Method.GET, url + "messages/users/" + uid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("lyle", "messages by uid: "+response);
                        populateListFromVolley(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("lyle", "userprofpageactivity line 60 That Get didn't work!");
            }
        }));
    } //end getMessages

    /**
     * Helper method for GET function
     *
     * @param response string of JSON data obtained from Get
     */
    private void populateListFromVolley(String response) {
        Log.d("lyle", "HERE");
        try {
            Log.d("lyle", "Populating: userprof" + response); // for whatever reason, this (or some log statement) is necessary for the messages to appear
            JSONObject jsonObj = new JSONObject(response);
            JSONArray json = jsonObj.getJSONArray("titles");
            for (int i = 0; i < json.length(); ++i) {
                String title = json.getJSONObject(i).getString("mTitle");
               // String message = " ";
//                try {
//                    message = json.getJSONObject(i).getString("mBody");
//                } catch (final JSONException e) {
//                    Log.e("lyle", e.getMessage());
//                }
//                int netVotes = json.getJSONObject(i).getInt("mVote");
                titles.add(new Titles(title));
            }
        } catch (final JSONException e) {
            Log.d("lyle", "Error parsing JSON file: " + e.getMessage());
            if(e.getMessage().equals("No value for mTitle")) {
                Log.d("lyle", "NO DATA");
            }
            return;
        }
        Log.d("lyle", "Successfully parsed JSON file.");

        rv.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        rv.setAdapter(adapter);
    }

}
