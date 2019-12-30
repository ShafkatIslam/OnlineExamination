package com.example.professt.onlineexamination.Score;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.professt.onlineexamination.Examination.ExaminationActivity;
import com.example.professt.onlineexamination.Information;
import com.example.professt.onlineexamination.Key;
import com.example.professt.onlineexamination.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    ListView writtenListView,mcqListView;

    MCQCustomAdapter mcqCustomAdapter;
    WrittenCustomAdapter writtenCustomAdapter;

    ArrayList<Written> writtens = new ArrayList<Written>();
    ArrayList<Mcq> mcqs = new ArrayList<Mcq>();

    String mid;

    private ProgressDialog loading;
    private ProgressDialog loading1;

    int MAX_SIZE=999;
    public String questionsId[]=new String[MAX_SIZE];
    public String courseId[]=new String[MAX_SIZE];
    public String markId[]=new String[MAX_SIZE];
    public String questionssId[]=new String[MAX_SIZE];
    public String coursesId[]=new String[MAX_SIZE];
    public String marksId[]=new String[MAX_SIZE];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        writtenListView = (ListView)findViewById(R.id.writtenListView);
        mcqListView = (ListView)findViewById(R.id.mcqListViews);


        Information information = Information.getInstance();
        mid = information.getMid();


        getData();
        getData1();

    }

    private void getData1() {

        //for showing progress dialog
        loading1 = new ProgressDialog(ScoreActivity.this);
        loading1.setIcon(R.drawable.wait_icon);
        loading1.setTitle("Loading");
        loading1.setMessage("Please wait....");
        loading1.show();

        String URL = Key.MCQ_MARK+mid;

        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading1.dismiss();
                showJSON1(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Intent intent = new Intent(ScoreActivity.this,ExaminationActivity.class);
                        startActivity(intent);
                        loading1.dismiss();
                        Toast.makeText(ScoreActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(ScoreActivity.this);
        requestQueue.add(stringRequest);
    }

    private void showJSON1(String response) {

        //Create json object for receiving jason data
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Key.JSON_ARRAY);

            if (result.length()==0)
            {
                showData("Sorry","No MCQ Marks Found");
            }
            else
            {

                for (int i = 0; i < result.length(); i++) {

                    JSONObject jo = result.getJSONObject(i);
                    String course = jo.getString(Key.KEY_COURSE_NAME);
                    String marks = jo.getString(Key.KEY_MCQ_MARKS);
                    String question_id = jo.getString(Key.KEY_QUESTION_PAPER_ID);

                    //insert data into array for put extra
                    questionsId[i] = question_id;
                    courseId[i] = course;
                    markId[i] = marks;



                    Log.d("data",""+mid+" "+marks);

                    mcqs.add(new Mcq(course,marks));

                }

                mcqCustomAdapter = new MCQCustomAdapter(this,mcqs);
                mcqListView.setAdapter(mcqCustomAdapter);
            }

            mcqListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent=new Intent(ScoreActivity.this,MCQMarkDetailsActivity.class);
                    intent.putExtra("questionsId",questionsId[position]);
                    intent.putExtra("courseId",courseId[position]);
                    intent.putExtra("markId",markId[position]);

                    Toast.makeText(ScoreActivity.this,courseId[position]+" is clicked",Toast.LENGTH_SHORT).show();
                    //for logcat
                    Log.d("questionsId",questionsId[position]);
                    Log.d("courseId",courseId[position]);
                    Log.d("markId",markId[position]);

                    startActivity(intent);
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getData() {

        //for showing progress dialog
        loading = new ProgressDialog(ScoreActivity.this);
        loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Key.WRITTEN_MARK+mid;

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

                        Intent intent = new Intent(ScoreActivity.this,ExaminationActivity.class);
                        startActivity(intent);
                        loading.dismiss();
                        Toast.makeText(ScoreActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(ScoreActivity.this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {

        //Create json object for receiving jason data
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Key.JSON_ARRAY);

            if (result.length()==0)
            {
                showData("Sorry","No Written Marks Found");
            }
            else
            {

                for (int i = 0; i < result.length(); i++) {

                    JSONObject jo = result.getJSONObject(i);
                    String course = jo.getString(Key.KEY_COURSE_NAME);
                    String marks = jo.getString(Key.KEY_WRITTEN_MARKS);
                    String question_id = jo.getString(Key.KEY_QUESTION_PAPER_ID);

                    //insert data into array for put extra
                    questionssId[i] = question_id;
                    coursesId[i] = course;
                    marksId[i] = marks;



                    Log.d("data",""+mid+" "+marks);

                    writtens.add(new Written(course,marks));

                }

                writtenCustomAdapter = new WrittenCustomAdapter(this,writtens);
                writtenListView.setAdapter(writtenCustomAdapter);
            }

            writtenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent=new Intent(ScoreActivity.this,WrittenMarkDetailsActivity.class);
                    intent.putExtra("questionssId",questionssId[position]);
                    intent.putExtra("coursesId",coursesId[position]);
                    intent.putExtra("marksId",marksId[position]);

                    Toast.makeText(ScoreActivity.this,courseId[position]+" is clicked",Toast.LENGTH_SHORT).show();
                    //for logcat
                    Log.d("questionssId",questionssId[position]);
                    Log.d("coursesId",coursesId[position]);
                    Log.d("marksId",marksId[position]);

                    startActivity(intent);
                }
            });
        }
        catch (JSONException e) {
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

    @Override
    public void onBackPressed() {           //creating method of onBackPressed

        Intent intent =  new Intent(ScoreActivity.this,ExaminationActivity.class);
        startActivity(intent);

    }
}
