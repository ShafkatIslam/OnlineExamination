package com.example.professt.onlineexamination.Examination;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.professt.onlineexamination.Evaluated_Answer_Script.Evaluated_Answer_Script;
import com.example.professt.onlineexamination.Information;
import com.example.professt.onlineexamination.Key;
import com.example.professt.onlineexamination.R;
import com.example.professt.onlineexamination.StudentProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class WrittenActivity extends AppCompatActivity {

    Button questionId,answerId,evaluateId;
    TextView time;

    private ProgressDialog loading;

    String dept,question_paper,Written_status;
    private String EVENT_DATE_TIME = "";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_written);

        questionId = (Button)findViewById(R.id.questionId);
        answerId = (Button)findViewById(R.id.answerId);
        evaluateId = (Button)findViewById(R.id.evaluateAnswerId);
        time = (TextView) findViewById(R.id.time);

        questionId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Information information = Information.getInstance();
                question_paper = information.getQuestion_id();

                String pdf = "https://www.tikabarta.com/mocktest/api/Questions/"+question_paper+".pdf"; //YOUR URL TO PDF
                String googleDocsUrl = "http://docs.google.com/viewer?url="+ pdf;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(googleDocsUrl ), "text/html");
                startActivity(intent);

//                Intent intent = new Intent(WrittenActivity.this,WrittenExaminationActivity.class);
//                startActivity(intent);

            }
        });

        evaluateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WrittenActivity.this,Evaluated_Answer_Script.class);
                startActivity(intent);
            }
        });

        answerId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String times = time.getText().toString();
                if(times.equalsIgnoreCase("Time is up"))
                {
                    Toast.makeText(WrittenActivity.this, "Time is up. You cannot submit", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Intent intent = new Intent(WrittenActivity.this,WrittenExaminationActivity.class);
                    startActivity(intent);
                }
            }
        });

        getData();
        countDownStart();

        if (dept != null)
        {
            Log.d("Dept", dept);
        }

    }

    private void countDownStart() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(EVENT_DATE_TIME);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;

                        String count = hours+":"+minutes+":"+seconds;

                        Log.d("Time", "" + count);

                        time.setText(count);
//                        tv_days.setText(String.format("%02d", Days));
//                        tv_hour.setText(String.format("%02d", Hours));
//                        tv_minute.setText(String.format("%02d", Minutes));
//                        tv_second.setText(String.format("%02d", Seconds));
                    } else {
//                        linear_layout_1.setVisibility(View.VISIBLE);
//                        linear_layout_2.setVisibility(View.GONE);
                        handler.removeCallbacks(runnable);
                        time.setText("Time is up");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1 * 1000);
    }

    private void getData() {

        Information information = Information.getInstance();
        dept = information.getDept();

        //for showing progress dialog
        loading = new ProgressDialog(WrittenActivity.this);
        loading.setIcon(R.drawable.load);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Key.LOAD_WRITTEN_QUES+dept;

        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loading.dismiss();
                        Toast.makeText(WrittenActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(WrittenActivity.this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {

        //Create json object for receiving jason data
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Key.JSON_ARRAY);


            if (result.length()==0)
            {
                Toast.makeText(WrittenActivity.this, "No Examination Schedule Available!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(WrittenActivity.this, ExaminationActivity.class);
                startActivity(intent);
                //imgNoData.setImageResource(R.drawable.nodata);
            }

            else
            {
                for (int i = 0; i < result.length(); i++) {

                    JSONObject jo = result.getJSONObject(i);
                    String question_id = jo.getString(Key.KEY_WQUESTION);
                    String department = jo.getString(Key.KEY_DEPT);
                    String date = jo.getString(Key.KEY_DATE);
                    String end_time = jo.getString(Key.KEY_END_TIME);
                    //String written_status = jo.getString(Key.KEY_WRITTEN_STATUS);

                    EVENT_DATE_TIME = date+" "+end_time;

                    Information information = Information.getInstance();
                    information.setQuestion_id(question_id);
                    //information.setWritten_status(written_status);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {           //creating method of onBackPressed

        Intent intent =  new Intent(WrittenActivity.this,ExaminationActivity.class);
        startActivity(intent);

    }
}
