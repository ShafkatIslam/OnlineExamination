package com.example.professt.onlineexamination.Examination;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.professt.onlineexamination.HttpsTrustManager;
import com.example.professt.onlineexamination.Information;
import com.example.professt.onlineexamination.Key;
import com.example.professt.onlineexamination.MainActivity;
import com.example.professt.onlineexamination.R;
import com.example.professt.onlineexamination.SignUp;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;


import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;


//import in.gauriinfotech.commons.Commons;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.professt.onlineexamination.Key.UPLOAD_URL;

public class WrittenExaminationActivity extends AppCompatActivity implements View.OnClickListener {

    //Pdf request code
    private int PICK_PDF_REQUEST = 1;

    public static final int PERMISSIONS_REQUEST_CODE = 0;
    public static final int FILE_PICKER_REQUEST_CODE = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;


    //Uri to store the image uri
    private Uri filePath;
    private String filePath1;

    //String path;

    private static final int INTENT_REQUEST_GET_IMAGES = 13;

    private static final String TAG = "TedPicker";
    ArrayList<Uri> image_uris = new ArrayList<Uri>();
    private ViewGroup mSelectedImagesContainer;

    Button uploadId,selectId,loadId;

    TextView textViewFilePath;

    File pdfFolder;

    private boolean isPortrait = true;

    private ProgressDialog loading;

    File myPDF;

    String fileName;

    String mid,question_paper,question_paper_id;

