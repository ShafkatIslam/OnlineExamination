package com.example.professt.onlineexamination.Examination;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.professt.onlineexamination.HomeActivity;
import com.example.professt.onlineexamination.Information;
import com.example.professt.onlineexamination.Key;
import com.example.professt.onlineexamination.MCQ.MCQActivity;
import com.example.professt.onlineexamination.R;
import com.example.professt.onlineexamination.Score.ScoreActivity;
import com.example.professt.onlineexamination.SignUp;
import com.example.professt.onlineexamination.StudentProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExaminationActivity extends AppCompatActivity {

    Button btn_Score, btn_Mcqexam, btn_Writtenexam;

    private ProgressDialog loading;

    String department,dept,mcq_Status,Written_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);

        btn_Score = (Button)findViewById(R.id.btn_score);
        btn_Mcqexam = (Button)findViewById(R.id.btn_mcqexam);
        btn_Writtenexam = (Button)findViewById(R.id.btn_writtenexam);


        btn_Score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ExaminationActivity.this,ScoreActivity.class);
                startActivity(intent);
            }
        });

        btn_Writtenexam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Information information = Information.getInstance();
                dept = information.getDept();
                department = information.getDepartment();
                Written_status = information.getWritten_status();

                if(Written_status.equalsIgnoreCase("No"))
                {
                    showData("Sorry","Examination is not yet start");
                }
                else
                {
                    Intent intent = new Intent(ExaminationActivity.this,WrittenActivity.class);
                    startActivity(intent);
                }

            }
        });

        btn_Mcqexam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Information information = Information.getInstance();
                dept = information.getDept();
                department = information.getDepartment();
                mcq_Status = information.getMcq_status();

                Log.d("dept", "" + dept);
                Log.d("department", "" + department);

                if(dept.equalsIgnoreCase(department))
                {
                    if(mcq_Status.equalsIgnoreCase("No"))
                    {
                        showData("Sorry","Examination is not yet start");
                    }
                    else
                    {
                        Intent intent = new Intent(ExaminationActivity.this,MCQActivity.class);
                        startActivity(intent);
                    }

                }

                else
                {
                    showData("Sorry","No Exam Schedule Found");
                }


            }
        });

        getData();
    }

    private void getInfo() {

        Information information = Information.getInstance();
        dept = information.getDept();

        //for showing progress dialog
        loading = new ProgressDialog(ExaminationActivity.this);
        loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Key.LOAD_WRITTEN_INFORMATION+dept;


        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON1(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loading.dismiss();
                        Toast.makeText(ExaminationActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(ExaminationActivity.this);
        requestQueue.add(stringRequest);
    }

    private void showJSON1(String response) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Key.JSON_ARRAY);


            if (result.length()==0)
            {
                //Toast.makeText(ExaminationActivity.this, "No Data Available!", Toast.LENGTH_SHORT).show();

            }

            else
            {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);
                    String id = jo.getString(Key.KEY_ID);
                    String written_status = jo.getString(Key.KEY_WRITTEN_STATUS);

                    Information information = Information.getInstance();
                    information.setWritten_status(written_status);

                    Log.d("W_status", "" + written_status);
                    //deptNames.add(dept);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showData(String title, String message)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.show();
    }

    private void getData() {

        Information information = Information.getInstance();
        dept = information.getDept();

        //for showing progress dialog
        loading = new ProgressDialog(ExaminationActivity.this);
        loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Key.LOAD_QUES_INFORMATION+dept;


        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
                getInfo();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loading.dismiss();
                        Toast.makeText(ExaminationActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(ExaminationActivity.this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Key.JSON_ARRAY);


            if (result.length()==0)
            {
                Toast.makeText(ExaminationActivity.this, "No Data Available!", Toast.LENGTH_SHORT).show();

            }

            else
            {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);
                    String id = jo.getString(Key.KEY_ID);
                    String course_id = jo.getString(Key.KEY_COURSE_ID);
                    String no_of_questions = jo.getString(Key.KEY_NO_OF_QUES);
                    String dept = jo.getString(Key.KEY_DEPT);
                    String mcq_status = jo.getString(Key.KEY_MCQ_STATUS);
                    String written_status = jo.getString(Key.KEY_WRITTEN_STATUS);

                    Information information = Information.getInstance();
                    information.setUid(id);
                    information.setCourse_id(course_id);
                    information.setNo_of_questions(no_of_questions);
                    information.setDepartment(dept);
                    information.setMcq_status(mcq_status);
                    information.setWritten_status(written_status);

                    Log.d("departments", "" + dept);

                    //deptNames.add(dept);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {           //creating method of onBackPressed

        Intent intent =  new Intent(ExaminationActivity.this,HomeActivity.class);
        startActivity(intent);

    }
}
