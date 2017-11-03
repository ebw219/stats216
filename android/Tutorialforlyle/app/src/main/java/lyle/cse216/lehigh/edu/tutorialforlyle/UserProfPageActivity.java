package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static lyle.cse216.lehigh.edu.tutorialforlyle.MainActivity.getUsernameById;

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


        Intent viewProfile = getIntent();
        String profusername = viewProfile.getStringExtra("username");
        Log.d("lyle", "getting username " + profusername);

        //String tempUsername = viewProfile.getStringExtra("username");
        Log.d("lyle", "username on page: " + profusername);
        ((TextView)findViewById(R.id.profileUsername)).setText(profusername);

    }


}
