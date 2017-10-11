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

import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by Kelli on 10/8/17.
 * Phase 3
 */

public class LoginActivity extends AppCompatActivity{
    String url = "https://sleepy-dusk-34987.herokuapp.com/users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((TextView) findViewById(R.id.username)).getText().toString();
                String password = ((TextView) findViewById(R.id.password)).getText().toString();
                if (username.equals("") || password.equals("")) {
                    findViewById(R.id.badLoginCreds).setVisibility(View.VISIBLE);
                } else {
                    Intent input = new Intent(getApplicationContext(), MainActivity.class);
                    input.putExtra("label_contents", "Login");
                    startActivityForResult(input, 789);


                }
            }
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

}
