package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by Kelli on 10/8/17.
 * Phase 3
 */

public class LoginActivity extends AppCompatActivity{
    String url = "https://sleepy-dusk-34987.herokuapp.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("lyle", "CLICKED LOGIN");

                final String username = ((TextView) findViewById(R.id.username)).getText().toString();
                String password = ((TextView) findViewById(R.id.password)).getText().toString();
                StringRequest login = new StringRequest(Request.Method.POST, url + "login/" + username + "/" + password,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("lyle", "LOGIN ATTEMPT");
                                Log.d("lyle", response);
                                if(loginSuccess(response)) {
                                    Intent input = new Intent(getApplicationContext(), MainActivity.class);
                                    input.putExtra("label_contents", "Login");
                                    input.putExtra("username", username);
                                    input.putExtra("rand_val", randVal(response));
                                    startActivityForResult(input, 123);
                                } else {
                                    findViewById(R.id.badLoginCreds).setVisibility(View.VISIBLE);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { //if mstatus is error
                        findViewById(R.id.badLoginCreds).setVisibility(View.VISIBLE);
                        Log.e("lyle", "Bad user creds???");
                        Log.e("lyle", "That POST didn't work");
                    }
                });
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(login);
            }
//            }
        });


        findViewById(R.id.createUserButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent input = new Intent(getApplicationContext(), CreateUserActivity.class);
                input.putExtra("label_contents", "Create user");
                startActivityForResult(input, 789);
            }
        });


        findViewById(R.id.badLoginCreds).setVisibility(View.INVISIBLE);

        Intent i = getIntent();
        Log.d("lyle", "label_contents");
        if (i.getStringExtra("label_contents") != null) {
            if (i.getStringExtra("label_contents").equals("Make new user"))
                findViewById(R.id.successCreate).setVisibility(View.VISIBLE);
        }
    }

    String randVal(String response){
        String randVal = new String();
        try{
            JSONObject jsonObj = new JSONObject(response);
            randVal = jsonObj.get("mMessage").toString();
        } catch (final JSONException e) {
            Log.e("lyle", e.getMessage());
        }
        return randVal;
    }

    boolean loginSuccess(String response){
        try {
            Log.d("lyle", "Checking: " + response); // for whatever reason, this (or some log statement) is necessary for the messages to appear
            JSONObject jsonObj = new JSONObject(response);
//            JSONArray json = jsonObj.getJSONArray("mStatus");
//            JSONObject jsonObj = new JSONObject("mStatus");
            if(jsonObj.get("mStatus").equals("ok"))
                return true;
        } catch (final JSONException e) {
            Log.d("lyle", "Error parsing JSON file: " + e.getMessage());
            if(e.getMessage().equals("No value for mData")) {
                Log.d("lyle", "NO DATA");
            }
        }
        return false;
    }

}