    String path,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_written_examination);

        mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);
        View getImages = findViewById(R.id.get_images);
        getImages.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                getImages(new Config());
            }
        });


        uploadId = (Button)findViewById(R.id.uploadId);
        selectId = (Button)findViewById(R.id.selectId);
        loadId = (Button)findViewById(R.id.loadId);

        textViewFilePath = (TextView)findViewById(R.id.textViewFilePath);

        selectId.setOnClickListener(this);
        loadId.setOnClickListener(this);

        Information information = Information.getInstance();
        mid = information.getMid();
        question_paper = information.getQuestion_id();

        question_paper_id = question_paper+"_"+mid;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }


        uploadId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(image_uris.isEmpty())
                    {
                        Toast.makeText(WrittenExaminationActivity.this, "Please Select or Capture Image", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        //start renaming
                        LayoutInflater li = LayoutInflater.from(WrittenExaminationActivity.this);
                        View promptsView = li.inflate(R.layout.layout, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                WrittenExaminationActivity.this);

                        // set prompts.xml to alertdialog builder
                        alertDialogBuilder.setView(promptsView);

                        final EditText userInput = (EditText) promptsView
                                .findViewById(R.id.editTextDialogUserInput);

                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                // get user input and set it to result
                                                // edit text

                                                 fileName = userInput.getText().toString();
                                                //myPDF = new File(pdfFolder + "/" + fileName + ".pdf");
                                                //File newFile = new File(pdfFolder + "/" + fileName + ".pdf");
                                                //boolean result = myPDF.renameTo(newFile);

                                                //myPDF = newFile;

                                                try {
                                                    createPdf();
                                                } catch (FileNotFoundException e) {
                                                    e.printStackTrace();
                                                }
                                                Toast.makeText(WrittenExaminationActivity.this, "Pdf file created", Toast.LENGTH_SHORT).show();

                                                Log.w(TAG, "myPDF renamed to: " + myPDF.toString() );
                                                promptForNextAction();
                                            }
                                        })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                dialog.cancel();
                                                //promptForNextAction();


                                            }
                                        });

                        // create alert dialog
                        //AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it

                        Log.e(TAG, "Before alertdialogue.show");


                        alertDialogBuilder.show();

                    }

            }
        });


        View getImages2 = findViewById(R.id.get_images2);
        getImages2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Config config = new Config();
                config.setCameraHeight(R.dimen.app_camera_height);
                config.setToolbarTitleRes(R.string.custom_title);
                config.setSelectionMin(2);
                config.setSelectionLimit(20);
                config.setSelectedBottomHeight(R.dimen.bottom_height);
                config.setFlashOn(true);


                getImages(config);
            }
        });

    }


    private void createPdf() throws FileNotFoundException {


        // creates folder with a pathname including the android storage directory
        pdfFolder = new File(Environment.getExternalStorageDirectory(), "EasyConvert"); // check this warning, may be important for diff API levels

        //ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);

        // if the directory doesn't already exist, create it
        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs();
            Log.i(TAG, "Folder successfully created");
        }

        // as long as we have images in the recycle view...
        if (image_uris != null) {

            // progress.setVisibility(View.VISIBLE);

            // name the pdf with the current timestamp by default
            Date date = new Date();
            final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
            myPDF = new File(pdfFolder + "/" + fileName + ".pdf");



            // point an output stream to our created document
            OutputStream output = new FileOutputStream(myPDF);
            com.itextpdf.text.Document document;

            // create a document with difference page sizes depending on orientation
            if (isPortrait)
                document = new com.itextpdf.text.Document(PageSize.A4, 50, 50, 50, 50);
            else
                document = new com.itextpdf.text.Document(PageSize.A4.rotate(), 50, 50, 50, 50);

            try {
                PdfWriter.getInstance(document, output);
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            long startTime, estimatedTime;

            document.open();
            //document.add(new Paragraph("~~~~Hello World!!~~~~"));


            // loop through all the images in the array
            for (int i = 0; i < image_uris.size(); i++) {

                // create bitmap from URI in our list
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(new File(String.valueOf(image_uris.get(i)))));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                startTime = System.currentTimeMillis();

                // changed from png to jpeg, lowered processing time greatly
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                estimatedTime = System.currentTimeMillis() - startTime;

                Log.e(TAG, "compressed image into stream: " + estimatedTime);

                byte[] byteArray = stream.toByteArray();

                // instantiate itext image
                com.itextpdf.text.Image img = null;
                try {
                    img = com.itextpdf.text.Image.getInstance(byteArray);
                } catch (BadElementException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //img.scalePercent(40, 40);
                //img.setAlignment(Element.ALIGN_CENTER);

                //img.scaleAbsolute(PageSize.A4.getWidth(), PageSize.A4.getHeight());


                // scale the image and set it to center
                if (isPortrait) {
                    img.scaleToFit(PageSize.A4);
                    img.setAbsolutePosition(
                            (PageSize.A4.getWidth() - img.getScaledWidth()) / 2,
                            (PageSize.A4.getHeight() - img.getScaledHeight()) / 2
                    );
                }
                else
                {
                    img.scaleToFit(PageSize.A4.rotate());
                    img.setAbsolutePosition(
                            (PageSize.A4.rotate().getWidth() - img.getScaledWidth()) / 2,
                            (PageSize.A4.rotate().getHeight() - img.getScaledHeight()) / 2
                    );
                }
                try {
                    document.add(img);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

                // add a new page to the document to maintain 1 image per page
                document.newPage();

                float fractionalProgress = (i + 1) / image_uris.size() * 100;




            }

            //progress.cancel();
            image_uris = null;
            document.close();


             promptForNextAction();


            Log.e(TAG, "After alertdialogue.show, and before promptfornextaction");

            Log.e(TAG, "prompt for next action has completed");


            //progress.setVisibility(View.GONE);

        }


    }

    private void getImages(Config config) {


        ImagePickerActivity.setConfig(config);

        Intent intent = new Intent(this, ImagePickerActivity.class);

        if (image_uris != null) {
            intent.putParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, image_uris);
        }


        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);

    }

    public void promptForNextAction()
    {
        final String[] options = { "preview",
                "cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(WrittenExaminationActivity.this);
        builder.setTitle("PDF Saved, What Next?");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("preview")) {
                    viewPdf();
                } else if (options[which].equals("cancel")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();

    }

    private void viewPdf(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Log.w(TAG, "Opening:  " + myPDF.toString());
        intent.setDataAndType(Uri.fromFile(myPDF), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        startActivity(intent);

        Intent intent1 = Intent.createChooser(intent, "Open File");
        try {
            startActivity(intent1);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }


//    ProgressDialog progress;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {

                image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                if (image_uris != null) {
                    showMedia();
                }


            }

            else if (requestCode == 1 && resultCode == RESULT_OK) {
            filePath1 = intent.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
//                filePath = intent.getData();
            textViewFilePath.setText(filePath1);
            if (filePath1 != null) {
                Log.d("Path: ", filePath1);
                Toast.makeText(this, "Picked file: " + filePath1, Toast.LENGTH_LONG).show();
            }
            Log.d(TAG, "File: "+filePath);
        }

//            else if(requestCode == 10 && resultCode == RESULT_OK){
//
//                filePath1 = intent.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
//                textViewFilePath.setText(filePath1);
//                if (filePath1 != null) {
//                    Log.d("Path: ", filePath1);
//                    Toast.makeText(this, "Picked file: " + filePath1, Toast.LENGTH_LONG).show();
//                }
//                Log.d(TAG, "File: "+filePath1);
//
//                progress = new ProgressDialog(WrittenExaminationActivity.this);
//                progress.setTitle("Uploading");
//                progress.setMessage("Please wait...");
//                progress.show();
//
//                Thread t = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//                        File f  = new File(intent.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
//                        String content_type  = getMimeType(f.getPath());
//
//                        String file_path = f.getAbsolutePath();
//                        OkHttpClient client = new OkHttpClient();
//                        RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);
//
//                        RequestBody request_body = new MultipartBody.Builder()
//                                .setType(MultipartBody.FORM)
//                                .addFormDataPart("type",content_type)
//                                .addFormDataPart("uploaded_file",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
//                                .build();
//
//                        Request request = new Request.Builder()
//                                .url("https://www.tikabarta.com/examination/uploads.php")
//                                .post(request_body)
//                                .build();
//
//                        try {
//                            Response response = client.newCall(request).execute();
//
//                            if(!response.isSuccessful()){
//                                throw new IOException("Error : "+response);
//                            }
//
//                            progress.dismiss();
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                });
//
//                t.start();
//
//
//
//
//            }
        }

    }

    private void showMedia() {
        // Remove all views before
        // adding the new ones.
        mSelectedImagesContainer.removeAllViews();
        if (image_uris.size() >= 1) {
            mSelectedImagesContainer.setVisibility(View.VISIBLE);
        }

        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());


        for (Uri uri : image_uris) {

            View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item, null);
            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);

            Glide.with(this)
                    .load(uri.toString())
                    .fitCenter()
                    .into(thumbnail);

            mSelectedImagesContainer.addView(imageHolder);

            Log.d("VALUE", "" + uri.toString());

            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));

        }
    }
    @Override
    public void onClick(View v) {
        if (v == selectId) {
            checkPermissionsAndOpenFilePicker();
            showFileChooser();
        }
        if (v == loadId) {

            //start renaming
            LayoutInflater li = LayoutInflater.from(WrittenExaminationActivity.this);
            View promptsView = li.inflate(R.layout.layout, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    WrittenExaminationActivity.this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // get user input and set it to result
                                    // edit text

                                    fileName = userInput.getText().toString();

                                    uploadMultipart();
                                    filePath1="";
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                    //promptForNextAction();


                                }
                            });

            // create alert dialog
            //AlertDialog alertDialog = alertDialogBuilder.create();

            // show it

            Log.e(TAG, "Before alertdialogue.show");


            alertDialogBuilder.show();
        }
    }

    private void checkPermissionsAndOpenFilePicker() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            showFileChooser();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showFileChooser();
                } else {
                    showError();
                }
            }
        }
    }


    private void showError() {
        Toast.makeText(this, "Allow external storage reading", Toast.LENGTH_SHORT).show();
    }

