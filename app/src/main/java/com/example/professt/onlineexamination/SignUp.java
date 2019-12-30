package com.example.professt.onlineexamination;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private ImageView profileImageView;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText cnicEditText;
    private EditText cellnoEditText;
    private EditText deptEditText;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private Button signupButton;
    private ProgressDialog progressDialog;

    //String[] deptNames;
    ArrayList<String> deptNames = new ArrayList<String>();
    private Spinner spinner;

    private ProgressDialog loading;

    String username,passwords,emails,cnic,cellno,dept,gender,images;

    private  final int IMG_REQUEST = 1;
    private Bitmap bitmap;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    int MY_SOCKET_TIMEOUT_MS=500;

    private static final int SELECT_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);

        profileImageView = (ImageView) findViewById(R.id.imageview_profile);
        usernameEditText = (EditText) findViewById(R.id.edittext_username);
        passwordEditText = (EditText) findViewById(R.id.edittext_password);
        emailEditText = (EditText) findViewById(R.id.edittext_email);
        cnicEditText = (EditText) findViewById(R.id.edittext_cnic);
        //deptEditText = (EditText) findViewById(R.id.edittext_dept);
        cellnoEditText = (EditText) findViewById(R.id.edittext_cellno);
        genderRadioGroup = (RadioGroup) findViewById(R.id.radiogroup_gender);
        spinner = (Spinner) findViewById(R.id.spinnerId);
        //maleRadioButton = (RadioButton) findViewById(R.id.radiobutton_male);
        //femaleRadioButton = (RadioButton) findViewById(R.id.radiobutton_female);
        signupButton = (Button) findViewById(R.id.btn_signup);
        signupButton.setOnClickListener(onClickSignUpButtonListener);
        profileImageView.setOnClickListener(onClickProfileImageViewListener);

        getData();

    }

    private void getData() {

        //for showing progress dialog
        loading = new ProgressDialog(SignUp.this);
        loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

        HttpsTrustManager.allowAllSSL();

        String URL = Key.LOAD_DEPT;


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
                        Toast.makeText(SignUp.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                MY_SOCKET_TIMEOUT_MS,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Key.JSON_ARRAY);


            if (result.length()==0)
            {
                Toast.makeText(SignUp.this, "No Data Available!", Toast.LENGTH_SHORT).show();

            }

            else
            {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);
                    String dept = jo.getString(Key.KEY_DEPT);

                    deptNames.add(dept);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUp.this,R.layout.sample_view,R.id.textViewId,deptNames); //creating object of ArrayAdapter....thre are 4 parameters in ArrayAdapter
                spinner.setAdapter(adapter);   //setting the adapter in spinner
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
                profileImageView.setImageBitmap(bitmap);
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


    View.OnClickListener onClickSignUpButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int selectedId = genderRadioGroup.getCheckedRadioButtonId();
            maleRadioButton = (RadioButton) findViewById(selectedId);

            String value = maleRadioButton.getText().toString();

            username = usernameEditText.getText().toString();
            passwords = passwordEditText.getText().toString();
            emails = emailEditText.getText().toString().trim();
            cnic = cnicEditText.getText().toString();
            cellno = cellnoEditText.getText().toString();
            //dept = deptEditText.getText().toString();
            dept = spinner.getSelectedItem().toString();
            gender = value;

            Log.d("CELL", "" + cellno);
            Log.d("MID", "" + cnic);
            if(username.isEmpty())
            {
                usernameEditText.setError("Please Enter Name");
                usernameEditText.requestFocus();
            }
            else if(passwords.isEmpty())
            {
                passwordEditText.setError("Please Enter Password");
                passwordEditText.requestFocus();
            }
            else if(emails.isEmpty())
            {
                emailEditText.setError("Please Enter Email");
                emailEditText.requestFocus();
            }
            else if(cnic.isEmpty())
            {
                cnicEditText.setError("Please Enter Student ID");
                cnicEditText.requestFocus();
            }
            else if(cellno.isEmpty())
            {
                cellnoEditText.setError("Please Enter Cell No");
                cellnoEditText.requestFocus();
            }
            else if((cellno.length()!=11) || (!(cellno.startsWith("018")) && !(cellno.startsWith("016")) && !(cellno.startsWith("017")) && !(cellno.startsWith("015")) && !(cellno.startsWith("019")) && !(cellno.startsWith("013")) && !(cellno.startsWith("014"))) )
            {
                cellnoEditText.setError("Wrong Cell Number");
                cellnoEditText.requestFocus();
            }
            if (!(emails.matches(emailPattern)))
            {
                Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
                emailEditText.setError("Invalid Email");
                emailEditText.requestFocus();

            }
            else if(dept.isEmpty())
            {
                Toast.makeText(SignUp.this, "Please select department", Toast.LENGTH_SHORT).show();
            }
            else if(gender.isEmpty())
            {
                Toast.makeText(SignUp.this, "Please select gender", Toast.LENGTH_SHORT).show();
            }

            else if (bitmap==null)
            {
                Toast.makeText(SignUp.this,"Please Upload Image",Toast.LENGTH_SHORT).show();
            }
            else
            {
                images = imageToString(bitmap);
                loading = new ProgressDialog(SignUp.this);
                loading.setIcon(R.drawable.load);
                loading.setTitle("Sign Up");
                loading.setMessage("Please wait...");
                loading.show();


                HttpsTrustManager.allowAllSSL();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Key.SIGNUP_URL1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //for track response in Logcat
                        Log.d("RESPONSE", "" + response);

                        //if we are getting success from server
                        if (response.equals("success")) {
                            //creating a shared preference
                            loading.dismiss();
                            //starting profile activity
                            Intent intent = new Intent(SignUp.this, MainActivity.class);
//                            intent.putExtra("email",emails);
                            startActivity(intent);

                            Toast.makeText(SignUp.this, "Registration Successfull", Toast.LENGTH_SHORT).show();

                            usernameEditText.setText("");
                            passwordEditText.setText("");
                            emailEditText.setText("");
                            cnicEditText.setText("");
                            cellnoEditText.setText("");

                        } else if (response.equals("exists")) {
                            Toast.makeText(SignUp.this, "User already exists", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                        else if (response.equals("existss")) {
                            Toast.makeText(SignUp.this, "M-ID already exists", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                        else if (response.equals("failure")) {
                            Toast.makeText(SignUp.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(SignUp.this, "There is an error", Toast.LENGTH_SHORT).show();
                                Toast.makeText(SignUp.this,error.toString(),Toast.LENGTH_LONG).show();
                                loading.dismiss();

//                                if (error instanceof NetworkError) {
//                                } else if (error instanceof ServerError) {
//                                } else if (error instanceof AuthFailureError) {
//                                } else if (error instanceof ParseError) {
//                                } else if (error instanceof NoConnectionError) {
//                                } else if (error instanceof TimeoutError) {
//                                    Toast.makeText(SignUp.this,
//                                            "Oops. Timeout error!",
//                                            Toast.LENGTH_LONG).show();
//                                }
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //return super.getParams();

                        Map<String,String> params = new HashMap<>();

                        //Ading parameters to request
                        params.put(Key.KEY_NAME,username);
                        params.put(Key.KEY_PASSWORD,passwords);
                        params.put(Key.KEY_EMAIL,emails);
                        params.put(Key.KEY_MID,cnic);
                        params.put(Key.KEY_CELL,cellno);
                        params.put(Key.KEY_DEPT,dept);
                        params.put(Key.KEY_GNDER,gender);
                        params.put(Key.KEY_IMAGE,images);
                        params.put(Key.KEY_STATUS,"No");

                        //returning parameter
                        return params;

                    }
                };

//                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                        MY_SOCKET_TIMEOUT_MS,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                //Adding the string request to the queue
                RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
                requestQueue.add(stringRequest);
            }
        }


    };

    @Override
    public void onBackPressed() {           //creating method of onBackPressed

        Intent intent =  new Intent(SignUp.this,SignUpActivity.class);
        startActivity(intent);

    }
}
