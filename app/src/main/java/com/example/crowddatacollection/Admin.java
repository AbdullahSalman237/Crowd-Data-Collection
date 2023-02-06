package com.example.crowddatacollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Button lights = findViewById(R.id.lights);
        Button busStop = findViewById(R.id.bus_stop);
        Button sewage = findViewById(R.id.sewage);



        lights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity on button click
                Intent i = new Intent(Admin.this,Activity_Details.class);
                i.putExtra("message_key", "street_lights");
                startActivity(i);
            }
        });
        busStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity on button click
                Intent i = new Intent(Admin.this,Activity_Details.class);
                i.putExtra("message_key", "bus_stop");
                startActivity(i);
            }
        });
        sewage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity on button click
                Intent i = new Intent(Admin.this,Activity_Details.class);
                i.putExtra("message_key", "sewage");
                startActivity(i);
            }
        });

    }
}