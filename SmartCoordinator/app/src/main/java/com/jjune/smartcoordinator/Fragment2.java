package com.jjune.smartcoordinator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.Base64;

import org.json.JSONObject;
import static com.jjune.smartcoordinator.RealActivity.socket;
import static com.jjune.smartcoordinator.RealActivity.out;
import static com.jjune.smartcoordinator.RealActivity.input1;
import static com.jjune.smartcoordinator.RealActivity.bitmap;
import static com.jjune.smartcoordinator.LoginActivity.wide_email;

public class Fragment2 extends Fragment {

    Button btnCamera;
    ImageView imageView;
    String TAG = "MyTAG";
    Button btnregister;
    String ipaddr = "";
    Integer port;
    private Socket socket;
    private Handler mHandler ;
    EditText output;
    Spinner input;
    Spinner big;
    Spinner small;
    String enrollmsg;
    String email;
    Bitmap send_bitmap;
    String cloth_name;
    String cloth_sepbig;
    String cloth_sepsmall;
    Bundle recommend_bundle = new Bundle();
    Fragment frag3 = new Fragment3();

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d(TAG, "RESULT " + result.getResultCode());
                    if(result.getResultCode() == getActivity().RESULT_OK)
                    {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        send_bitmap = imageBitmap;
                        imageView.setImageBitmap(imageBitmap);
                        Log.d(TAG, "bittostr " + bitmapToString(imageBitmap));
                        Log.d(TAG, "bittostr " + imageBitmap.getClass().getName());

                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        String[] cloth1 = {"상의","아우터","바지","원피스","치마","신발"};
        View view = inflater.inflate(R.layout.fragment_2, container, false);

        Spinner clothspinner = (Spinner)view.findViewById(R.id.cloth_spin);
        ArrayAdapter clothadapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.cloth, android.R.layout.simple_spinner_item);
        clothadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clothspinner.setAdapter(clothadapter);

        clothspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view2, int i, long l) {
                switch (i)
                {
                    case 0:
                        Spinner clothspinner1 = (Spinner)view.findViewById(R.id.cloth_spin2);
                        Log.d(TAG, "spnieer " + clothspinner1);
                        Log.d(TAG, "int " + i);
                        ArrayAdapter clothadapter1 = ArrayAdapter.createFromResource(getContext(),R.array.cloth1, android.R.layout.simple_spinner_item);
                        clothadapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        clothspinner1.setAdapter(clothadapter1);
                        break;

                    case 1:
                        Spinner clothspinner2 = (Spinner)view.findViewById(R.id.cloth_spin2);
                        Log.d(TAG, "spnieer " + clothspinner2);
                        Log.d(TAG, "int " + i);
                        ArrayAdapter clothadapter2 = ArrayAdapter.createFromResource(getContext(),R.array.cloth2, android.R.layout.simple_spinner_item);
                        clothadapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        clothspinner2.setAdapter(clothadapter2);
                        break;

                    case 2:
                        Spinner clothspinner3 = (Spinner)view.findViewById(R.id.cloth_spin2);
                        ArrayAdapter clothadapter3 = ArrayAdapter.createFromResource(getContext(),R.array.cloth3, android.R.layout.simple_spinner_item);
                        clothadapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        clothspinner3.setAdapter(clothadapter3);
                        Log.d(TAG, "int " + i);
                        break;

                    case 3:
                        Spinner clothspinner4 = (Spinner)view.findViewById(R.id.cloth_spin2);
                        ArrayAdapter clothadapter4 = ArrayAdapter.createFromResource(getContext(),R.array.cloth4, android.R.layout.simple_spinner_item);
                        clothadapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        clothspinner4.setAdapter(clothadapter4);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        /*Spinner clothspinner2 = (Spinner)view.findViewById(R.id.cloth_spin2);
        ArrayAdapter clothadapter2 = ArrayAdapter.createFromResource(this.getActivity(),R.array.cloth2, android.R.layout.simple_spinner_item);
        clothadapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clothspinner2.setAdapter(clothadapter2);*/


        big = (Spinner)view.findViewById(R.id.cloth_spin);
        small = (Spinner)view.findViewById(R.id.cloth_spin2);

        btnregister = (Button)view.findViewById(R.id.register);
        btnCamera = (Button)view.findViewById(R.id.camera_button);
        imageView = (ImageView)view.findViewById(R.id.image);
        mHandler = new Handler();

        input = (Spinner)view.findViewById(R.id.cloth_spin);
        output = (EditText)view.findViewById(R.id.cloth_name);


        if(send_bitmap!=null)
            imageView.setImageBitmap(send_bitmap);
        if(getArguments()!=null)
        {
            Bundle bundle = getArguments();
            email = bundle.getString("email");
        }

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivity(i);
                //Log.d(TAG, "THREAD port " + "hi");

                launcher.launch(i);

            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recommend_bundle.putString("weather","1212");
                frag3.setArguments(recommend_bundle);
                ConnectThread th = new ConnectThread();
                th.start();
                //Log.d(TAG, "thread" + th.getId());

                Toast.makeText(getContext(), "옷 등록이 완료되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    @Override
    public void onStop(){
        super.onStop();
        /*try{
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }*/
    }

    class ConnectThread extends Thread{
        public void run(){
            try{
                JSONObject jsondata = new JSONObject();

                enrollmsg = "등록|";
                //jsondata.put("code","등록|");
                if(email!=null) {
                    jsondata.put("email", email);
                    enrollmsg = enrollmsg + email;
                }
                else
                {
                    enrollmsg = enrollmsg + wide_email;
                }

                if(send_bitmap!=null)
                {
                    jsondata.put("image",send_bitmap);
                    enrollmsg=enrollmsg +"^" + bitmapToString(send_bitmap);
                }

                jsondata.put("name",output.getText().toString());
                enrollmsg=enrollmsg +"^" + output.getText().toString();

                jsondata.put("sep_big",big.getSelectedItem().toString());
                enrollmsg=enrollmsg +"^" + big.getSelectedItem().toString();

                jsondata.put("sep_small",small.getSelectedItem().toString());
                enrollmsg=enrollmsg +"^" + small.getSelectedItem().toString();

                //socket = new Socket("192.168.152.133",10000);
                //Log.d(TAG, "socket1010" + socket);
                //Log.d(TAG, "sndmsg" + jsondata);

                //PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8")),true);
                Log.d(TAG, "sndmsg" + out);
                out.println(enrollmsg);
                Log.d(TAG, "realmsg" + enrollmsg);
                //BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                /*String read = input1.readLine();
                Log.d(TAG, "read" + read);

                recommend_bundle.putString("weather",read);
                frag3.setArguments(recommend_bundle);*/

                //mHandler.post(new msgUpdate(read));

                //socket.close();

            }catch(Exception e)
            {
                //Log.e(TAG, "ERROR" + e);
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

    public String bitmapToString(Bitmap bitmap){
        String image = "";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            image = Base64.getEncoder().encodeToString(byteArray);
        }
        return image;
    }

    public Bitmap stringToBitmap(String data){
        Bitmap bitmap = null;
        byte[] byteArray = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            byteArray = Base64.getDecoder().decode(data);
        }
        ByteArrayInputStream stream = new ByteArrayInputStream(byteArray);
        bitmap = BitmapFactory.decodeStream(stream);
        return bitmap;
    }
}