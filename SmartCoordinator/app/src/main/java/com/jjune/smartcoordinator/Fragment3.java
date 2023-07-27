package com.jjune.smartcoordinator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.app.ActivityCompat;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.time.LocalTime;
import java.util.Random;

import static com.jjune.smartcoordinator.LoginActivity.wide_email;
import static com.jjune.smartcoordinator.RealActivity.out;
import static com.jjune.smartcoordinator.RealActivity.input1;
import androidx.appcompat.app.AppCompatActivity;
import static com.jjune.smartcoordinator.RealActivity.socket;
import static com.jjune.smartcoordinator.RealActivity.out;

public class Fragment3 extends Fragment {

    private static final String TAG = "mytag";
    String location_str;
    TextView locatetxt;
    TextView locatetxt2;
    String weather;
    private Socket socket;
    private Handler mHandler ;
    String[] weather_arr;
    String present_location;
    List<String> weather_arr_slice;
    String[] stt;
    Button reco;
    String receive_top;
    ImageView im_top;
    ImageView im_bottom;
    ImageView im_outer;
    ImageView im_etc;
    TextView text_top;
    TextView text_bottom;
    TextView text_outer;
    TextView text_etc;
    Button feed_hot;
    Button feed_cold;
    Button feed_good;
    String status;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_3, container, false);
        //ConnectThread th = new ConnectThread();
        //th.start();
        weather_arr_slice = new ArrayList<>();
        reco= view.findViewById(R.id.receive_recommend);

        im_top = (ImageView) view.findViewById(R.id.im_top);
        im_bottom = (ImageView) view.findViewById(R.id.im_bottom);
        im_outer = (ImageView) view.findViewById(R.id.im_outer);
        im_etc = (ImageView) view.findViewById(R.id.im_etc);

        text_top = (TextView) view.findViewById(R.id.text_top);
        text_bottom = (TextView) view.findViewById(R.id.text_bottom);
        text_outer = (TextView) view.findViewById(R.id.text_outer);
        text_etc = (TextView) view.findViewById(R.id.text_etc);

        feed_cold = (Button)view.findViewById(R.id.but_cold);
        feed_hot = (Button)view.findViewById(R.id.but_hot);
        feed_good = (Button)view.findViewById(R.id.but_good);

        feed_cold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "cold";
                ConnectThread_feedback th = new ConnectThread_feedback();
                th.start();
                Toast.makeText(getContext(), "'현재 추천조합에 대한 피드백 : 추움' 이 적용되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        feed_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "hot";
                ConnectThread_feedback th1 = new ConnectThread_feedback();
                th1.start();
                Toast.makeText(getContext(), "'현재 추천조합에 대한 피드백 : 더움' 이 적용되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        feed_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "good";
                ConnectThread_feedback th2 = new ConnectThread_feedback();
                th2.start();
                Toast.makeText(getContext(), "'현재 추천조합에 대한 피드백 : 적당' 이 적용되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });


        reco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "reco" + ":: 11");
                ConnectThread th6 = new ConnectThread();
                th6.start();
                try {
                    th6.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String[] string_bitmap = new String[0];
                String[] string_topname = new String[0];
                String[] string_topsep = new String[0];
                String[] string_topsepsep = new String[0];
                Bitmap[] bit_bitmap = new Bitmap[0];

                if(receive_top != null)
                {
                    String[] receive_top2 = receive_top.split("\\^");
                    List<String> str_top3 = new ArrayList<>();
                    List<String> str_topname = new ArrayList<>();
                    List<String> str_bitmap = new ArrayList<>();
                    List<String> str_topsep = new ArrayList<>();
                    List<String> str_topsmallsep = new ArrayList<>();
                    //String[] string_bitmap;
                    //String[] string_topname;
                    //String[] string_topsep;
                    Log.d(TAG, "retop" + receive_top2);
                    Log.d(TAG, "retop" + receive_top2.length);

                    for(int j=0; j<receive_top2.length;j++)
                    {
                        String[] imp = new String[receive_top2.length*4];
                        imp = receive_top2[j].split("\\|");

                        if(imp.length>=4)
                        {
                            str_top3.add(imp[0]);
                            str_top3.add(imp[1]);
                            str_top3.add(imp[2]);
                            str_top3.add(imp[3]);
                        }
                    }

                    for(int k=0;k<receive_top2.length;k++)
                    {
                        if(!str_top3.isEmpty())
                        {
                            str_bitmap.add(str_top3.get(k*4));
                            str_topname.add(str_top3.get(k*4+1));
                            str_topsep.add(str_top3.get(k*4+2));
                            str_topsmallsep.add(str_top3.get(k*4+3));
                        }
                    }

                    string_bitmap = str_bitmap.toArray(new String[str_bitmap.size()]);
                    string_topname = str_topname.toArray(new String[str_topname.size()]);
                    string_topsep = str_topsep.toArray(new String[str_topsep.size()]);
                    string_topsepsep = str_topsmallsep.toArray(new String[str_topsmallsep.size()]);

                    //Bitmap[] bit_bitmap = new Bitmap[string_bitmap.length];
                    bit_bitmap = new Bitmap[string_bitmap.length];

                    for(int p=0;p<string_bitmap.length;p++)
                        bit_bitmap[p] = stringToBitmap(string_bitmap[p]);

                    List<String> rand_text_top = new ArrayList<>();
                    List<String> rand_text_topsep = new ArrayList<>();
                    List<Bitmap> rand_im_top = new ArrayList<>();

                    List<String> rand_text_pants = new ArrayList<>();
                    List<String> rand_text_pantsep = new ArrayList<>();
                    List<Bitmap> rand_im_pants = new ArrayList<>();

                    List<String> rand_text_outer = new ArrayList<>();
                    List<String> rand_text_outersep = new ArrayList<>();
                    List<Bitmap> rand_im_outer = new ArrayList<>();

                    List<String> rand_text_etc = new ArrayList<>();
                    List<String> rand_text_etcsep = new ArrayList<>();
                    List<Bitmap> rand_im_etc = new ArrayList<>();

                    text_top.setText("종류 : 상의\n분류 :\n별명 :");
                    im_top.setImageResource(R.drawable.ic_baseline_people_24);
                    text_bottom.setText("종류 : 하의\n분류 :\n별명 :");
                    im_bottom.setImageResource(R.drawable.ic_baseline_people_24);
                    text_outer.setText("종류 : 아우터\n분류 :\n별명 :");
                    im_outer.setImageResource(R.drawable.ic_baseline_people_24);
                    text_etc.setText("종류 : 기타\n분류 :\n별명 :");
                    im_etc.setImageResource(R.drawable.ic_baseline_people_24);

                    for(int m=0; m<receive_top2.length ; m++)
                    {
                        //Log.d(TAG, "setim2" + bit_bitmap[m]);
                        if(string_topsep.length>1)
                        {
                            String cla = string_topsep[m];
                            Log.d(TAG, "retop" + cla);
                            switch (cla){
                                case "상의":
                                    rand_text_top.add(string_topname[m]);
                                    rand_im_top.add(bit_bitmap[m]);
                                    rand_text_topsep.add(string_topsepsep[m]);
                                    /*String imp = "종류 : 상의\n별명 :" + string_topname[m];
                                    text_top.setText(imp);
                                    im_top.setImageBitmap(bit_bitmap[m]);*/
                                    break;
                                case "하의":
                                    rand_text_pants.add(string_topname[m]);
                                    rand_im_pants.add(bit_bitmap[m]);
                                    rand_text_pantsep.add(string_topsepsep[m]);
                                    /*String imp1 = "종류 : 하의\n별명 :" + string_topname[m];
                                    text_bottom.setText(imp1);
                                    im_bottom.setImageBitmap(bit_bitmap[m]);*/
                                    break;
                                case "아우터":
                                    rand_text_outer.add(string_topname[m]);
                                    rand_im_outer.add(bit_bitmap[m]);
                                    rand_text_outersep.add(string_topsepsep[m]);
                                    /*String imp2 = "종류 : 아우터\n별명 :" + string_topname[m];
                                    text_outer.setText(imp2);
                                    im_outer.setImageBitmap(bit_bitmap[m]);*/
                                    break;
                                case "기타":
                                    rand_text_etc.add(string_topname[m]);
                                    rand_im_etc.add(bit_bitmap[m]);
                                    rand_text_etcsep.add(string_topsepsep[m]);
                                    /*String imp3 = "종류 : 기타\n별명 :" + string_topname[m];
                                    text_etc.setText(imp3);
                                    im_etc.setImageBitmap(bit_bitmap[m]);*/
                                    break;
                            }
                        }
                        else
                        {/*
                            text_top.setText("종류 : 상의\n별명 :");
                            im_top.setImageResource(R.drawable.ic_baseline_people_24);
                            text_bottom.setText("종류 : 하의\n별명 :");
                            im_bottom.setImageResource(R.drawable.ic_baseline_people_24);
                            text_outer.setText("종류 : 아우터\n별명 :");
                            im_outer.setImageResource(R.drawable.ic_baseline_people_24);
                            text_etc.setText("종류 : 기타\n별명 :");
                            im_etc.setImageResource(R.drawable.ic_baseline_people_24);*/
                        }
                    }

                    int n;

                    if(rand_text_top.size()>1)
                    {
                        Random random = new Random();
                        n = random.nextInt(rand_text_top.size());

                        String imp = "종류 : 상의\n분류 : " + rand_text_topsep.get(n) + "\n별명 : " + rand_text_top.get(n);
                        text_top.setText(imp);
                        im_top.setImageBitmap(rand_im_top.get(n));
                        Log.d(TAG, "setim" + rand_im_top.get(n) + n);
                        Log.d(TAG, "setnum" + rand_text_top.size());
                    }
                    else if(rand_text_top.size()==1)
                    {
                        String imp = "종류 : 상의\n분류 : " + rand_text_topsep.get(0) + "\n별명 : " + rand_text_top.get(0);
                        text_top.setText(imp);
                        im_top.setImageBitmap(rand_im_top.get(0));
                        Log.d(TAG, "setim1" + rand_im_top.get(0));
                    }

                    if(rand_text_pants.size()>1)
                    {
                        Random random = new Random();
                        n = random.nextInt(rand_text_pants.size());

                        String imp = "종류 : 하의\n분류 : " + rand_text_pantsep.get(n) + "\n별명 : " + rand_text_pants.get(n);
                        text_bottom.setText(imp);
                        im_bottom.setImageBitmap(rand_im_pants.get(n));
                    }
                    else if(rand_text_pants.size()==1)
                    {
                        String imp = "종류 : 하의\n분류 : " + rand_text_pantsep.get(0) + "\n별명 : " + rand_text_pants.get(0);
                        text_bottom.setText(imp);
                        im_bottom.setImageBitmap(rand_im_pants.get(0));
                        Log.d(TAG, "setim11" + rand_text_pants.size());
                    }

                    if(rand_text_outer.size()>1)
                    {
                        Random random = new Random();
                        n = random.nextInt(rand_text_outer.size());

                        String imp = "종류 : 아우터\n분류 : " + rand_text_outersep.get(n) + "\n별명 : " + rand_text_outer.get(n);
                        text_outer.setText(imp);
                        im_outer.setImageBitmap(rand_im_outer.get(n));
                    }
                    else if(rand_text_outer.size()==1)
                    {
                        String imp = "종류 : 아우터\n분류 : " + rand_text_outersep.get(0) + "\n별명 : " + rand_text_outer.get(0);
                        text_outer.setText(imp);
                        im_outer.setImageBitmap(rand_im_outer.get(0));
                    }

                    if(rand_text_etc.size()>1)
                    {
                        Random random = new Random();
                        n = random.nextInt(rand_text_etc.size());

                        String imp = "종류 : 기타\n분류 : " + rand_text_etcsep.get(n) + "\n별명 : " + rand_text_etc.get(n);
                        text_etc.setText(imp);
                        im_etc.setImageBitmap(rand_im_etc.get(n));
                    }
                    else if(rand_text_etc.size()==1)
                    {
                        String imp = "종류 : 기타\n분류 : " + rand_text_etcsep.get(0) + "\n별명 : " + rand_text_etc.get(0);
                        text_etc.setText(imp);
                        im_etc.setImageBitmap(rand_im_etc.get(0));
                    }

                    Toast.makeText(getContext(), "옷 추천이 완료되었습니다.",Toast.LENGTH_SHORT).show();

                }
            }
        });

        if(getArguments()!=null)
        {
            Bundle bundle = getArguments();
            location_str = bundle.getString("location");
            locatetxt = (TextView)view.findViewById(R.id.location);
            //locatetxt.setText(location_str);

            weather = bundle.getString("weather");

            //weather = "경상북도 경산시 압량읍|20220512 0600 일기 예보^온도 : 17 도^풍속 : 0.9 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 80%|20220512 0700 일기 예보^온도 : 17 도^풍속 : 0.9 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 80%|20220512 0800 일기 예보^온도 : 18 도^풍속 : 1.2 m/s^하늘 상태 : 흐림^강수 형태 : 비^강수 확률 : 60%^습도 : 80%|20220512 0900 일기 예보^온도 : 19 도^풍속 : 1.8 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 70%|20220512 1000 일기 예보^온도 : 21 도^풍속 : 2.7 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 65%|20220512 1100 일기 예보^온도 : 22 도^풍속 : 2.9 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 60%|20220512 1200 일기 예보^온도 : 22 도^풍속 : 3.3 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 60%|20220512 1300 일기 예보^온도 : 23 도^풍속 : 3.2 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 60%|20220512 1400 일기 예보^온도 : 23 도^풍속 : 2.7 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 60%|20220512 1500 일기 예보^온도 : 22 도^풍속 : 2.8 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 60%|20220512 1600 일기 예보^온도 : 22 도^풍속 : 2.8 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 60%|20220512 1700 일기 예보^온도 : 21 도^풍속 : 2.8 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 60%|20220512 1800 일기 예보^온도 : 20 도^풍속 : 2.6 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 65%|20220512 1900 일기 예보^온도 : 19 도^풍속 : 2.2 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 65%|20220512 2000 일기 예보^온도 : 18 도^풍속 : 2 m/s^하늘 상태 : 구름 많음^강수 형태 : 없음^강수 확률 : 20%^습도 : 65%|20220512 2100 일기 예보^온도 : 18 도^풍속 : 1.8 m/s^하늘 상태 : 구름 많음^강수 형태 : 없음^강수 확률 : 20%^습도 : 65%|20220512 2200 일기 예보^온도 : 17 도^풍속 : 1.7 m/s^하늘 상태 : 구름 많음^강수 형태 : 없음^강수 확률 : 20%^습도 : 70%|20220512 2300 일기 예보^온도 : 16 도^풍속 : 1.3 m/s^하늘 상태 : 구름 많음^강수 형태 : 없음^강수 확률 : 20%^습도 : 75%|20220513 0000 일기 예보^온도 : 16 도^풍속 : 1.3 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 75%|20220513 0100 일기 예보^온도 : 16 도^풍속 : 1.4 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 75%|20220513 0200 일기 예보^온도 : 16 도^풍속 : 1.5 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 80%|20220513 0300 일기 예보^온도 : 15 도^풍속 : 1.7 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 80%|20220513 0400 일기 예보^온도 : 15 도^풍속 : 1.8 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 80%|20220513 0500 일기 예보^온도 : 15 도^풍속 : 1.8 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 80%|20220513 0600 일기 예보^온도 : 15 도^풍속 : 1.9 m/s^하늘 상태 : 흐림^강수 형태 : 없음^강수 확률 : 30%^습도 : 80%";

            if(weather!=null)
                weather_arr = weather.split("\\|");


            if(weather_arr!=null)
            {
                present_location = weather_arr[0];

                for(int i=1;i<26;i++)
                {
                    String[] arr_slice = weather_arr[i].split("\\^");
                    weather_arr_slice.add(arr_slice[0]);
                    weather_arr_slice.add(arr_slice[1]);
                    weather_arr_slice.add(arr_slice[2]);
                    weather_arr_slice.add(arr_slice[3]);
                    weather_arr_slice.add(arr_slice[4]);
                    weather_arr_slice.add(arr_slice[5]);
                    weather_arr_slice.add(arr_slice[6]);
                    Log.d(TAG, "weather_slice" + weather_arr_slice);
                }
            }

            LocalTime now = LocalTime.now();
            LocalDate nowdate = LocalDate.now();

            int dayOfMonth = nowdate.getDayOfMonth();
            int hour = now.getHour();
            int minute = now.getMinute();
            int time_sum = dayOfMonth*1000 + hour*100 + minute;
            int min = 100000;
            int minindex = 0;

            int[] time_arr = {dayOfMonth*1000+600,dayOfMonth*1000+700,dayOfMonth*1000+800,dayOfMonth*1000+900,dayOfMonth*1000+1000,
                    dayOfMonth*1000+1200,dayOfMonth*1000+1300,dayOfMonth*1000+1400,dayOfMonth*1000+1500,dayOfMonth*1000+1600,
                    dayOfMonth*1000+1700,dayOfMonth*1000+1800,dayOfMonth*1000+1900,dayOfMonth*1000+2000,dayOfMonth*1000+2100,
                    dayOfMonth*1000+2200,dayOfMonth*1000+2300,dayOfMonth*1000+2400,(dayOfMonth+1)*1000+100,(dayOfMonth+1)*1000+200,
                    (dayOfMonth+1)*1000+300,(dayOfMonth+1)*1000+400,(dayOfMonth+1)*1000+500,(dayOfMonth+1)*1000+600};

            for(int i=0;i<24;i++)
            {
                int time = time_arr[i];
                if(Math.abs(time_sum - time) < min)
                {
                    min = Math.abs(time_sum - time);
                    minindex = 7*i;
                }
            }

            minindex = minindex + 7;

            String present_weather = "\n"+ "현재위치: " +present_location + "\n"+ "기준날짜: "   + weather_arr_slice.get(minindex)  + "\n"
                    + weather_arr_slice.get(minindex+1) + "\n"
                    + weather_arr_slice.get(minindex + 3) + "\n"
                    + weather_arr_slice.get(minindex + 5);

            Log.d(TAG, "minindex1" + weather_arr_slice.get(minindex+1));
            Log.d(TAG, "minindex2" + weather_arr_slice.get(minindex+1));
            Log.d(TAG, "minindex3" + weather_arr_slice.get(minindex + 5));

            String str22[] = weather_arr_slice.get(minindex + 5).split(" ");
            String str23[] = str22[3].split("\\%");

            Log.d(TAG, "str23" + str23[0]);

            ImageView img_fore = view.findViewById(R.id.fore);
            if(Integer.parseInt(str23[0])>=0 && Integer.parseInt(str23[0])<20)
               img_fore.setImageDrawable(getResources().getDrawable(R.drawable.sun));
            else if(Integer.parseInt(str23[0])>=20 && Integer.parseInt(str23[0])<40)
                img_fore.setImageDrawable(getResources().getDrawable(R.drawable.little_sun));
            else if(Integer.parseInt(str23[0])>=40 && Integer.parseInt(str23[0])<60)
                img_fore.setImageDrawable(getResources().getDrawable(R.drawable.normal));
            else if(Integer.parseInt(str23[0])>=60 && Integer.parseInt(str23[0])<80)
                img_fore.setImageDrawable(getResources().getDrawable(R.drawable.cloud));
            else if(Integer.parseInt(str23[0])>=80 && Integer.parseInt(str23[0])<100)
                img_fore.setImageDrawable(getResources().getDrawable(R.drawable.rain));

            stt = weather_arr_slice.get(minindex+1).split(" ");

            Log.d(TAG, "presenttime" + dayOfMonth);
            Log.d(TAG, "minindex" + minindex);
            Log.d(TAG, "mintime" + min);

            /*if(weather_arr_slice!=null)
            {
                for(int i=0;i<weather_arr_slice.size();i++)
                {
                    weather_arr_slice.add(weather_arr[i]);
                    Log.d(TAG, "weather_slice" + weather_arr[i-1]);
                }
            }*/


            locatetxt2 = (TextView)view.findViewById(R.id.location2);
            locatetxt2.setText(present_weather);
            Log.d(TAG, "frag3 " + getArguments());
            Log.d(TAG, "frag3 " + location_str);
            //Log.d(TAG, "frag3 " + weather_arr[0]);
            //Log.d(TAG, "frag3 " + weather_arr[1]);

        }

        return view;
    }

    class ConnectThread extends Thread{
        public void run(){
            try{
                //socket = new Socket("192.168.0.6",10000);

                String frag1_string = "추천|" + stt[2] + "|" + wide_email;
                Log.d(TAG, "frag3 " + frag1_string);
                //PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8")),true);
                out.println(frag1_string);
                //BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                receive_top = input1.readLine();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    class ConnectThread_feedback extends Thread{
        public void run(){
            try{
                //socket = new Socket("192.168.0.6",10000);

                String frag1_string = "피드백|" + wide_email+ "|" + status;
                Log.d(TAG, "frag3 " + frag1_string);
                //PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8")),true);
                out.println(frag1_string);
                //BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //receive_top = input1.readLine();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
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