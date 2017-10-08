package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kelli on 9/30/17.
 */

public class EditMessage extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("lyle", "Debug Message from onCreate");

        EditText mEdit = (EditText)findViewById(R.id.item);

        Log.d("lyle", mEdit.getText().toString());
//        EditText title = (EditText) findViewById(R.id.listItemTitle);
//        EditText message = (EditText) findViewById(R.id.listItemText);
//        message.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick (View view){
//                Intent input = new Intent(getApplicationContext(), NewMessageActivity.class);
//                input.putExtra("label_contents", "Add new message");
//                startActivityForResult(input, 789);
//
//                ArrayList<Datum> mData = MainActivity.getmData();
//
//                for(int i = 0; i < mData.size(); i++){
//                    if(mData.get(i).mIndex == this.mIndex){ //find the info of clicked obj
//
//                    }
//                }
//
//                Log.d("lyle", "Successfully parsed JSON file.");
//                RecyclerView rv = (RecyclerView) findViewById(R.id.datum_list_view);
////                rv.setLayoutManager(new LinearLayoutManager(this));
////                ItemListAdapter adapter = new ItemListAdapter(this, mData);
////                rv.setAdapter(adapter);
//
//
//
//            }
//
//        });


//        finish();
    }

}
