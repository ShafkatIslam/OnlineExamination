package com.example.professt.onlineexamination;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TeacherProfileActivity extends AppCompatActivity {

    EditText etxtName, etxtCell, etxtEmail, etxtId, etxtDept,etxt_gender;
    String getID, getName, getCell, getEmail, getImage ,getMId, getDept;
    ImageView imageView1;
    private RadioGroup genderRadioGroup;
    private RadioButton genderRadioButton;

    Button btnEdit, btnUpdate;

    private  final int IMG_REQUEST = 1;
    private Bitmap bitmap;

    private ProgressDialog loading;

    private android.support.v7.app.AlertDialog.Builder alertdialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        etxtName = (EditText) findViewById(R.id.etxt_name);
        etxtCell = (EditText) findViewById(R.id.etxt_cell);
        etxtEmail = (EditText) findViewById(R.id.etxt_email);
        etxtId = (EditText) findViewById(R.id.etxt_id);
        etxtDept = (EditText) findViewById(R.id.etxt_dept);
        etxt_gender = (EditText) findViewById(R.id.etxt_gender);
        genderRadioGroup = (RadioGroup) findViewById(R.id.radiogroup_gender);

        imageView1 = (ImageView) findViewById(R.id.imageView1);

        btnEdit = (Button) findViewById(R.id.btn_edit);
        btnUpdate = (Button) findViewById(R.id.btn_update);

        imageView1.setOnClickListener(onClickProfileImageViewListener);
        imageView1.setEnabled(false);

        //For disable edit text
        etxtName.setEnabled(false);
        etxtCell.setEnabled(false);
        etxtEmail.setEnabled(false);
        etxtId.setEnabled(false);
        etxtDept.setEnabled(false);
        etxt_gender.setEnabled(false);
        genderRadioGroup.setVisibility(View.INVISIBLE);

        Reminder reminder = Reminder.getInstance();
        getCell = reminder.getCell();

        Log.d("CELL", getCell);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageView1.setEnabled(true);

                etxtName.setEnabled(true);
                etxtCell.setEnabled(true);
                etxtEmail.setEnabled(true);
                etxtId.setEnabled(true);
                etxtDept.setEnabled(true);
                genderRadioGroup.setVisibility(View.VISIBLE);
                etxt_gender.setVisibility(View.INVISIBLE);


                etxtName.setTextColor(Color.BLUE);
                etxtCell.setTextColor(Color.BLUE);
                etxtEmail.setTextColor(Color.BLUE);
                etxtId.setTextColor(Color.BLUE);
                etxtDept.setTextColor(Color.BLUE);


            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bitmap==null)
                {
//                    Toast.makeText(StudentProfileActivity.this,"Please Upload Image",Toast.LENGTH_SHORT).show();
                    if (etxtName.isEnabled()) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(TeacherProfileActivity.this);
                        builder.setIcon(R.drawable.load)
                                .setMessage("Want to Update Profile?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {


                                        // Perform Your Task Here--When Yes Is Pressed.
                                        UpdateContact1();
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Perform Your Task Here--When No is pressed
                                        dialog.cancel();
                                    }
                                }).show();

                    } else {
                        Toast.makeText(TeacherProfileActivity.this, "Please edit data!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    if (etxtName.isEnabled()) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(TeacherProfileActivity.this);
                        builder.setIcon(R.drawable.load)
                                .setMessage("Want to Update Contact?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {


                                        // Perform Your Task Here--When Yes Is Pressed.
                                        UpdateContact();
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Perform Your Task Here--When No is pressed
                                        dialog.cancel();
                                    }
                                }).show();

                    } else {
                        Toast.makeText(TeacherProfileActivity.this, "Please edit data!", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        getData();
    }

    private void UpdateContact1() {

        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        genderRadioButton = (RadioButton) findViewById(selectedId);

        String value = genderRadioButton.getText().toString();

        final String name = etxtName.getText().toString();
        final String cell = etxtCell.getText().toString();
        final String email = etxtEmail.getText().toString();
        final String mid = etxtId.getText().toString();
        final String dept = etxtDept.getText().toString();
        final String gender = value;


        if (name.isEmpty()) {
            Toast.makeText(this, "Name Can't Empty", Toast.LENGTH_SHORT).show();
        } else if (cell.isEmpty()) {
            Toast.makeText(this, "Cell Can't Empty", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Email Can't Empty", Toast.LENGTH_SHORT).show();
        }
        else if (mid.isEmpty()) {
            Toast.makeText(this, "MId Can't Empty", Toast.LENGTH_SHORT).show();
        }
        else if (dept.isEmpty()) {
            Toast.makeText(this, "Department Can't Empty", Toast.LENGTH_SHORT).show();
        }
        else if(gender.isEmpty())
        {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
        }

        else {
            loading = new ProgressDialog(this);
            // loading.setIcon(R.drawable.wait_icon);
            loading.setTitle("Update");
            loading.setMessage("Please wait....");
            loading.show();

            HttpsTrustManager.allowAllSSL();

            String URL = Key.UPDATE_URL3;


            //Creating a string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener <String>() {
                        @Override
                        public void onResponse(String response) {


                            //for track response in logcat
                            Log.d("RESPONSE", response);
                            // Log.d("RESPONSE", userCell);


                            //If we are getting success from server
                            if (response.equals("success")) {

                                loading.dismiss();
                                //Starting profile activity
                                genderRadioGroup.setVisibility(View.INVISIBLE);
                                etxt_gender.setVisibility(View.VISIBLE);

                                Intent intent = new Intent(TeacherProfileActivity.this, TeacherProfileActivity.class);
                                Toast.makeText(TeacherProfileActivity.this, " Successfully Updated!", Toast.LENGTH_SHORT).show();
                                startActivity(intent);

                            }


                            //If we are getting success from server
                            else if (response.equals("failure")) {

                                loading.dismiss();
                                //Starting profile activity

                                Intent intent = new Intent(TeacherProfileActivity.this, TeacherProfileActivity.class);
                                Toast.makeText(TeacherProfileActivity.this, " Update fail!", Toast.LENGTH_SHORT).show();
                                //startActivity(intent);

                            } else {

                                loading.dismiss();
                                Toast.makeText(TeacherProfileActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                            }

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want

                            Toast.makeText(TeacherProfileActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map <String, String> params = new HashMap<>();
                    //Adding parameters to request

                    params.put(Key.KEY_ID, getID);
                    params.put(Key.KEY_NAME, name);
                    params.put(Key.KEY_CELL, cell);
                    params.put(Key.KEY_EMAIL, email);
                    params.put(Key.KEY_MID, mid);
                    params.put(Key.KEY_DEPT, dept);
                    params.put(Key.KEY_GNDER, gender);


                    Log.d("ID", getID);

                    //returning parameter
                    return params;
                }
            };


            //Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(TeacherProfileActivity.this);
            requestQueue.add(stringRequest);
        }
    }

    View.OnClickListener onClickProfileImageViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            chooseImage();
        }
    };

    private void chooseImage()
    {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imageView1.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(imgBytes, android.util.Base64.DEFAULT);
    }

    private void UpdateContact() {

        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        genderRadioButton = (RadioButton) findViewById(selectedId);

        String value = genderRadioButton.getText().toString();

        final String name = etxtName.getText().toString();
        final String cell = etxtCell.getText().toString();
        final String email = etxtEmail.getText().toString();
        final String mid = etxtId.getText().toString();
        final String dept = etxtDept.getText().toString();
        final String gender = value;
        final String image = imageToString(bitmap);


        if (name.isEmpty()) {
            Toast.makeText(this, "Name Can't Empty", Toast.LENGTH_SHORT).show();
        } else if (cell.isEmpty()) {
            Toast.makeText(this, "Cell Can't Empty", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Email Can't Empty", Toast.LENGTH_SHORT).show();
        }
        else if (mid.isEmpty()) {
            Toast.makeText(this, "MId Can't Empty", Toast.LENGTH_SHORT).show();
        }
        else if (dept.isEmpty()) {
            Toast.makeText(this, "Department Can't Empty", Toast.LENGTH_SHORT).show();
        }
        else if(gender.isEmpty())
        {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
        }

        else if (bitmap==null)
        {
            Toast.makeText(this,"Please Upload Image",Toast.LENGTH_SHORT).show();
        }
        else {
            loading = new ProgressDialog(this);
            // loading.setIcon(R.drawable.wait_icon);
            loading.setTitle("Update");
            loading.setMessage("Please wait....");
            loading.show();

            HttpsTrustManager.allowAllSSL();

            String URL = Key.UPDATE_URL2;


            //Creating a string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener <String>() {
                        @Override
                        public void onResponse(String response) {


                            //for track response in logcat
                            Log.d("RESPONSE", response);
                            // Log.d("RESPONSE", userCell);


                            //If we are getting success from server
                            if (response.equals("success")) {

                                loading.dismiss();
                                //Starting profile activity
                                genderRadioGroup.setVisibility(View.INVISIBLE);
                                etxt_gender.setVisibility(View.VISIBLE);

                                Intent intent = new Intent(TeacherProfileActivity.this, TeacherProfileActivity.class);
                                Toast.makeText(TeacherProfileActivity.this, " Successfully Updated!", Toast.LENGTH_SHORT).show();
                                startActivity(intent);

                            }


                            //If we are getting success from server
                            else if (response.equals("failure")) {

                                loading.dismiss();
                                //Starting profile activity

                                Intent intent = new Intent(TeacherProfileActivity.this, TeacherProfileActivity.class);
                                Toast.makeText(TeacherProfileActivity.this, " Update fail!", Toast.LENGTH_SHORT).show();
                                //startActivity(intent);

                            } else {

                                loading.dismiss();
                                Toast.makeText(TeacherProfileActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                            }

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want

                            Toast.makeText(TeacherProfileActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map <String, String> params = new HashMap <>();
                    //Adding parameters to request

                    params.put(Key.KEY_ID, getID);
                    params.put(Key.KEY_NAME, name);
                    params.put(Key.KEY_CELL, cell);
                    params.put(Key.KEY_EMAIL, email);
                    params.put(Key.KEY_MID, mid);
                    params.put(Key.KEY_DEPT, dept);
                    params.put(Key.KEY_GNDER, gender);
                    params.put(Key.KEY_IMAGE, image);


                    Log.d("ID", getID);

                    //returning parameter
                    return params;
                }
            };


            //Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(TeacherProfileActivity.this);
            requestQueue.add(stringRequest);
        }
    }

    private void getData() {

        //for showing progress dialog
        loading = new ProgressDialog(TeacherProfileActivity.this);
        loading.setIcon(R.drawable.load);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

        String URL = Key.VIEW_URL1+getCell;

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
                        Toast.makeText(TeacherProfileActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(TeacherProfileActivity.this);
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
                Toast.makeText(TeacherProfileActivity.this, "No Data Available!", Toast.LENGTH_SHORT).show();

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

                    Log.d("Dept", dept);

                    int radius = 30; // corner radius, higher value = more rounded
                    int margin = 10; // crop margin, set to 0 for corners with no crop

                    Glide.with(TeacherProfileActivity.this).load((Key.MAIN_URL+"/api/"+image))
                            .placeholder(R.drawable.load)
                            .error(R.drawable.no_image)
                            .override(120, 120) // resizes the image to 100x200 pixels but does not respect aspect ratio
                            .fitCenter()   // scale to fit entire image within ImageView
                            .bitmapTransform(new RoundedCornersTransformation(this, radius, margin))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                            .into(imageView1);

                    getID = id;

                    etxtName.setText(name);
                    etxtCell.setText(cell);
                    etxtEmail.setText(email);
                    etxtId.setText(mid);
                    etxtDept.setText(dept);
                    etxt_gender.setText(gender);

                    Log.d("ID", getID);
                    Log.d("Image", image);


                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {           //creating method of onBackPressed

        alertdialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);    //creating object of alertDialogBuilder

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
