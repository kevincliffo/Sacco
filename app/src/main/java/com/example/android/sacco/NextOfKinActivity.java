package com.example.android.sacco;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NextOfKinActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAddUpdate;
    private EditText etBirthday;
    private EditText etFullName;
    private EditText etRelationship;
    private EditText etMobileNo;
    private EditText etIdNumber;
    private EditText etEmail;
    private EditText etAddress;

    private String Birthday;
    private String FullName;
    private String MemberNo;
    private String Relationship;
    private String MobileNo;
    private String IdNumber;
    private String Email;
    private String Address;

    private int nYear;
    private int nMonth;
    private int nDay;
    private static final String KEY_ADD_TASK_Value = "1";
    private static final String KEY_UPDATE_TASK_Value = "2";
    private static final String KEY_EMPTY = "";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_MEMBER_NO = "memberNo";
    private static final String KEY_RELATIONSHIP = "relationship";
    private static final String KEY_BIRTH_DATE = "birthDate";
    private static final String KEY_MOBILE_NO = "mobileNo";
    private static final String KEY_ID_NUMBER = "idNumber";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_TASK_VALUE = "taskValue";
    private String NEXT_OF_KIN_ADD_URL = "http://10.0.2.2/sacco/db/nextofkinadd.php";
    private String NEXT_OF_KIN_GET_URL = "http://10.0.2.2/sacco/db/nextofkinget.php";

    private String taskValue;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_of_kin);

        btnAddUpdate = findViewById(R.id.btnAddUpdate);
        etBirthday = (EditText)findViewById(R.id.etBirthday);
        etBirthday.setOnClickListener(this);
        etFullName = (EditText)findViewById(R.id.etFullName);
        //etMemberNo = (EditText)findViewById(R.id.etMemberNo);
        etRelationship = (EditText)findViewById(R.id.etRelationship);
        etMobileNo = (EditText)findViewById(R.id.etMobileNo);
        etIdNumber = (EditText)findViewById(R.id.etIDNumber);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etAddress = (EditText)findViewById(R.id.etAddress);

        btnAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Birthday = etBirthday.getText().toString().trim();
                FullName = etFullName.getText().toString().trim();
                //MemberNo = etMemberNo.getText().toString().trim();
                Relationship = etRelationship.getText().toString().trim();
                MobileNo = etMobileNo.getText().toString().trim();
                IdNumber = etIdNumber.getText().toString().trim();
                Email = etEmail.getText().toString().trim();
                Address = etAddress.getText().toString().trim();

                addNextOfKin();
            }
        });

        taskValue = KEY_ADD_TASK_Value;
        MemberNo = getIntent().getStringExtra("MemberNo");
        getNextOfKin();
    }

    private void displayLoader(String loaderMessage) {
        pDialog = new ProgressDialog(NextOfKinActivity.this);
        pDialog.setMessage(loaderMessage + "... Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    public void onClick(View v)
    {
        final Calendar c = Calendar.getInstance();

        nYear = c.get(Calendar.YEAR);
        nMonth = c.get(Calendar.MONTH);
        nDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String szDay = padWithZero(dayOfMonth);
                String szMonth = padWithZero(month + 1);
                String szYear = Integer.toString(year);

                etBirthday.setText(szDay + "-" + szMonth + "-" + szYear);
            }
        }, nYear, nMonth, nDay);
        dpd.getDatePicker().setCalendarViewShown(false);
        dpd.show();
    }

    private String padWithZero(int nvValue)
    {
        String szValue = Integer.toString(nvValue);
        String szPaddedValue = szValue;

        if(szValue.length() == 1)
        {
            szPaddedValue = "0" + szValue;
        }

        return szPaddedValue;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbAdd:
                if (checked)
                {
                    taskValue = KEY_ADD_TASK_Value;
                }
                break;
            case R.id.rbUpdate:
                if (checked)
                {
                    taskValue = KEY_UPDATE_TASK_Value;
                }
                break;
        }
    }

    private void getNextOfKin() {
        displayLoader("Getting Next of Kin..");
        Map<String, String> params = new HashMap<String, String>();

        params.put(KEY_MEMBER_NO, MemberNo);

        JSONObject request = new JSONObject(params);

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, NEXT_OF_KIN_GET_URL, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
                                //session.loginUser(username,fullName);
                                //loadDashboard();
                                etFullName.setText(response.getString(KEY_FULL_NAME));
                                //etMemberNo.setText(response.getString(KEY_MEMBER_NO));
                                etBirthday.setText(response.getString(KEY_BIRTH_DATE));
                                etMobileNo.setText(response.getString(KEY_MOBILE_NO));
                                etIdNumber.setText(response.getString(KEY_ID_NUMBER));
                                etEmail.setText(response.getString(KEY_EMAIL));
                                etAddress.setText(response.getString(KEY_ADDRESS));
                                etRelationship.setText(response.getString(KEY_RELATIONSHIP));

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

    private void addNextOfKin() {
        displayLoader("Adding Next of Kin..");
        Map<String, String> params = new HashMap<String, String>();

        params.put(KEY_FULL_NAME, FullName );
        params.put(KEY_MEMBER_NO, MemberNo);
        params.put(KEY_RELATIONSHIP, Relationship);
        params.put(KEY_BIRTH_DATE, Birthday);
        params.put(KEY_MOBILE_NO, MobileNo);
        params.put(KEY_ID_NUMBER, IdNumber);
        params.put(KEY_EMAIL, Email);
        params.put(KEY_ADDRESS, Address);
        params.put(KEY_TASK_VALUE, taskValue.trim());

        JSONObject request = new JSONObject(params);

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, NEXT_OF_KIN_ADD_URL, request, new Response.Listener<JSONObject>() {
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
