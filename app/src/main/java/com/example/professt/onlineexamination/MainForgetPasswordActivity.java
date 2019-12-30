package com.example.professt.onlineexamination;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainForgetPasswordActivity extends AppCompatActivity {

    Button Teachers,Student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_forget_password);

        Teachers = (Button)findViewById(R.id.teachers);
        Student = (Button)findViewById(R.id.students);


        Teachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainForgetPasswordActivity.this,ForgetsPasswordActivity.class);
                startActivity(intent);
            }
        });


        Student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainForgetPasswordActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(MainForgetPasswordActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
