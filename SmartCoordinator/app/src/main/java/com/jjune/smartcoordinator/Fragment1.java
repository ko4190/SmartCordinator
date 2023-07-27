package com.jjune.smartcoordinator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import static com.jjune.smartcoordinator.RealActivity.socket;
import static com.jjune.smartcoordinator.RealActivity.out;
import static com.jjune.smartcoordinator.RealActivity.input1;

public class Fragment1 extends Fragment {

    private Socket socket;
    private Handler mHandler ;
    String TAG = "MyTAG";
    String cloth;
    String string_top[] = {"top1","top1","top1"};
    String string_pants[] = {"pants1","pants1","pants1"};
    String string_outer[] = {};
    String email;
    ListView listView2;

    String receive_top;
    String receive_pants;
    String receive_outer;
    String receive_skirt;
    String receive_onepiece;
    String receive_cap;
    String receive_shoe;
    String delete_info;
    String last_info;

    String sld2;
    String sld;

    int img[] = {R.drawable.ic_baseline_web_asset_24,R.drawable.ic_baseline_people_24,R.drawable.ic_baseline_construction_24};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(getArguments()!=null)
        {
            Bundle bundle = getArguments();
            email = bundle.getString("email");
        }

        View view = inflater.inflate(R.layout.fragment_1, container, false);
        //ConnectThread th = new ConnectThread();
        //th.start();


        Log.d(TAG, "cloth_image " + img);

        ListView listView = view.findViewById(R.id.frag_list);
        listView2 = view.findViewById(R.id.frag_list2);

        List<String> list = new ArrayList<>();
        list.add("상의");
        list.add("하의");
        list.add("아우터");
        list.add("기타");
        //list.add("원피스");
        //list.add("모자");
        //list.add("신발");

        List<String> list_top = new ArrayList<>();
        List<String> list_pants = new ArrayList<>();
        List<String> list_outer = new ArrayList<>();
        List<String> list_skirt = new ArrayList<>();
        List<String> list_onepiece = new ArrayList<>();
        List<String> list_cap = new ArrayList<>();
        List<String> list_shoe = new ArrayList<>();

        list_top.add("상의1");
        list_top.add("상의2");
        list_top.add("상의3");
        list_top.add("상의4");
        list_top.add("상의5");

        list_pants.add("바지1");
        list_pants.add("바지1");
        list_pants.add("바지1");
        list_pants.add("바지1");
        list_pants.add("바지1");


