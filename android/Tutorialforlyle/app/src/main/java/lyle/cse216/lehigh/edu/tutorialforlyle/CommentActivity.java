package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kelli on 10/6/17.
 * Phase 3
 */

public class CommentActivity extends AppCompatActivity {
    RecyclerView rv;
    RecyclerView.Adapter adapter;
    ArrayList<String> mComments = new ArrayList<>();

    String url = "https://sleepy-dusk-34987.herokuapp.com/messages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_item);


        rv = (RecyclerView) findViewById(R.id.datum_list_view);
        adapter = new CommentListAdapter(this, mComments);


        /**
         * Helper method for GET function
         *
         * @param response string of JSON data obtained from Get
         */
    }

    private void populateListFromVolley(String response) {
        try {
            Log.d("lyle", "Populating: " + response); // for whatever reason, this (or some log statement) is necessary for the messages to appear
            JSONObject jsonObj = new JSONObject(response);
            JSONArray json = jsonObj.getJSONArray("tblComments");
            for (int i = 0; i < json.length(); ++i) {
//                int id = json.getJSONObject(i).getInt("mId");
//                String title = json.getJSONObject(i).getString("mTitle");
                String message = json.getJSONObject(i).getString("comment_text");
//                int votes = json.getJSONObject(i).getInt("mVote");
//                mComments.add(new Datum(id, title, message, votes));
                mComments.add(message);
            }
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