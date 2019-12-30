package com.example.professt.onlineexamination;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class WelcomeActivity extends AppCompatActivity {

    Button enterButton;
    TextView nameTextView;

    String getID, getName, getCell, getEmail, getImage ,getMId, getDept;

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        nameTextView = (TextView)findViewById(R.id.nameTextView);
        enterButton = (Button)findViewById(R.id.enterId);

        Reminder reminder = Reminder.getInstance();
        getCell = reminder.getCell();

        getData();

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getData() {

        //for showing progress dialog
        loading = new ProgressDialog(WelcomeActivity.this);
        loading.setIcon(R.drawable.load);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Key.VIEW_URL+getCell;

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
                        Toast.makeText(WelcomeActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(WelcomeActivity.this);
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
                Toast.makeText(WelcomeActivity.this, "No Data Available!", Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(StudentProfileActivity.this, ProfileActivity.class);
//
//                startActivity(intent);
                //imgNoData.setImageResource(R.drawable.nodata);
            }

            else {

                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);
                    String id = jo.getString(Key.KEY_ID);
                    String name = jo.getString(Key.KEY_NAME);
                    String cell = jo.getString(Key.KEY_CELL);
                    String email = jo.getString(Key.KEY_EMAIL);
                    String mid = jo.getString(Key.KEY_MID);
                    String dept = jo.getString(Key.KEY_DEPT);
                    String image = jo.getString(Key.KEY_IMAGE);

                    nameTextView.setText("Hello "+name);



                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
