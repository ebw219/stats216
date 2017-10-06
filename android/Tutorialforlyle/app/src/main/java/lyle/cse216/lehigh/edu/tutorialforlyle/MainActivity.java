package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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
import java.util.List;
import java.util.Map;

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

        rv = (RecyclerView) findViewById(R.id.datum_list_view);
        adapter = new ItemListAdapter(this, mData);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
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
        });
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
