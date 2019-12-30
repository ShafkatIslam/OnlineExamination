package com.example.professt.onlineexamination.Score;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
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

public class MCQMarkDetailsActivity extends AppCompatActivity {

    TextView subjectName,mark,rank;

    String subjectNames, marks, questionsId;

    String mid;

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcqmark_details);

        subjectName = (TextView)findViewById(R.id.subjectName);
        mark = (TextView)findViewById(R.id.mark);
        rank = (TextView)findViewById(R.id.rank);

        subjectNames = getIntent().getExtras().getString("courseId");
        marks = getIntent().getExtras().getString("markId");
        questionsId = getIntent().getExtras().getString("questionsId");

        Information information = Information.getInstance();
        mid = information.getMid();

        getData();
    }

    private void getData() {

        //for showing progress dialog
        loading = new ProgressDialog(MCQMarkDetailsActivity.this);
        loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Key.MCQ_RANK+mid+"&Question_Paper_Id="+questionsId+"&Course_Name="+subjectNames;

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

                        Intent intent = new Intent(MCQMarkDetailsActivity.this,ScoreActivity.class);
                        startActivity(intent);
                        loading.dismiss();
                        Toast.makeText(MCQMarkDetailsActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(MCQMarkDetailsActivity.this);
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
                showData("Sorry","No MCQ Marks Found");
            }
            else
            {

                for (int i = 0; i < result.length(); i++) {

                    JSONObject jo = result.getJSONObject(i);
                    String course = jo.getString(Key.KEY_COURSE_NAME);
                    String marks = jo.getString(Key.KEY_MCQ_MARKS);
                    String ranks = jo.getString(Key.KEY_MCQ_RANK);

                    subjectName.setText(course);
                    mark.setText(marks);
                    rank.setText(ranks);



                    Log.d("data",""+mid+" "+marks);


                }


            }
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

        Intent intent =  new Intent(MCQMarkDetailsActivity.this,ScoreActivity.class);
        startActivity(intent);

    }
}
