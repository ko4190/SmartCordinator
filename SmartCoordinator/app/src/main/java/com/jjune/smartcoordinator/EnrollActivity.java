package com.jjune.smartcoordinator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class EnrollActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText mEmail, mPwd;
    private Button mButtonReg, mButtonCan;
    public Socket socket;
    public Handler mHandler ;
    String TAG = "MyTAG";
    EditText email;
    EditText address;
    EditText gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("smart");

        mEmail = findViewById(R.id.enroll_ID);
        mPwd = findViewById(R.id.edit_pwd);

        mButtonReg = findViewById(R.id.enroll_enrollbutton);

        mButtonCan = findViewById(R.id.enroll_cancelbutton);

        email = (EditText)findViewById(R.id.enroll_ID);
        address = (EditText)findViewById(R.id.enroll_location);
        gender = (EditText)findViewById(R.id.enroll_gender);



        mButtonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = mEmail.getText().toString();
                String strPwd = mPwd.getText().toString();

                mFirebaseAuth.createUserWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(EnrollActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(strPwd);

                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast.makeText(EnrollActivity.this, "회원가입 성공",Toast.LENGTH_SHORT).show();
                            String txt = email.getText().toString() + "^" + address.getText().toString() + "^" + gender.getText().toString();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                            intent.putExtra("email",txt);
                            startActivity(intent);


                        }
                        else
                        {
                            Toast.makeText(EnrollActivity.this, "회원가입 실패(아이디 중복)",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        mButtonCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}