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
import com.example.professt.onlineexamination.Information;
import com.example.professt.onlineexamination.Key;
import com.example.professt.onlineexamination.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WrittenMarkDetailsActivity extends AppCompatActivity {

    TextView subjectsName,marks,ranks;

    String subjectsNames, markss, questionssId;

    String mid;

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_written_mark_details);

        subjectsName = (TextView)findViewById(R.id.subjectsName);
        marks = (TextView)findViewById(R.id.marks);
        ranks = (TextView)findViewById(R.id.ranks);

        subjectsNames = getIntent().getExtras().getString("coursesId");
        markss = getIntent().getExtras().getString("marksId");
        questionssId = getIntent().getExtras().getString("questionssId");

        Information information = Information.getInstance();
        mid = information.getMid();

        getData();
    }

    private void getData() {

        //for showing progress dialog
        loading = new ProgressDialog(WrittenMarkDetailsActivity.this);
        loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Key.WRITTEN_RANK+mid+"&Question_Paper_Id="+questionssId+"&Course_Name="+subjectsNames;

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

                        Intent intent = new Intent(WrittenMarkDetailsActivity.this,ScoreActivity.class);
                        startActivity(intent);
                        loading.dismiss();
                        Toast.makeText(WrittenMarkDetailsActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(WrittenMarkDetailsActivity.this);
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
                    String wmarks = jo.getString(Key.KEY_WRITTEN_MARKS);
                    String wranks = jo.getString(Key.KEY_WRITTEN_RANK);

                    subjectsName.setText(course);
                    marks.setText(wmarks);
                    ranks.setText(wranks);



                    Log.d("data",""+mid+" "+wmarks);


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
}
