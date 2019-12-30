package com.example.professt.onlineexamination;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karan.churi.PermissionManager.PermissionManager;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private CheckBox rememberMeCheckBox;
    private TextView forgetPassword;
    private Button loginButton;
    private Button signUpButton;
    private ProgressDialog progressDialog;
    private android.support.v7.app.AlertDialog.Builder alertdialogBuilder;

    private ProgressDialog loading;

    PermissionManager permissionManager;

    String username,passwords;


    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        loginButton =  (Button) findViewById(R.id.loginbtn);
        signUpButton =  (Button) findViewById(R.id.signupbtn);
        forgetPassword =  (TextView) findViewById(R.id.forgetPassword);

        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(this);

        loginButton.setOnClickListener(onClickLoginButtonListener);
        signUpButton.setOnClickListener(onClickSignUpButtonListener);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MainForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

//        otpForget.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                username = mEmailView.getText().toString().trim();
//
//                if(username.isEmpty())
//                {
//                    mEmailView.setError("Please Enter Cell");
//                    mEmailView.requestFocus();
//                }
//                else
//                {
//                    Intent intent = new Intent(MainActivity.this,EmailVerification.class);
//                    intent.putExtra("email",username);
//                    startActivity(intent);
//                }
//
//            }
//        });

        SharedPreferences sharedPreferences = getSharedPreferences("user_data",MODE_PRIVATE);
        boolean remembered = sharedPreferences.getBoolean("remembered",false);
        if(remembered)
        {
            //rememberMeCheckBox.setChecked(true);
            String username = sharedPreferences.getString("username", null);
            String password = sharedPreferences.getString("password", null);
            //login(username,password);
        }
    }

    private void goToSignupActivity()
    {
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }

    private void goToMainMenuActivity()
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    View.OnClickListener onClickLoginButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            username = mEmailView.getText().toString().trim();
            passwords = mPasswordView.getText().toString().trim();


            if(username.isEmpty())
            {
                mEmailView.setError("Please Enter Cell");
                mEmailView.requestFocus();
            }

            //checking password field/validation
            else if(passwords.isEmpty())
            {
                mPasswordView.setError("Please Enter Password");
                mPasswordView.requestFocus();
            }
            else
            {
                loading = new ProgressDialog(MainActivity.this);
                loading.setIcon(R.drawable.load);
                loading.setTitle("Login");
                loading.setMessage("Please wait...");
                loading.show();

                //creating a string request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Key.LOGIN_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("RESPONSE", "" + response);

                        //if we are getting success from server
                        if (response.equals("success")) {

                            //creating a shared preference

                            SharedPreferences sp = MainActivity.this.getSharedPreferences(Key.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sp.edit();
                            //Adding values to editor
                            editor.putString(Key.USER_SHARED_PREF, username);

                            //Saving values to editor
                            editor.commit();

                            loading.dismiss();

                            //starting profile activity
                            Intent intent = new Intent(MainActivity.this, Teacher_WelcomeActivity.class);
//                            intent.putExtra("cell",username);
                            Reminder reminder = Reminder.getInstance();
                            reminder.setCell(username);
                            startActivity(intent);

                            Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();

                        }
                        else if (response.equals("success1")) {

                                //creating a shared preference

                                SharedPreferences sp = MainActivity.this.getSharedPreferences(Key.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                //Creating editor to store values to shared preferences
                                SharedPreferences.Editor editor = sp.edit();
                                //Adding values to editor
                                editor.putString(Key.USER_SHARED_PREF, username);

                                //Saving values to editor
                                editor.commit();

                                loading.dismiss();

                                //starting profile activity
                                Intent intent = new Intent(MainActivity.this,WelcomeActivity.class);
//                                intent.putExtra("cell",username);
                                Reminder reminder = Reminder.getInstance();
                                reminder.setCell(username);
                                startActivity(intent);

                                Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();

                            }
                        else if (response.equals("fail")) {

                            //if the server response is not success
                            //displaying an error message or toast
                            Toast.makeText(MainActivity.this, "Not Permitted to login", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                        else if (response.equals("fail1")) {

                            //if the server response is not success
                            //displaying an error message or toast
                            Toast.makeText(MainActivity.this, "Not Permitted to login", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                        else if (response.equals("failure")) {

                            //if the server response is not success
                            //displaying an error message or toast
                            Toast.makeText(MainActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        } else {
                            //if the server response is not success
                            //displaying an error message or toast
                            Toast.makeText(MainActivity.this, "Invalid Usercell or password", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "There is an error", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //return super.getParams();

                        Map<String,String> params = new HashMap<>();

                        //Ading parameters to request
                        params.put(Key.KEY_CELL,username);
                        params.put(Key.KEY_PASSWORD,passwords);

                        //returning parameter
                        return params;

                    }
                };

                //Adding the string request to the queue
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(stringRequest);

            }

            ///vollry();
        }
    };

    View.OnClickListener onClickSignUpButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            goToSignupActivity();
        }
    };

    @Override
    public void onBackPressed() {           //creating method of onBackPressed

        alertdialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);    //creating object of alertDialogBuilder

        //setting the properties of alertDialogBuilder:

        //for setting title
        alertdialogBuilder.setTitle("Online Examination");

        //for setting message
        alertdialogBuilder.setMessage("Do you want to exit?");

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
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//                finish();;
//                System.exit(0);
//                onDestroy();
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
