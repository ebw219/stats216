package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kelli on 10/6/17.
 * Phase 3
 */

public class CommentActivity extends AppCompatActivity {
    RecyclerView rv;
    RecyclerView.Adapter adapter;
    ArrayList<String> mComments = new ArrayList<>();

    String url = "https://lyle-buzz.herokuapp.com/messages/comments/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_main);


        rv = (RecyclerView) findViewById(R.id.datum_list_view);
        adapter = new CommentListAdapter(this, mComments);

        Intent viewComments = getIntent();
        int id = viewComments.getIntExtra("id", -1);
        Log.d("lyle", "ID: " + id);

        String tempTitle = viewComments.getStringExtra("title");
        Log.d("lyle", "title: " + tempTitle);
        ((TextView)findViewById(R.id.commentItemTitle)).setText(tempTitle);

        String tempMsg = viewComments.getStringExtra("message");
        Log.d("lyle", "msg: " + tempMsg);
        ((TextView)findViewById(R.id.commentItemMessage)).setText(tempMsg);


        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(getResponse());
    }


    /**
     * Helper method for GET function
     * @param response string of JSON data obtained from Get
     */
    private void populateListFromVolley(String response) {
        try {
            Log.d("lyle", "Populating comments: " + response); // for whatever reason, this (or some log statement) is necessary for the messages to appear
            JSONObject jsonObj = new JSONObject(response);
            JSONArray json = jsonObj.getJSONArray("mData");
            for (int i = 0; i < json.length(); ++i) {
//                int cid = json.getJSONObject(i).getInt("cId");
//                int uId = json.getJSONObject(i).getInt("uId");
//                int mId = json.getJSONObject(i).getInt("mId");
                String message = json.getJSONObject(i).getString("mCom");
//                String message = json.getString("mCom");
                mComments.add(message);
                Log.d("lyle", message);
            }
//            }
        } catch (final JSONException e) {
            Log.d("lyle", "Error parsing JSON file: " + e.getMessage());
            return;
        }
        Log.d("lyle", "Successfully parsed JSON file.");

        rv.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        rv.setAdapter(adapter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mComments.clear();
        MySingleton.getInstance(this).addToRequestQueue(getResponse());
    }


    /**
     * GET Volley request
     * @return the request
     */
    protected StringRequest getResponse() {
        Intent viewComments = getIntent();
        final int id = viewComments.getIntExtra("id", -1);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("lyle", "RESPONSE: " + response);
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


}