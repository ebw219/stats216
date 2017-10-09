package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Kelli on 10/8/17.
 * Phase 3
 */

public class CreateUserActivity extends AppCompatActivity {
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
    }
}