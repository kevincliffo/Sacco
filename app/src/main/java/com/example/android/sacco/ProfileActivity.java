package com.example.android.sacco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private ImageView ivx;
    private ImageView ivxHeadCover;
    private CircleImageView civx;
    private TextView tvMemberNo;
    private TextView tvFullName;
    private TextView tvIdNo;

    private EditText etEmail;
    private EditText etBirthday;
    private EditText etMobile;
    private EditText etAddress;
    private ProgressDialog pDialog;
    private Button btnUpdate;

    private static final String KEY_EMPTY = "";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MEMBER_NO = "memberNo";
    private static final String KEY_BIRTH_DATE = "birthDate";
    private static final String KEY_MOBILE_NO = "mobileNo";
    private static final String KEY_ADDRESS = "address";
    private String UPDATE_PROFILE_URL = "http://10.0.2.2/sacco/db/updateprofile.php";
    private String Birthday;
    private String MemberNo;
    private String MobileNo;
    private String Email;
    private String Address;

    private  String szxUserName;
    private  String szxFullName;
    SessionHandler session;

    private static final String IMAGE_DIRECTORY = "/sacco";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        session = new SessionHandler(getApplicationContext());
        Member m = session.getMemberDetails();

        ivx = (ImageView)findViewById(R.id.edit);
        ivxHeadCover = (ImageView)findViewById(R.id.header_cover_image);
        civx = (CircleImageView)findViewById(R.id.profile);

        szxUserName = getIntent().getStringExtra("UserName");
        szxFullName = getIntent().getStringExtra("FullName");

        tvMemberNo = (TextView)findViewById(R.id.memberNo);
        tvMemberNo.setText(szxUserName);
        tvFullName = (TextView)findViewById(R.id.fullName);
        tvFullName.setText(szxFullName);
        etEmail = findViewById(R.id.email);
        etEmail.setText(m.getEmail());
        etBirthday = findViewById(R.id.birthDay);
        etBirthday.setText(m.getBirthDate());
        tvIdNo = findViewById(R.id.idNo);
        tvIdNo.setText(m.getIDNumber());
        etMobile = findViewById(R.id.mobileNo);
        etMobile.setText(m.getMobileNo());
        etAddress = findViewById(R.id.address);
        etAddress.setText(m.getAddress());
        btnUpdate = findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Birthday = etBirthday.getText().toString().trim();
                MemberNo = tvMemberNo.getText().toString().trim();
                MobileNo = etMobile.getText().toString().trim();
                Email = etEmail.getText().toString().trim();
                Address = etAddress.getText().toString().trim();

                updateProfile();
            }
        });
        if(session.UserBitmap() != null)
        {
            civx.setImageBitmap(session.UserBitmap());
        }
        ivx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
    }

    private void displayLoader(String loaderMessage) {
        pDialog = new ProgressDialog(ProfileActivity.this);
        pDialog.setMessage(loaderMessage + "... Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                //Get image
                Bitmap newProfilePic = extras.getParcelable("data");

                ivxHeadCover.setImageBitmap((newProfilePic));
                civx.setImageBitmap(newProfilePic);
                session.setBitmap(newProfilePic);
            }
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void updateProfile() {
        displayLoader("Adding Next of Kin..");
        Map<String, String> params = new HashMap<String, String>();

        params.put(KEY_MEMBER_NO, MemberNo.toString().trim());
        params.put(KEY_BIRTH_DATE, Birthday.toString().trim());
        params.put(KEY_MOBILE_NO, MobileNo.toString().trim());
        params.put(KEY_EMAIL, Email.toString().trim());
        params.put(KEY_ADDRESS, Address.toString().trim());

        JSONObject request = new JSONObject(params);

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, UPDATE_PROFILE_URL, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
                                //session.loginUser(username,fullName);
                                //loadDashboard();
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();


                            }else if(response.getInt(KEY_STATUS) == 1){
                                //Display error message if username is already existsing
                                //etIDNumber.setError("Username already taken!");
                                //etIDNumber.requestFocus();

                            }else{
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

}
