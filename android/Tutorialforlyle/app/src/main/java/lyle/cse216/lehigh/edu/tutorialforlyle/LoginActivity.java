package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * Created by Kelli on 10/8/17.
 * Phase 3
 */

public class LoginActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);



        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String username = ((TextView)findViewById(R.id.email)).getText().toString();
                String password = ((TextView)findViewById(R.id.password)).getText().toString();
                if(username.equals("") || password.equals("")){
                    findViewById(R.id.badLoginCreds).setVisibility(View.VISIBLE);
                } else {
                    Intent input = new Intent(getApplicationContext(), MainActivity.class);
                    input.putExtra("label_contents", "Login");
                    startActivityForResult(input, 789);
                }
            }
        });

        findViewById(R.id.badLoginCreds).setVisibility(View.INVISIBLE);

    }

}
