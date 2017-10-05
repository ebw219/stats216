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
//import static com.sun.xml.internal.ws.api.message.Packet.Status.Request;
//import static com.sun.xml.internal.ws.api.message.Packet.Status.Response;

public class MainActivity extends AppCompatActivity {


    ArrayList<Datum> mData = new ArrayList<>();
    String url = "https://sleepy-dusk-34987.herokuapp.com/messages";
    RecyclerView rv;
    ItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("lyle", "Debug Message from onCreate");

        // Instantiate the RequestQueue
//        RequestQueue queue = Volley.newRequestQueue(this);
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        String url = "https://sleepy-dusk-34987.herokuapp.com/messages";

        RecyclerView rv = (RecyclerView) findViewById(R.id.datum_list_view);
        adapter = new ItemListAdapter(this, mData);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        MySingleton list = MySingleton.getInstance(this.getApplicationContext());
        MySingleton.getInstance(this).addToRequestQueue(getResponse());



        FloatingActionButton newMessage = (FloatingActionButton) findViewById(R.id.add);
        newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent input = new Intent(getApplicationContext(), NewMessage.class);
                input.putExtra("label_contents", "Add new message");
                startActivityForResult(input, 789);

            }

        });


        // Add the request to the RequestQueue.
//        queue.add(getResponse());

        Log.d("lyle", "request queue:" + MySingleton.getInstance(this).getRequestQueue());
    }


    /**
     * GET Volley request
     *
     * @return the request
     */
    protected StringRequest getResponse() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("lyle", response);
                        populateListFromVolley(response);
                        Log.d("lyle", "done here");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("lyle", "That Get didn't work!");
            }
        });
        Log.d("lyle", "WHERE'S THE ERROR?");
        return stringRequest;
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
            JSONArray json = jsonObj.getJSONArray("mData");
            for (int i = 0; i < json.length(); ++i) {
                int id = json.getJSONObject(i).getInt("mId");
                String title = json.getJSONObject(i).getString("mTitle");
                String message = json.getJSONObject(i).getString("mMessage");
                int votes = json.getJSONObject(i).getInt("mVote");
                mData.add(new Datum(id, title, message, votes));
            }
        } catch (final JSONException e) {
            Log.d("lyle", "Error parsing JSON file: " + e.getMessage());
            return;
        }
        Log.d("lyle", "Successfully parsed JSON file.");

//        rv.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
//        rv.setAdapter(adapter);
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

//    /**
//     * Put Volley method
//     *
//     * @param d - Datum of post being upvoted
//     * @return upvote request
//     */
//    protected StringRequest putRequest(Datum d) {
//        StringRequest putRequest = new StringRequest(Request.Method.PUT, url + "/" + d.mIndex,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("Response", response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("lyle", "That put didn't work!");
//                        Log.d("lyle", " If your vote is about 10 line up the log, " +
//                                "the vote parsed correctly, but the server rejected it");
//                    }
//                }
//        ) {
//
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                HashMap<String, String> params2 = new HashMap<String, String>();
//                Log.d("lyle", new JSONObject(params2).toString());
//                return new JSONObject(params2).toString().getBytes();
//            }
//
//        };
//
//        return (putRequest);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Check which request we're responding to
//        if (requestCode == 789) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
////                final TextView msg = (TextView) findViewById(R.id.bEdit);
////                msg.setOnClickListener(new View.OnClickListener()
//
//                {
//                    @Override
//                    public void onClick (View view){
//                        Intent input = new Intent(getApplicationContext(), EditMessage.class);
//                        input.putExtra("label_contents", "Edit message");
//                        startActivityForResult(input, 789);
//                    }
//
//                });
//
//            }
//        }
//    }
//
//    public int upvote() {
//        return 1;
//    }



}
