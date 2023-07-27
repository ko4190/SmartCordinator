package com.jjune.smartcoordinator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.main_button);
        final EditText editText_ip = findViewById(R.id.edit_ip);
        final EditText editText_port = findViewById(R.id.edit_port);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                String IP = editText_ip.getText().toString();
                String Port = editText_port.getText().toString();
                intent.putExtra("IP",IP);
                intent.putExtra("Port",Port);
                startActivity(intent);
            }
        });
    }
}