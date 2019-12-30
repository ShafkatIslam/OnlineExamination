package com.example.professt.onlineexamination;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.example.professt.onlineexamination.Examination.ExaminationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class HomeActivity extends AppCompatActivity {

    private ImageButton profile;
    private ImageButton about;
    private ImageButton examination;
    private ImageButton logout;

    private ProgressDialog loading;

    String getID, getName, getCell, getEmail, getImage ,getMId, getDept;

    private android.support.v7.app.AlertDialog.Builder alertdialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profile = (ImageButton) findViewById(R.id.profile);
        about =  (ImageButton) findViewById(R.id.about);
        examination = (ImageButton) findViewById(R.id.examination);
        logout = (ImageButton) findViewById(R.id.logout);

        Reminder reminder = Reminder.getInstance();
        getCell = reminder.getCell();

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,StudentProfileActivity.class);
                startActivity(intent);
            }
        });

        examination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ExaminationActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialogBuilder = new android.support.v7.app.AlertDialog.Builder(HomeActivity.this);    //creating object of alertDialogBuilder

                //setting the properties of alertDialogBuilder:

                //for setting title
                alertdialogBuilder.setTitle("Online Examination");

                //for setting message
                alertdialogBuilder.setMessage("Do you want to logout?");

                //for setting icon
                alertdialogBuilder.setIcon(R.drawable.exit);

                //for setting cancelable
                alertdialogBuilder.setCancelable(false);

                //for setting Button
                alertdialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //exit
//                finish();;
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                finish();;
//                System.exit(0);
//                onDestroy();
//                startActivity(intent);

                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        finish();
                        finishAffinity(); // Close all activites
                        System.exit(0);  // closing files, releasing resourc
                    }
                });
                alertdialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Not exit
                        // Toast.makeText(MainActivity.this,"You have clicked on no button",Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                alertdialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Not exit
                        //Toast.makeText(MainActivity.this,"You have clicked on cancel button",Toast.LENGTH_SHORT).show();
                    }
                });

                //showing alertDialog by creating alertDialog in object and creating alertDialogBuilder in this object
                android.support.v7.app.AlertDialog alertDialog = alertdialogBuilder.create();
                alertDialog.show();
            }
        });

        getData();
    }

    private void getData() {

        //for showing progress dialog
        loading = new ProgressDialog(HomeActivity.this);
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
                        Toast.makeText(HomeActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
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
                Toast.makeText(HomeActivity.this, "No Data Available!", Toast.LENGTH_SHORT).show();

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
                    String gender = jo.getString(Key.KEY_GNDER);
                    String image = jo.getString(Key.KEY_IMAGE);

                    Information information = Information.getInstance();
                    information.setMid(mid);
                    information.setDept(dept);



                    getID = id;

                    Log.d("ID", getID);
                    Log.d("Image", image);


                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        alertdialogBuilder = new android.support.v7.app.AlertDialog.Builder(HomeActivity.this);    //creating object of alertDialogBuilder

        //setting the properties of alertDialogBuilder:

        //for setting title
        alertdialogBuilder.setTitle("Online Examination");

        //for setting message
        alertdialogBuilder.setMessage("Do you want to logout?");

        //for setting icon
        alertdialogBuilder.setIcon(R.drawable.exit);

        //for setting cancelable
        alertdialogBuilder.setCancelable(false);

        //for setting Button
        alertdialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //exit
//                finish();;
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                finish();;
//                System.exit(0);
//                onDestroy();
//                startActivity(intent);

                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                finish();
                finishAffinity(); // Close all activites
                System.exit(0);  // closing files, releasing resourc
            }
        });
        alertdialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Not exit
                // Toast.makeText(MainActivity.this,"You have clicked on no button",Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        alertdialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Not exit
                //Toast.makeText(MainActivity.this,"You have clicked on cancel button",Toast.LENGTH_SHORT).show();
            }
        });

        //showing alertDialog by creating alertDialog in object and creating alertDialogBuilder in this object
        android.support.v7.app.AlertDialog alertDialog = alertdialogBuilder.create();
        alertDialog.show();


    }
}
