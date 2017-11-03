package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Kelli on 10/8/17.
 * Phase 3
 */


public class LoginActivity extends AppCompatActivity{
    String username;
    String url = "https://lyle-buzz.herokuapp.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        Log.d("lyle", "SHARED PREF: " + SaveSharedPreference.getUserName(LoginActivity.this));

        if(SaveSharedPreference.getUserName(LoginActivity.this).length() == 0) {
            Intent input = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(input, 789);
        }
        else {
            // Stay at the current activity.
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("lyle", "CLICKED LOGIN");



                username = ((TextView) findViewById(R.id.username)).getText().toString();
                String password = ((TextView) findViewById(R.id.password)).getText().toString();
                Intent loginButton = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(loginButton, 123);

//                StringRequest login = new StringRequest(Request.Method.POST, url + "login/" + username + "/" + password,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Log.d("lyle", "LOGIN ATTEMPT");
//                                Log.d("lyle", response);
//                                if(loginSuccess(response)) {
//                                    Intent input = new Intent(getApplicationContext(), MainActivity.class);
//                                    input.putExtra("label_contents", "Login");
//                                    input.putExtra("username", username);
//                                    input.putExtra("rand_val", randVal(response));
//                                    SaveSharedPreference.setUserName(getApplicationContext(), username);
//                                    SaveSharedPreference.setRandVal(getApplicationContext(), randVal(response));
//                                    startActivityForResult(input, 123);
//                                } else {
//                                    findViewById(R.id.badLoginCreds).setVisibility(View.VISIBLE);
//                                }
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) { //if mstatus is error
//                        findViewById(R.id.badLoginCreds).setVisibility(View.VISIBLE);
//                        Log.e("lyle", "Bad user creds???");
//                        Log.e("lyle", "That POST didn't work");
//                    }
//                });
//                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(login);
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