        if(cloth!=null)
        {
            // 데이터를 받아오면 그 정보를 각 옷별 리스트에 추가해줌
            switch(cloth){
                case "top":
                    list_top.add("name");
                    break;
                case "pants":
                    list_pants.add("name");
                    break;
                case "outer":
                    list_outer.add("name");
                    break;
                case "skirt":
                    list_skirt.add("name");
                    break;
                case "onepiece":
                    list_onepiece.add("name");
                    break;
                case "cap":
                    list_cap.add("name");
                    break;
                case "shoe":
                    list_shoe.add("name");
                    break;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String data = (String) adapterView.getItemAtPosition(i);
                //Log.d(TAG, "data " + data);
                //Log.d(TAG, "position " + i);
                //Log.d(TAG, "receive2 " + "/listview");

                switch(data) {
                    case "상의":
                        last_info = "상의";
                        ConnectThread_top th = new ConnectThread_top();
                        th.start();
                        try {
                            th.join();
                            //Log.d(TAG, "receive2 " + receive_top);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            //Log.d(TAG, "receive2 " + e);
                        }
                        break;

                    case "하의":
                        last_info = "하의";
                        ConnectThread_pants th1 = new ConnectThread_pants();
                        th1.start();
                        try {
                            th1.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "아우터":
                        last_info = "아우터";
                        ConnectThread_outer th2 = new ConnectThread_outer();
                        th2.start();
                        try {
                            th2.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "기타":
                        last_info = "기타";
                        ConnectThread_skirt th3 = new ConnectThread_skirt();
                        th3.start();
                        try {
                            th3.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "원피스":
                        last_info = "원피스";
                        ConnectThread_onepiece th4 = new ConnectThread_onepiece();
                        th4.start();
                        try {
                            th4.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "모자":
                        last_info = "모자";
                        ConnectThread_cap th5 = new ConnectThread_cap();
                        th5.start();
                        try {
                            th5.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "신발":
                        last_info = "신발";
                        ConnectThread_shoe th6 = new ConnectThread_shoe();
                        th6.start();
                        try {
                            th6.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                        /*ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list_top);
                        listView2.setAdapter(adapter2);*/
                        /*try{
                            String frag1_string = "옷장|상의|" + email;
                            out.println(frag1_string);
                            top1 = input1.readLine();
                        }catch(Exception e)
                        {   }*/
                }
                //Log.d(TAG, "frag1_read " + receive_top);
                //Log.d(TAG, "frag1_read " + last_info);

                String[] string_bitmap = new String[0];
                String[] string_topname = new String[0];
                String[] string_topsep = new String[0];
                Bitmap[] bit_bitmap = new Bitmap[0];

                if(receive_top != null)
                {
                    String[] receive_top2 = receive_top.split("\\^");
                    List<String> str_top3 = new ArrayList<>();
                    List<String> str_topname = new ArrayList<>();
                    List<String> str_bitmap = new ArrayList<>();
                    List<String> str_topsep = new ArrayList<>();
                    //String[] string_bitmap;
                    //String[] string_topname;
                    //String[] string_topsep;

                    for(int j=0; j<receive_top2.length;j++)
                    {
                        //Log.d(TAG, "frag1_readd " + receive_top2[0]);
                        //Log.d(TAG, "frag1_readd " + receive_top2.length);
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
                            str_topsep.add(str_top3.get(k*4+3));
                        }
                    }

                    string_bitmap = str_bitmap.toArray(new String[str_bitmap.size()]);
                    string_topname = str_topname.toArray(new String[str_topname.size()]);
                    string_topsep = str_topsep.toArray(new String[str_topsep.size()]);
                    //Bitmap[] bit_bitmap = new Bitmap[string_bitmap.length];
                    bit_bitmap = new Bitmap[string_bitmap.length];

                    for(int p=0;p<string_bitmap.length;p++)
                        bit_bitmap[p] = stringToBitmap(string_bitmap[p]);
                }
                /*string_topname = new String[10];
                string_topsep = new String[10];
                string_topname[0]="as";
                string_topsep[0]="ass";
                bit_bitmap = new Bitmap[10];
                bit_bitmap[0] = BitmapFactory.decodeFile("C:\\Users\\beom\\AndroidStudioProjects\\SmartCoordinator\\app\\src\\main\\res\\drawable\\ic_baseline_people_24.xml" );*/

                MyAdapter adapter2 = new MyAdapter(getContext(),string_topname,string_topsep, bit_bitmap);
                listView2.setAdapter(adapter2);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    class ConnectThread extends Thread{
        public void run(){
            try{

                //socket = new Socket("192.168.152.133",10000);
                String frag1_string = "옷장|" + email;
                //PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8")),true);
                out.println(frag1_string);
                //BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String read = input1.readLine();

                //socket.close();

            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        //List<String> big_sep = new ArrayList<>();
        //List<String> small_sep = new ArrayList<>();
        String big_sep[];
        String small_sep[];
        //List<String> imgs= new ArrayList<>();
        //int imgs[];
        List<Bitmap> bit_1 = new ArrayList<>();
        Bitmap[] bit11;

        MyAdapter(Context con, String big[], String small[], Bitmap[] bitmap_1){
            super(con, R.layout.custom_adapter,R.id.adapter_text1,big);

            this.context=con;
            this.big_sep=big;
            this.small_sep=small;
            //this.imgs=img;
            this.bit11 = bitmap_1;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.custom_adapter,parent,false);
            ImageView images = row.findViewById(R.id.adapter_image);
            TextView text1 = row.findViewById(R.id.adapter_text1);
            TextView text2 = row.findViewById(R.id.adapter_text2);

            Button bt1 = (Button) row.findViewById(R.id.delete);
            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TextView info1 = (TextView) view.findViewById(R.id.adapter_text1);
                    //TextView info2 = (TextView) view.findViewById(R.id.adapter_text2);
                    sld = text1.getText().toString();
                    sld2 = text2.getText().toString();

                    ConnectThread_delete th11 = new ConnectThread_delete();
                    th11.start();
                    try {
                        th11.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    switch(last_info) {
                        case "상의":
                            ConnectThread_top th = new ConnectThread_top();
                            th.start();
                            try {
                                th.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;

                        case "하의":
                            ConnectThread_pants th1 = new ConnectThread_pants();
                            th1.start();
                            try {
                                th1.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;

                        case "아우터":
                            ConnectThread_outer th2 = new ConnectThread_outer();
                            th2.start();
                            try {
                                th2.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;

                        case "기타":
                            ConnectThread_skirt th3 = new ConnectThread_skirt();
                            th3.start();
                            try {
                                th3.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;

                        case "원피스":
                            ConnectThread_onepiece th4 = new ConnectThread_onepiece();
                            th4.start();
                            try {
                                th4.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;

                        case "모자":
                            ConnectThread_cap th5 = new ConnectThread_cap();
                            th5.start();
                            try {
                                th5.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;

                        case "신발":
                            ConnectThread_shoe th6 = new ConnectThread_shoe();
                            th6.start();
                            try {
                                th6.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        /*ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list_top);
                        listView2.setAdapter(adapter2);*/
                        /*try{
                            String frag1_string = "옷장|상의|" + email;
                            out.println(frag1_string);
                            top1 = input1.readLine();
                        }catch(Exception e)
                        {   }*/
                    }
                    //Log.d(TAG, "frag1_read " + receive_top);
                    //Log.d(TAG, "frag1_read " + last_info);

                    String[] string_bitmap = new String[0];
                    String[] string_topname = new String[0];
                    String[] string_topsep = new String[0];
                    Bitmap[] bit_bitmap = new Bitmap[0];

                    if(receive_top != null)
                    {
                        String[] receive_top2 = receive_top.split("\\^");
                        List<String> str_top3 = new ArrayList<>();
                        List<String> str_topname = new ArrayList<>();
                        List<String> str_bitmap = new ArrayList<>();
                        List<String> str_topsep = new ArrayList<>();
                        //String[] string_bitmap;
                        //String[] string_topname;
                        //String[] string_topsep;

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
                                str_topsep.add(str_top3.get(k*4+3));
                            }
                        }

                        string_bitmap = str_bitmap.toArray(new String[str_bitmap.size()]);
                        string_topname = str_topname.toArray(new String[str_topname.size()]);
                        string_topsep = str_topsep.toArray(new String[str_topsep.size()]);
                        //Bitmap[] bit_bitmap = new Bitmap[string_bitmap.length];
                        bit_bitmap = new Bitmap[string_bitmap.length];

                        for(int p=0;p<string_bitmap.length;p++)
                            bit_bitmap[p] = stringToBitmap(string_bitmap[p]);
                    }

                    MyAdapter adapter2 = new MyAdapter(getContext(),string_topname,string_topsep, bit_bitmap);
                    listView2.setAdapter(adapter2);
                    //Log.d(TAG, "delete " + "delete");
                }
            });

            images.setClipToOutline(true);
            /*if(imgs!=null)
                images.setImageResource(imgs[position]);*/
            if(big_sep!=null)
                text1.setText(big_sep[position]);
            if(small_sep!=null)
                text2.setText(small_sep[position]);
            if(bit_1!=null)
                images.setImageBitmap(bit11[position]);

            return row;
        }
    }

    class ConnectThread_top extends Thread{
        public void run(){
            try{
                String frag1_string = "옷장|상의|" + email;
                out.println(frag1_string);
               //Log.d(TAG, "receive " + receive_top);
                receive_top = input1.readLine();

            }catch(Exception e)
            {
                e.printStackTrace();
                //Log.d(TAG, "receive " + e);
            }
        }
    }

    class ConnectThread_pants extends Thread{
        public void run(){
            try{
                String frag1_string = "옷장|하의|" + email;
                out.println(frag1_string);
                //receive_pants = input1.readLine();
                receive_top = input1.readLine();

            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    class ConnectThread_outer extends Thread{
        public void run(){
            try{
                String frag1_string = "옷장|아우터|" + email;
                out.println(frag1_string);
                //receive_outer = input1.readLine();
                receive_top = input1.readLine();

            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    class ConnectThread_skirt extends Thread{
        public void run(){
            try{
                String frag1_string = "옷장|기타|" + email;
                out.println(frag1_string);
                //receive_skirt = input1.readLine();
                receive_top = input1.readLine();

            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    class ConnectThread_onepiece extends Thread{
        public void run(){
            try{
                String frag1_string = "옷장|상의|" + email;
                out.println(frag1_string);
                //receive_onepiece = input1.readLine();
                receive_top = input1.readLine();

            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    class ConnectThread_cap extends Thread{
        public void run(){
            try{
                String frag1_string = "옷장|모자|" + email;
                out.println(frag1_string);
                //receive_cap = input1.readLine();
                receive_top = input1.readLine();

            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    class ConnectThread_shoe extends Thread{
        public void run(){
            try{
                String frag1_string = "옷장|신발|" + email;
                out.println(frag1_string);
                //receive_shoe = input1.readLine();
                receive_top = input1.readLine();

            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    class ConnectThread_delete extends Thread{
        public void run(){
            try{
                String frag1_string = "삭제|" + email + "|" + sld;
                out.println(frag1_string);
                //delete_info = input1.readLine();

            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

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