//    public void uploadMultipart() {
//        //getting name for the image
//        String name = fileName;
//
//        //getting the actual path of the image
//        String path = FilePath.getPath(this, filePath);
//        //String path = FileUtils.getPath(this, filePath);
//        //String path = Commons.getPath(filePath, this);
//
//        if (path == null) {
//
//            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
//        } else {
//            //Uploading code
//            try {
//                String uploadId = UUID.randomUUID().toString();
//
//                //Creating a multi part request
//                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
//                        .addFileToUpload(path, "pdf") //Adding file
//                        .addParameter("name", name) //Adding text parameter to the request
//                        .setNotificationConfig(new UploadNotificationConfig())
//                        .setMaxRetries(2)
//                        .startUpload(); //Starting the upload
//
//            } catch (Exception exc) {
//                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    //method to show file chooser
//    private void showFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("application/pdf");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
//    }

    public void showFileChooser() {

        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(1)
                .withFilterDirectories(true) // Set directories filterable (false by default)
                .withHiddenFiles(true) // Show hidden files and folders
                .start();

    }

    public void uploadMultipart() {

        String name1 = fileName;
        if (name1.isEmpty()) {
            Toast.makeText(this, "File Name is Empty", Toast.LENGTH_SHORT).show();
        } else {
            path = filePath1;
            name = name1;

            if (path == null) {

                Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
            } else {



                loading = new ProgressDialog(WrittenExaminationActivity.this);
                loading.setIcon(R.drawable.load);
                loading.setTitle("Uploading");
                loading.setMessage("Please wait...");
                loading.show();

                HttpsTrustManager.allowAllSSL();

                StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, Key.DUPLICATE_ANSWER_SCRIPT, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //for track response in Logcat
                        Log.d("RESPONSE", "" + response);

                        //if we are getting success from server
                        if (response.equals("success")) {
                            //creating a shared preference
                            loading.dismiss();


                            //Uploading code
                            try {
                                String uploadId = UUID.randomUUID().toString();

                                //Creating a multi part request
                                new MultipartUploadRequest(WrittenExaminationActivity.this, uploadId, Key.UPLOAD_URL)
                                        .addFileToUpload(path, "pdf") //Adding file
                                        .addParameter("name", name) //Adding text parameter to the request
                                        .addParameter("Mid",mid)
                                        .addParameter(Key.KEY_ANSWER_ID,question_paper_id)
                                        .setMaxRetries(2)
                                        .startUpload(); //Starting the upload

                                //.setNotificationConfig(new UploadNotificationConfig())

                                Toast.makeText(WrittenExaminationActivity.this, "Pdf file is uploaded", Toast.LENGTH_SHORT).show();

                            } catch (Exception exc) {
                                Toast.makeText(WrittenExaminationActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        } else if (response.equals("exists")) {
                            Toast.makeText(WrittenExaminationActivity.this, "Answer Script already submitted", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }

                    }
                },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(WrittenExaminationActivity.this, "There is an error", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //return super.getParams();

                        Map<String,String> params = new HashMap<>();

                        //Ading parameters to request
                        params.put("name",name);
                        params.put(Key.KEY_MID,mid);
                        params.put(Key.KEY_ANSWER_ID,question_paper_id);

                        //returning parameter
                        return params;

                    }
                };

                //Adding the string request to the queue
                RequestQueue requestQueue = Volley.newRequestQueue(WrittenExaminationActivity.this);
                requestQueue.add(stringRequest);

            }
        }


    }

    @Override
    public void onBackPressed() {           //creating method of onBackPressed

        Intent intent =  new Intent(WrittenExaminationActivity.this,WrittenActivity.class);
        startActivity(intent);

    }
}
