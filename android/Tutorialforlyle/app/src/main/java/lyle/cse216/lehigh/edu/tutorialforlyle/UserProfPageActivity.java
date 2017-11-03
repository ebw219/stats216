package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by emyweston on 11/2/17.
 * Phase 4
 */

public class UserProfPageActivity extends AppCompatActivity {

    String url = "https://lyle-buzz.herokuapp.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

//        findViewById(R.id.viewUsername).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("lyle", "CLICKED USERNAME");
//
//
//
//               // username = ((TextView) findViewById(R.id.username)).getText().toString();
//            }
//
//        }
    }
}
