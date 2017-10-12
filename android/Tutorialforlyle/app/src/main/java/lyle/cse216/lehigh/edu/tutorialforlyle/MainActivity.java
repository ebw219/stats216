package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Datum> mData = new ArrayList<>();
    ArrayList<UserInfo> mUsers = new ArrayList<>();
    static String url = "https://sleepy-dusk-34987.herokuapp.com/messages";
    static RecyclerView rv;
    static ItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("lyle", "Debug Message from onCreate");

        // Instantiate the RequestQueue
//        RequestQueue queue = Volley.newRequestQueue(this);
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        String url = "https://sleepy-dusk-34987.herokuapp.com/messages";

        rv = (RecyclerView) findViewById(R.id.datum_list_view);
        adapter = new ItemListAdapter(this, mData, mUsers);
        MySingleton list = MySingleton.getInstance(this.getApplicationContext());
        MySingleton.getInstance(this).addToRequestQueue(getResponse());

        MySingleton.getInstance(this).addToRequestQueue(getUsers());

        FloatingActionButton newMessage = findViewById(R.id.add);
        newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent input = new Intent(getApplicationContext(), NewMessageActivity.class);
                input.putExtra("label_contents", "Add new message");
                startActivityForResult(input, 789);
            }

        });

        findViewById(R.id.logoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent input = new Intent(getApplicationContext(), LoginActivity.class);
                input.putExtra("label_contents", "Logout");
                startActivityForResult(input, 789);
            }
        });

        adapter.setClickListener(new ItemListAdapter.ClickListener(){ //clicking on message to view comments
            @Override
            public void onClick(Datum d) {
                Intent i = new Intent(getApplicationContext(), CommentActivity.class);
                i.putExtra("title", d.mTitle);
                Log.d("lyle", "PUTTING TITLE: " + d.mTitle);
                i.putExtra("message", d.mMessage);
                startActivityForResult(i, 789);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.d("lyle", "RESULT");
        mData.clear();
        MySingleton.getInstance(this).addToRequestQueue(getResponse());
    }



    /**
     * GET Volley request
     * @return the request
     */
    protected StringRequest getResponse() {
        return (new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("lyle", response);
                        populateListFromVolley(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("lyle", "That Get didn't work!");
            }
        }));
    }


    /**
     * Helper method for GET function
     *
     * @param response string of JSON data obtained from Get
     */
    private void populateListFromVolley(String response) {
        Log.d("lyle", "HERE");
        try {
            Log.d("lyle", "Populating: " + response); // for whatever reason, this (or some log statement) is necessary for the messages to appear
            JSONObject jsonObj = new JSONObject(response);
            JSONArray json = jsonObj.getJSONArray("mData");
            for (int i = 0; i < json.length(); ++i) {
                int message_id = json.getJSONObject(i).getInt("mId");
                int user_id = json.getJSONObject(i).getInt("uId");
                String title = json.getJSONObject(i).getString("mTitle");
                String message = "";
                try {
                    message = json.getJSONObject(i).getString("mBody");
                } catch (final JSONException e) {

                }
//                int votes = json.getJSONObject(i).getInt("mVote");
                mData.add(new Datum(user_id, message_id, title, message));
            }
        } catch (final JSONException e) {
            Log.d("lyle", "Error parsing JSON file: " + e.getMessage());
            if(e.getMessage().equals("No value for mData")) {
                Log.d("lyle", "NO DATA");
            }
            return;
        }
        Log.d("lyle", "Successfully parsed JSON file.");

        rv.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        rv.setAdapter(adapter);
    }


    /**
     * GET Volley request
     * @return the request
     */
    protected StringRequest getUsers() {
        return (new StringRequest(Request.Method.GET, "https://sleepy-dusk-34987.herokuapp.com/users",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("lyle", response);
                        getUserInfo(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("lyle", "That Get didn't work!");
            }
        }));
    }

    /**
     * Helper method for GET function
     *
     * @param response string of JSON data obtained from Get
     */
    private void getUserInfo(String response) {
        Log.d("lyle", "HERE");
        try {
            Log.d("lyle", "Getting user info: " + response); // for whatever reason, this (or some log statement) is necessary for the messages to appear
            JSONObject jsonObj = new JSONObject(response);
            JSONArray json = jsonObj.getJSONArray("mData");
            for (int i = 0; i < json.length(); ++i) {
                String username = json.getJSONObject(i).getString("username");
                String realname = json.getJSONObject(i).getString("realname");
                int user_id = json.getJSONObject(i).getInt("uId");
                Log.d("lyle", "INFO: " + user_id + ", " + username + ", " + realname);
                mUsers.add(new UserInfo(user_id, username, realname));
            }
        } catch (final JSONException e) {
            Log.d("lyle", "Error parsing JSON file: " + e.getMessage());
            if(e.getMessage().equals("No value for mData")) {
                Log.d("lyle", "NO DATA");
            }
            return;
        }
        Log.d("lyle", "Successfully parsed JSON file.");

        rv.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        rv.setAdapter(adapter);
    }

    static String getUsernameById(ArrayList<UserInfo> mUsers, int id){
        Log.d("lyle", "LENGTH: " + mUsers.size() + " PASSED ID: " + id);
        for(int i = 0; i < mUsers.size(); i++){
            Log.d("lyle", "ID: " + mUsers.get(i).uId + " USERNAME: " + mUsers.get(i).username);
            if(mUsers.get(i).uId == id){
                return mUsers.get(i).username;
            }
        }
        return null;
    }

//    /**
//     * POST Volley method
//     *
//     * @param editText typed in data for new message
//     * @return post request
//     */
//    protected StringRequest postRequest(final EditText editText) {
//        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("lyle", response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("lyle", "That POST didn't work!");
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("mTitle", editText.getText().toString());
//                params.put("mBody", editText.getText().toString());
//                params.put("mVotes", editText.getText().toString());
//                return params;
//            }
//        };
//        return postRequest;
//    }



}
