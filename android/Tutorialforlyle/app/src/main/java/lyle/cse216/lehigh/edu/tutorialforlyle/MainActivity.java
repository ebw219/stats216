package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ArrayList<Datum> mData = new ArrayList<>();
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
        adapter = new ItemListAdapter(this, mData);
        MySingleton list = MySingleton.getInstance(this.getApplicationContext());
        MySingleton.getInstance(this).addToRequestQueue(getResponse());


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

        adapter.setClickListener(new ItemListAdapter.ClickListener(){ //clicking on message
            @Override
            public void onClick(Datum d) {
                int id = d.mIndex;
                Log.d("lyle", "In click listener here");
                Intent i = new Intent(getApplicationContext(), CommentActivity.class);
                i.putExtra("id", id);
                startActivityForResult(i, 789);
            }
        });


        // Add the request to the RequestQueue
        Log.d("lyle", "request queue:" + MySingleton.getInstance(this).getRequestQueue());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
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
        try {
            Log.d("lyle", "Populating: " + response); // for whatever reason, this (or some log statement) is necessary for the messages to appear
            JSONObject jsonObj = new JSONObject(response);
            JSONArray json = jsonObj.getJSONArray("tblMessage");
            for (int i = 0; i < json.length(); ++i) {
                int message_id = json.getJSONObject(i).getInt("message_id");
                int user_id = json.getJSONObject(i).getInt("user_id");
                String title = json.getJSONObject(i).getString("title");
                String message = json.getJSONObject(i).getString("body");
                int votes = json.getJSONObject(i).getInt("mVote");
                mData.add(new Datum(message_id, user_id, title, message, votes));
            }
        } catch (final JSONException e) {
            Log.d("lyle", "Error parsing JSON file: " + e.getMessage());
            if(e.getMessage().equals("No value for mData")) {
                String title = "No Messages";
            }
            return;
        }
        Log.d("lyle", "Successfully parsed JSON file.");

        rv.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        rv.setAdapter(adapter);
    }



    /**
     * POST Volley method
     *
     * @param editText typed in data for new message
     * @return post request
     */
    protected StringRequest postRequest(final EditText editText) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("lyle", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("lyle", "That POST didn't work!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mTitle", editText.getText().toString());
                params.put("mMessage", editText.getText().toString());
                params.put("mVotes", editText.getText().toString());
                return params;
            }
        };
        return postRequest;
    }

//    static void sendPutRoute(int index, String voteType){
//        Log.d("lyle", "HERE");
//        StringRequest putRequest = new StringRequest(Request.Method.PUT, url + voteType + index, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("lyle", response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("lyle", "That PUT didn't work");
//            }
//        });
//        Context context = MySingleton.getContext();
//        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(putRequest);
//
//        rv.setAdapter(adapter);
//
//        runOnUiThread(new Runnable(){
//
//            @Override
//            public void run() {
//
//            }
//        })
//        adapter.notifyDataSetChanged();
//    }


//    public void likeMethod(View v){
//        Log.d("lyle", "WORKING");
//        final View.OnClickListener like = new View.OnClickListener() {
//            @Override
//            public void onClick(View view){
//                StringRequest putRequest = new StringRequest(Request.Method.PUT, url + "/upVote/" + d.mIndex, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("lyle", response);
//                        Log.d("lyle", "BUTTON PRESSED: " + d.mIndex);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("lyle", "That PUT didn't work");
//                    }
//                });
//
//            }
//        };
//    }



}
