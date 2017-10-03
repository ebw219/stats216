package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class mySingleton {
    ArrayList<Datum> mData = new ArrayList<>();
    RequestQueue queue;
    String url;
    Context context;
    RecyclerView rv;
    ItemListAdapter adapter;

    /**
     * Singleton method of immense technical debt
     * @param appQueue - requestqueue for Volley functions
     * @param appUrl url for backend
     * @param appData arraylist of posts
     * @param volleyContext context
     * @param rView recycler view used by main activity
     * @param listAdapter adapter for data to list
     */
    mySingleton(RequestQueue appQueue, String appUrl, ArrayList<Datum> appData,
                 Context volleyContext, RecyclerView rView, ItemListAdapter listAdapter){
        queue = appQueue;
        url = appUrl;
        mData = appData;
        context = volleyContext;
        rv = rView;
        adapter = listAdapter;

    }
    mySingleton(RequestQueue appQueue, String appUrl, ArrayList<Datum> appData, ItemListAdapter listAdapter){
        queue = appQueue;
        url = appUrl;
        mData = appData;
        adapter = listAdapter;

    }

    /**
     * Helper method for GET function
     * @param response string of JSON data obtained from Get
     */
    private void populateListFromVolley(String response){
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
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
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
     * POST Volley method
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
                Log.d("lyle", params.toString() + " message");
                return params;
            }
        };
        return postRequest;
    }

    /**
     * Put Volley method
     * @param d - Datum of post being upvoted
     * @return upvote request
     */
        protected StringRequest putRequest(Datum d) {
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url +"/" + d.mIndex,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("lyle", "That put didn't work!");
                        Log.d("lyle", " If your vote is about 10 line up the log, " +
                                "the vote parsed correctly, but the server rejected it");
                    }
                }
        ) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                HashMap<String, String> params2 = new HashMap<String, String>();
//                params2.put("mVote", "up");
                Log.d("lyle", new JSONObject(params2).toString());
                return new JSONObject(params2).toString().getBytes();
            }
            
        };

        return (putRequest);
        }
    }
