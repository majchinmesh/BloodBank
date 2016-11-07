package com.lol.majchin.bloodbank;

/**
 * Created by majch on 08-10-2016.
 */

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    private Button buttonChoose;
    private Button buttonSubmit;
    private ImageView imageView;
    private EditText editText;


    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    EditText description ,contactno, email2 , bloodtype   ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //Requesting storage permission
        //requestStoragePermission();

        //Initializing views
        //buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonSubmit = (Button) findViewById(R.id.btn_p_submit);



        description = (EditText) findViewById(R.id.et_p_description);
        contactno = (EditText) findViewById(R.id.et_p_contactno);
        email2 = (EditText) findViewById(R.id.et_p_email);
        bloodtype = (EditText) findViewById(R.id.et_p_bloodtype);

        buttonSubmit.setOnClickListener(this);
    }





    @Override
    public void onClick(View v) {

        String email = email2.getText().toString() ;
        String contact = contactno.getText().toString()  ;
        String bloodt = bloodtype.getText().toString()  ;
        String desc = description.getText().toString() ;

        if(email.length() != 0 && contact.length() != 0 && bloodt.length() != 0) {

            new PostAsync().execute(bloodt,contact , email, desc);
        }else{
            Toast.makeText(this, "Some of the field's are missing.", Toast.LENGTH_LONG).show();
        }


    }

    // this class is not used soo far
    class PostAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private ProgressDialog pDialog;
        //private static final String UPLOAD_URL = Constants.UPLOAD_URL ;
        private static final String UPLOAD_URL = Constants.POST_URL ;
        //private static final String UPLOAD_URL = "http://findphones.hostfree.pw/mail2/send.php";
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(PostActivity.this);
            pDialog.setMessage("Attempting to Post...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("bloodtype", args[0]);
                params.put("contactno", args[1]);
                params.put("email", args[2]);
                params.put("description", args[3]);
                params.put("email2", SaveSharedPreference.getUser(getApplicationContext()).toString() );


                Log.d("request", "starting");
                JSONObject json = jsonParser.makeHttpRequest(
                        UPLOAD_URL, "POST", params);
                if (json != null) {
                    Log.d("JSON result", json.toString());
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            int success = 0;
            String message = "";
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (json != null) {
                Toast.makeText(PostActivity.this, json.toString(), Toast.LENGTH_LONG ).show(); // must be commented finally
                try {
                    success = json.getInt(TAG_SUCCESS);
                    message = json.getString(TAG_MESSAGE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{

                Toast.makeText(PostActivity.this, "Network Error", Toast.LENGTH_LONG ).show();

            }


            if (success == 1) {

                Log.d("Success!", message);

                Intent gotoHome = new Intent(PostActivity.this, HomeActivity.class);

                PostActivity.this.startActivity(gotoHome);

                PostActivity.this.finish();


            }else{
                Toast.makeText(PostActivity.this, message , Toast.LENGTH_LONG ).show();
                Log.d("Failure", message);
            }
        }
    }

}
