package com.example.professt.onlineexamination.Evaluated_Answer_Script;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.example.professt.onlineexamination.Examination.WrittenActivity;
import com.example.professt.onlineexamination.Information;
import com.example.professt.onlineexamination.Key;
import com.example.professt.onlineexamination.R;
import com.example.professt.onlineexamination.Score.ScoreActivity;
import com.example.professt.onlineexamination.Score.Written;
import com.example.professt.onlineexamination.Score.WrittenCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Evaluated_Answer_Script extends AppCompatActivity {

    ListView answerListView;

    Answer_Script answer_script;

    ArrayList<Answer> answers = new ArrayList<Answer>();

    int MAX_SIZE=999;

    public String answerIds[]=new String[MAX_SIZE];

    String mid;

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluated__answer__script);

        answerListView = (ListView)findViewById(R.id.answerListView);

        Information information = Information.getInstance();
        mid = information.getMid();

        getData();
    }

    private void getData() {

        //for showing progress dialog
        loading = new ProgressDialog(Evaluated_Answer_Script.this);
        loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Key.WRITTEN_SCRIPT+mid;

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

                        Intent intent = new Intent(Evaluated_Answer_Script.this,WrittenActivity.class);
                        startActivity(intent);
                        loading.dismiss();
                        Toast.makeText(Evaluated_Answer_Script.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(Evaluated_Answer_Script.this);
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
                    String mid = jo.getString(Key.KEY_MID);
                    String answer_script_id = jo.getString(Key.KEY_ANSWER_SCRIPT_ID);


                    answerIds[i] = answer_script_id;

                    Log.d("data",""+mid+" "+answer_script_id);

                    answers.add(new Answer(answer_script_id,mid));

                }

                answer_script = new Answer_Script(this,answers);
                answerListView.setAdapter(answer_script);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        answerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String answerid = answerIds[position];

                String pdf = "https://www.tikabarta.com/mocktest/api/Answers/"+answerid+".pdf"; //YOUR URL TO PDF
                String googleDocsUrl = "http://docs.google.com/viewer?url="+ pdf;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(googleDocsUrl ), "text/html");
                startActivity(intent);
            }
        });
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

        Intent intent =  new Intent(Evaluated_Answer_Script.this,WrittenActivity.class);
        startActivity(intent);

    }
}
