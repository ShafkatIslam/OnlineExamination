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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

public class TeacherSignUp extends AppCompatActivity {

    private ImageView profileImageView;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText cnicEditText;
    private EditText cellnoEditText;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private Button signupButton;
    private ProgressDialog progressDialog;

    private  final int IMG_REQUEST = 1;
    private Bitmap bitmap;

    //String[] deptNames;
    ArrayList<String> deptNames = new ArrayList<String>();
    private Spinner spinner;

    private ProgressDialog loading;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String username,passwords,emails,cnic,dept,cellno,gender,images;

    private static final int SELECT_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_sign_up);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);

        profileImageView = (ImageView) findViewById(R.id.imageview_profile11);
        usernameEditText = (EditText) findViewById(R.id.edittext_username11);
        passwordEditText = (EditText) findViewById(R.id.edittext_password11);
        emailEditText = (EditText) findViewById(R.id.edittext_email11);
        cnicEditText = (EditText) findViewById(R.id.edittext_cnic11);
        cellnoEditText = (EditText) findViewById(R.id.edittext_cellno11);
        genderRadioGroup = (RadioGroup) findViewById(R.id.radiogroup_gender11);
        //maleRadioButton = (RadioButton) findViewById(R.id.radiobutton_male);
        //femaleRadioButton = (RadioButton) findViewById(R.id.radiobutton_female);

        spinner = (Spinner) findViewById(R.id.spinnerId1);
        signupButton = (Button) findViewById(R.id.btn_signup11);
        signupButton.setOnClickListener(onClickSignUpButtonListener);
        profileImageView.setOnClickListener(onClickProfileImageViewListener);

        getData();
    }

    private void getData() {

        //for showing progress dialog
        loading = new ProgressDialog(TeacherSignUp.this);
        loading.setIcon(R.drawable.wait_icon);
        loading.setTitle("Loading");
        loading.setMessage("Please wait....");
        loading.show();

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
                        Toast.makeText(TeacherSignUp.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(TeacherSignUp.this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Key.JSON_ARRAY);


            if (result.length()==0)
            {
                Toast.makeText(TeacherSignUp.this, "No Data Available!", Toast.LENGTH_SHORT).show();

            }

            else
            {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);
                    String dept = jo.getString(Key.KEY_DEPT);

                    deptNames.add(dept);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeacherSignUp.this,R.layout.sample_view,R.id.textViewId,deptNames); //creating object of ArrayAdapter....thre are 4 parameters in ArrayAdapter
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
//        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, SELECT_PICTURE);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK)
//            try {
//                // We need to recyle unused bitmaps
//                if (bitmap != null) {
//                    bitmap.recycle();
//                }
//                InputStream stream = getContentResolver().openInputStream(data.getData());
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize=2;
//                bitmap = BitmapFactory.decodeStream(stream, null, options);
//                int width = bitmap.getWidth();
//                int height = bitmap.getHeight();
//                double scale = 100.0/height;
//                height = (int)(height*scale);
//                width = (int)(width*scale);
//                bitmap = Bitmap.createScaledBitmap(bitmap, width,height, false);
//                stream.close();
//                profileImageView.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

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
            emails = emailEditText.getText().toString();
            cnic = cnicEditText.getText().toString();
            cellno = cellnoEditText.getText().toString();
            dept = spinner.getSelectedItem().toString();
            gender = value;

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
                cnicEditText.setError("Please Enter Address");
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
            else if(gender.isEmpty())
            {
                Toast.makeText(TeacherSignUp.this, "Please select gender", Toast.LENGTH_SHORT).show();
            }

            else if (bitmap==null)
            {
                Toast.makeText(TeacherSignUp.this,"Please Upload Image",Toast.LENGTH_SHORT).show();
            }

            else
            {
                images = imageToString(bitmap);
                loading = new ProgressDialog(TeacherSignUp.this);
                loading.setIcon(R.drawable.load);
                loading.setTitle("Sign Up");
                loading.setMessage("Please wait...");
                loading.show();


                StringRequest stringRequest = new StringRequest(Request.Method.POST, Key.SIGNUP_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //for track response in Logcat
                        Log.d("RESPONSE", "" + response);

                        //if we are getting success from server
                        if (response.equals("success")) {
                            //creating a shared preference
                            loading.dismiss();
                            //starting profile activity
                            Intent intent = new Intent(TeacherSignUp.this, MainActivity.class);
//                            intent.putExtra("email",emails);
                            startActivity(intent);

                            Toast.makeText(TeacherSignUp.this, "Registration Successfull", Toast.LENGTH_SHORT).show();

                            usernameEditText.setText("");
                            passwordEditText.setText("");
                            emailEditText.setText("");
                            cnicEditText.setText("");
                            cellnoEditText.setText("");

                        } else if (response.equals("exists")) {
                            Toast.makeText(TeacherSignUp.this, "User already exists", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                        else if (response.equals("existss")) {
                            Toast.makeText(TeacherSignUp.this, "M-ID already exists", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }else if (response.equals("failure")) {
                            Toast.makeText(TeacherSignUp.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(TeacherSignUp.this, "There is an error", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
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
                        params.put(Key.KEY_IMAGE,images);
                        params.put(Key.KEY_GNDER,gender);
                        params.put(Key.KEY_STATUS,"Teacher");
                        params.put(Key.KEY_CSTATUS,"No");

                        //returning parameter
                        return params;

                    }
                };

                //Adding the string request to the queue
                RequestQueue requestQueue = Volley.newRequestQueue(TeacherSignUp.this);
                requestQueue.add(stringRequest);
            }
        }


    };

    @Override
    public void onBackPressed() {           //creating method of onBackPressed

        Intent intent =  new Intent(TeacherSignUp.this,SignUpActivity.class);
        startActivity(intent);

    }
}
