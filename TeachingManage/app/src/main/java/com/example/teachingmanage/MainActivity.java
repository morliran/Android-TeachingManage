package com.example.teachingmanage;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button b1;
    EditText usr,pswd;
    TextView bytes;
    myDbAdapter myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myHelper = new myDbAdapter(this);
        bytes = (TextView)findViewById(R.id.bytes);
        usr = (EditText)findViewById(R.id.usrusr);
        pswd = (EditText)findViewById(R.id.passwrd);
        b1 = (Button)findViewById(R.id.logiin);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),"fonts/Lato-Light.ttf");
        bytes.setTypeface(custom_font);
        pswd.setTypeface(custom_font);
        b1.setTypeface(custom_font);
        usr.setTypeface(custom_font);
        usr.setText("");
        pswd.setText("");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usr.getText().toString().equals("admin") && pswd.getText().toString().equals("admin"))
                {
                    Toast.makeText(getApplicationContext(), "Welcome admin :-)", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainSystem.class);
                    startActivity(i);
                    finish();
                }
                else if(!usr.getText().toString().equals("admin") && pswd.getText().toString().equals("admin"))
                {
                    Toast.makeText(getApplicationContext(), "Invalided Username", Toast.LENGTH_SHORT).show();
                    usr.setText("");
                }
                else if(usr.getText().toString().equals("admin") && !pswd.getText().toString().equals("admin"))
                {
                    Toast.makeText(getApplicationContext(), "Invalided Password", Toast.LENGTH_SHORT).show();
                    pswd.setText("");
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Invalided Username and Password", Toast.LENGTH_SHORT).show();
                    usr.setText("");
                    pswd.setText("");
                }
            }
        });
    }
}
