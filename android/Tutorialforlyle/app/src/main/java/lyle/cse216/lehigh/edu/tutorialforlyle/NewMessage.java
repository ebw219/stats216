package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

import lyle.cse216.lehigh.edu.tutorialforlyle.R;

public class NewMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_message);

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://sleepy-dusk-34987.herokuapp.com/messages";


        Button bCancel = (Button) findViewById(R.id.cancelButton);
        Button bSubmit = (Button) findViewById(R.id.submitButton);

        final EditText title = (EditText) findViewById(R.id.listItemTitle);
        final EditText message = (EditText) findViewById(R.id.listItemText);
        final TextView votes = (TextView) findViewById(R.id.listItemVotes);

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!title.getText().toString().equals("") && !message.getText().toString().equals("")) {
                    Intent i = new Intent();

                    StringRequest newReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("lyle", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("lyle", "That POST didn't work!");
                            Log.e("lyle", " If your vote is about 10 line up the log, " +
                                    "the message parsed correctly, but the server rejected it");
                        }
                    })
                    {
                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            HashMap<String, String> params2 = new HashMap<String, String>();
                            params2.put("mTitle", title.getText().toString());
//                            params2.put("mMessage", message.getText().toString());
//                            params2.put("mVote", votes.getText().toString()); // need to check this
                            Log.d("lyle", new JSONObject(params2).toString());
                            return new JSONObject(params2).toString().getBytes();
                        }

                    };
                    queue.add(newReq);

                    i.putExtra("result", title.getText().toString());
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

    }


}