package com.jjune.smartcoordinator;

import static com.gun0912.tedpermission.TedPermission.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Locale;



public class RealActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    Fragment fragment_enroll;
    Fragment fragment_closet;
    Fragment fragment_recommend;
    String email;
    String txtResult;
    Bundle bundle;
    Bundle bundle1;
    Bundle bundle2;
    String txtResult2;
    String TAG = "MyTAG";
    String userinfo;

    public static Socket socket;
    public static Handler mHandler ;
    public static PrintWriter out;
    public static BufferedReader input1;
    public static Bitmap bitmap;

    String weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);


        // 프래그먼트 시작
        fragment_closet = new Fragment1();
        fragment_enroll = new Fragment2();
        fragment_recommend = new Fragment3();

        mHandler = new Handler();

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        bottomNavigationView = findViewById(R.id.navigation);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        userinfo = intent.getStringExtra("userinfo");
        /*Spinner clothspinner = (Spinner)findViewById(R.id.cloth_spin);
        ArrayAdapter clothadapter = ArrayAdapter.createFromResource(this,R.array.cloth, android.R.layout.simple_spinner_item);
        clothadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clothspinner.setAdapter(clothadapter);*/


        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragment_recommend).commit();

        // 네비게이션바 생성
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.main1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,fragment_closet).commit();
                        break;
                    case R.id.main2:
                        fragment_enroll = new Fragment2();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,fragment_enroll).commit();
                        break;
                    case R.id.main3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,fragment_recommend).commit();
                        break;

                }
                return true;
            }
        });

        if(Looper.myLooper() == Looper.getMainLooper())
        {
            Log.e(TAG, "ERROR" + "/TRUE");
            Log.e(TAG, "ERROR" + Thread.currentThread().getId());
        }


        bundle1 = new Bundle();
        bundle1.putString("email",email);
        fragment_enroll.setArguments(bundle1);

        // 위치정보 받아오기
        final LocationManager lm = (LocationManager)getSystemService(getApplicationContext().LOCATION_SERVICE);

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( RealActivity.this, new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 0 );
        }
        else {
            // 가장최근 위치정보 가져오기
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                String provider = location.getProvider();
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                double altitude = location.getAltitude();

                txtResult = "위치정보 : " + provider + " " +
                        "위도 : " + longitude + " "  +
                        "경도 : " + latitude + " " +
                        "고도  : " + altitude;
                bundle = new Bundle();
                //bundle1 = new Bundle();
                bundle2 = new Bundle();

                txtResult2 = longitude + "|" + latitude;
                //txtResult2 = "128.75391" + "|" + "35.83609";
                if(userinfo!=null)
                    txtResult2 = txtResult2 + "|" + userinfo;
                else
                {
                    if(email!=null)
                        txtResult2 = txtResult2 + "|" + email + "^";
                }
                bundle.putString("location",txtResult);
                //bundle1.putString("email",email);
                bundle2.putString("email",email);

                //fragment_enroll.setArguments(bundle1);
                fragment_closet.setArguments(bundle2);


                Log.d(TAG, "location " + txtResult);
                ConnectThread th = new ConnectThread();
                th.start();

                Log.d(TAG, "thread" + Thread.currentThread().getId());
                Log.d(TAG, "thread" + th.getId());
                try {
                    Thread.sleep(2000); //1초 대기

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                /*try{
                    weather = input1.readLine();
                } catch (Exception e)
                {

                }*/
                try {
                    th.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d(TAG, "eerror" + e);
                }

                Log.d(TAG, "socket22" + socket);
                Log.d(TAG, "printout" + out);

                bundle.putString("weather",weather);

                Log.d(TAG, "bundle2 " + bundle);
                fragment_recommend.setArguments(bundle);
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
        }


    }

    final LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();
            txtResult = "위치정보 : " + provider + " " +
                    "위도 : " + longitude + " "  +
                    "경도 : " + latitude + " " +
                    "고도  : " + altitude;
            bundle = new Bundle();
            bundle = new Bundle();
            bundle.putString("location",txtResult);
            bundle.putString("weather",weather);
            fragment_recommend.setArguments(bundle);
            Log.d(TAG, "location " + txtResult);

        }
        public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };

    class ConnectThread extends Thread{
        public void run(){
            try{

                socket = new Socket("192.168.90.133",10000);
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8")),true);
                //out.println(txtResult2);
                String txt = "안녕하세요";
                String txt2 = new String(txt.getBytes("UTF-8"),"MS949");
                out.println(txtResult2);
                input1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String read = input1.readLine();
                Log.d(TAG, "socket12" + out);

                weather = read;

                //socket.close();

            }catch(Exception e)
            {
                Log.e(TAG, "ERROR" + e);
                e.printStackTrace();
            }
        }
    }
}