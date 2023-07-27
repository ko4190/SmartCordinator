package com.jjune.smartcoordinator;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.IOException;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;


public class MainActivity2 extends AppCompatActivity {
    Address address;
    private Socket socket;
    PrintWriter out; // 데이터 전송
    BufferedReader in; // 데이터 수신
    TextView output;
    EditText input;
    String data;
    String TAG = "MyTAG";
    String IP;
    Integer Port;
    private Handler mHandler ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        Intent intent = getIntent();
        IP = intent.getStringExtra("IP");
        String Port2 = intent.getStringExtra("Port");
        Port = Integer.parseInt(Port2);
        output = findViewById(R.id.main2_text);
        input = findViewById(R.id.main2_edit);
        Button button = findViewById(R.id.main2_button);
        mHandler = new Handler();

        Log.d(TAG, "IP" + IP);
        Log.d(TAG, "PORT" + Port);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input.getText().toString()!=null||!input.getText().toString().equals("")) {
                    Log.d(TAG, "DATA" + input.getText().toString());
                    ConnectThread th = new ConnectThread();
                    th.start();
                }
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
        try{
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    class ConnectThread extends Thread{
        public void run(){
            try{
                InetAddress serverAddr = InetAddress.getByName(IP);
                Log.d(TAG, "THREAD ip " + IP);
                Log.d(TAG, "THREAD ad " + serverAddr);
                Log.d(TAG, "THREAD port " + Port);
                socket = new Socket(serverAddr,Port);
                String sndMsg = input.getText().toString();
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
                out.println(sndMsg);
                BufferedReader input = new BufferedReader((new InputStreamReader(socket.getInputStream())));
                String read = input.readLine();

                mHandler.post(new msgUpdate(read));
                socket.close();

            }catch(Exception e)
            {
                Log.e(TAG, "ERROR" + e);
                e.printStackTrace();
            }
        }
    }

    class msgUpdate implements Runnable{
        private String msg;
        public msgUpdate(String str){
            this.msg = str;
        }
        public void run(){
           output.setText(output.getText().toString() + msg + "\n");
        }
    };
}