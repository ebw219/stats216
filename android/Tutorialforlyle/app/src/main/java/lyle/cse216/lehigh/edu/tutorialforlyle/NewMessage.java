package lyle.cse216.lehigh.edu.tutorialforlyle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import lyle.cse216.lehigh.edu.tutorialforlyle.R;

public class NewMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_message);


        Button bCancel = (Button) findViewById(R.id.cancelButton);
        Button bSubmit = (Button) findViewById(R.id.submitButton);

        final EditText title = (EditText) findViewById(R.id.listItemTitle);
        final EditText message = (EditText) findViewById(R.id.listItemText);

        bSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (!title.getText().toString().equals("") && !message.getText().toString().equals("")) {
                    Intent i = new Intent();
                    i.putExtra("result", title.getText().toString());
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

//        // Get the parameter from the calling activity, and put it in the TextView
//        Intent input = getIntent();
//        String label_contents = input.getStringExtra("label_contents");
//        TextView tv = (TextView) findViewById(R.id.specialMessage);
//        tv.setText(label_contents);

//        // The OK button gets the text from the input box and returns it to the calling activity
//        final EditText et = (EditText) findViewById(R.id.editText);
//        Button bOk = (Button) findViewById(R.id.buttonOk);
//        bOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!et.getText().toString().equals("")) {
//                    Intent i   = new Intent();
//                    i.putExtra("result", et.getText().toString());
//                    setResult(Activity.RESULT_OK, i);
//                    finish();
//                }
//            }
//        });
//
//        // The Cancel button returns to the caller without sending any data
//        Button bCancel = (Button) findViewById(R.id.buttonCancel);
//        bCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setResult(Activity.RESULT_CANCELED);
//                finish();
//            }
//        });
    }

}