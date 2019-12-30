package com.example.professt.onlineexamination.MCQ;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.professt.onlineexamination.Examination.ExaminationActivity;
import com.example.professt.onlineexamination.Information;
import com.example.professt.onlineexamination.Key;
import com.example.professt.onlineexamination.R;
import com.example.professt.onlineexamination.SignUp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MCQActivity extends AppCompatActivity {

    public ListView mcqListView;
    Button submit;
    TextView courseName,time;

    ArrayList<Questions> questions = new ArrayList<Questions>();

    CustomAdapter customAdapter;

    private ProgressDialog loading;

    ArrayList<String> answers = new ArrayList<String>();
    int marks;
    String mid,course_id,uid,no_of_ques,department,ids,cid;

    private String EVENT_DATE_TIME = "";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq);

        mcqListView = (ListView)findViewById(R.id.mcqListView);
        submit = (Button)findViewById(R.id.submit);
        courseName = (TextView) findViewById(R.id.courseName);
        time = (TextView) findViewById(R.id.time);

        marks = 0;


        getData();
        countDownStart();

        Information information = Information.getInstance();
        mid = information.getMid();
        course_id = information.getCourse_id();
        uid = information.getUid();
        no_of_ques = information.getNo_of_questions();



        Log.d("Question_Id", "" + uid);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String times = time.getText().toString();
                if(times.equalsIgnoreCase("Time is up"))
                {
                    Toast.makeText(MCQActivity.this, "Time is up. You cannot submit", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String message = "";

                    // get the value of selected answers from custom adapter
                    for (int i = 0; i < CustomAdapter.selectedAnswers.size(); i++) {

                        if((answers.get(i)).equalsIgnoreCase(CustomAdapter.selectedAnswers.get(i)))
                        {
                            marks = marks+1;
                        }
                        message = message + "\n" + (i + 1) + " " + CustomAdapter.selectedAnswers.get(i);
                    }
// display the message on screen with the help of Toast.
                    final String mark = String.valueOf(marks);
                    //Toast.makeText(getApplicationContext(), mark, Toast.LENGTH_LONG).show();

                    final String course_name = courseName.getText().toString();

                    loading = new ProgressDialog(MCQActivity.this);
                    loading.setIcon(R.drawable.load);
                    loading.setTitle("Uploading to Server");
                    loading.setMessage("Please wait...");
                    loading.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Key.LOAD_MCQ_MARKS, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            //for track response in Logcat
                            Log.d("RESPONSE", "" + response);

                            //if we are getting success from server
                            if (response.equals("success")) {
                                //creating a shared preference
                                loading.dismiss();
                                //starting profile activity
                                Intent intent = new Intent(MCQActivity.this, ExaminationActivity.class);
//                            intent.putExtra("email",emails);
                                startActivity(intent);

                                Toast.makeText(MCQActivity.this, "Submit Successfully", Toast.LENGTH_SHORT).show();


                            }
                            else if (response.equals("exists")) {
                                Toast.makeText(MCQActivity.this, "Already Submitted", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                            else if (response.equals("failure")) {
                                Toast.makeText(MCQActivity.this, "Submit failed", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MCQActivity.this, "There is an error", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            //return super.getParams();

                            Map<String,String> params = new HashMap<>();

                            //Ading parameters to request
                            params.put(Key.KEY_MID,mid);
                            params.put(Key.KEY_MCQ_MARKS,mark);
                            params.put(Key.KEY_NAMES,course_name);
                            params.put(Key.KEY_MCQ_VERIFY,"No");
                            params.put(Key.KEY_ID,uid);

                            //returning parameter
                            return params;

                        }
                    };

                    //Adding the string request to the queue
                    RequestQueue requestQueue = Volley.newRequestQueue(MCQActivity.this);
                    requestQueue.add(stringRequest);
                }
            }
        });
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

//    protected void onStop() {
//        super.onStop();
//        handler.removeCallbacks(runnable);
//    }
    private void getData() {

        Information information = Information.getInstance();
        no_of_ques = information.getNo_of_questions();
        department = information.getDepartment();
        ids = information.getUid();
        cid = information.getCourse_id();

        Log.d("No_of_Ques", "" + no_of_ques);
        Log.d("ids", "" + ids);
        Log.d("cid", "" + cid);

        //for showing progress dialog
        loading = new ProgressDialog(MCQActivity.this);
        loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Key.LOAD_QUES+no_of_ques+"&id="+ids+"&cid="+cid;
        Log.d("query", "" + URL);


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
                        Toast.makeText(MCQActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(MCQActivity.this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Key.JSON_ARRAY);


            if (result.length()==0)
            {
                Toast.makeText(MCQActivity.this, "No Data Available!", Toast.LENGTH_SHORT).show();

            }

            else
            {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);


                    String question = jo.getString(Key.KEY_QUESTION);
                    String op1 = jo.getString(Key.KEY_OP1);
                    String op2 = jo.getString(Key.KEY_OP2);
                    String op3 = jo.getString(Key.KEY_OP3);
                    String op4 = jo.getString(Key.KEY_OP4);
                    String answer = jo.getString(Key.KEY_ANSWER);
                    String teacherId = jo.getString(Key.KEY_TEACHERID);
                    String course = jo.getString(Key.KEY_NAMES);
                    String date = jo.getString(Key.KEY_DATE);
                    String end_time = jo.getString(Key.KEY_END_TIME);

                    EVENT_DATE_TIME = date+" "+end_time;

                    Log.d("EVENT_DATE_TIME", "" + EVENT_DATE_TIME);

                    courseName.setText(course);
                    answers.add(answer);

                    questions.add(new Questions(question,op1,op2,op3,op4,answer,teacherId,course));
                }

                customAdapter = new CustomAdapter(this,questions);
                mcqListView.setAdapter(customAdapter);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {           //creating method of onBackPressed

        Intent intent =  new Intent(MCQActivity.this,ExaminationActivity.class);
        startActivity(intent);

    }
}
