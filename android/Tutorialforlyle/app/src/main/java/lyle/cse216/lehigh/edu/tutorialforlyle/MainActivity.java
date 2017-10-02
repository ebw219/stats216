package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
//import static com.sun.xml.internal.ws.api.message.Packet.Status.Request;
//import static com.sun.xml.internal.ws.api.message.Packet.Status.Response;

public class MainActivity extends AppCompatActivity {

    mySingleton list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("lyle", "Debug Message from onCreate");

        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://sleepy-dusk-34987.herokuapp.com/messages";


        RecyclerView rv = (RecyclerView) findViewById(R.id.datum_list_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        ItemListAdapter adapter = new ItemListAdapter(this, mData);
        rv.setAdapter(adapter);

        list = new mySingleton(queue, url, mData, this, rv, adapter);
        StringRequest stringRequest1 = list.getResponse();

        FloatingActionButton newMessage = (FloatingActionButton) findViewById(R.id.add);
        newMessage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                Intent input = new Intent(getApplicationContext(), NewMessage.class);
                input.putExtra("label_contents", "Add new message");
                startActivityForResult(input, 789);

            }

        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 789) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                final TextView msg = (TextView) findViewById(R.id.bEdit);
                msg.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick (View view){
                        Intent input = new Intent(getApplicationContext(), EditMessage.class);
                        input.putExtra("label_contents", "Edit message");
                        startActivityForResult(input, 789);
                    }

                });

            }
        }
    }

    public int upvote(){
        return 1;
    }

    /**
     * mData holds the data we get from Volley
     */
    static ArrayList<Datum> mData = new ArrayList<>();

    public static ArrayList<Datum> getmData(){
        return mData;
    }
}