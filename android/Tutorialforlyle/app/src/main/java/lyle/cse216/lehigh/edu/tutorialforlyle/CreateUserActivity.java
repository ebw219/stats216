package lyle.cse216.lehigh.edu.tutorialforlyle;

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


public class CreateUserActivity extends AppCompatActivity {
String url = "https://sleepy-dusk-34987.herokuapp.com/users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);

        findViewById(R.id.cancelCreate).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent input = new Intent(getApplicationContext(), LoginActivity.class);
                input.putExtra("label_contents", "Cancel");
                startActivityForResult(input, 789);
            }
        });

        findViewById(R.id.submitCreate).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String username = ((TextView) findViewById(R.id.newUsername)).getText().toString();
                String realName = ((TextView) findViewById(R.id.newName)).getText().toString();
                String email = ((TextView) findViewById(R.id.newEmail)).getText().toString();
                String password = ((TextView) findViewById(R.id.newPassword)).getText().toString();
                String confirmPassword = ((TextView) findViewById(R.id.confirmNewPassword)).getText().toString();

                if(!(password.equals(confirmPassword))){
                    findViewById(R.id.badPassword).setVisibility(View.VISIBLE);
                } else if(username.isEmpty() || realName.isEmpty() || email.isEmpty() || password.isEmpty()){
                    findViewById(R.id.invalidField).setVisibility(View.VISIBLE);
                } else {
                    StringRequest newReq = new StringRequest(Request.Method.POST, url + "/" + username + "/" + realName + "/" + email + "/" + password, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("lyle", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("lyle", "That POST didn't work!");
                            Log.e("lyle", " If your sendPutRoute is about 10 line up the log, " +
                                    "the message parsed correctly, but the server rejected it");
                        }
                    })
                    {
//                        @Override
//                        public byte[] getBody() throws AuthFailureError {
//                            HashMap<String, String> params = new HashMap<String, String>();
//                            params.put("email", ((TextView) findViewById(R.id.newEmail)).getText().toString());
//                            params.put("username", ((TextView) findViewById(R.id.newUsername)).getText().toString());
//                            params.put("realname", ((TextView) findViewById(R.id.newName)).getText().toString());
//                            params.put("password", ((TextView) findViewById(R.id.newPassword)).getText().toString());
//                            Log.d("lyle", new JSONObject(params).toString());
//                            return new JSONObject(params).toString().getBytes();
//                        }

                    };
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(newReq);
                    //POST request for new user
                    Intent input = new Intent(getApplicationContext(), LoginActivity.class);
                    input.putExtra("label_contents", "Make new user");
                    startActivityForResult(input, 789);
                }
            }
        });
    }
}