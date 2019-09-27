package com.example.android.sacco;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_MOBILE_NO = "mobileNo";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_SALARY = "salary";
    private static final String KEY_ID_NUMBER = "idNumber";
    private static final String KEY_BIRTH_DATE = "birthDate";
    private static final String KEY_NEXT_OF_KIN_FULL_NAME = "nextOfKinName";
    private static final String KEY_NEXT_OF_KIN_MOBILE_NO = "nextOfKinMobile";
    private static final String KEY_EMPTY = "";
    private EditText etFullName;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etMobileNo;
    private EditText etEmail;
    private EditText etAddress;
    private EditText etSalary;
    private EditText etIDNumber;
    private EditText etBirthday;
    private EditText etNextOfKinName;
    private EditText etNextOfKinMobile;
    private Button btnRegister;
    private String fullName;
    private String password;
    private String confirmPassword;
    private String mobileNo;
    private String email;
    private String address;
    private String salary;
    private String idNumber;
    private String birthDate;
    private String nextOfKinName;
    private String nextOfKinMobile;
    private ProgressDialog pDialog;
    private String REGISTER_URL = "http://10.0.2.2/sacco/db/register.php";
    private String register_url = "http://www.heavenlydelights.co.ke/sacco/db/register.php";
    private SessionHandler session;

    private int nYear;
    private int nMonth;
    private int nDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etBirthday = (EditText)findViewById(R.id.etBirthday);
        etBirthday.setOnClickListener(this);
        etFullName = (EditText)findViewById(R.id.etFullName);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etConfirmPassword = (EditText)findViewById(R.id.etConfirmPassword);
        etMobileNo = (EditText)findViewById(R.id.etMobileNo);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etAddress = (EditText)findViewById(R.id.etAddress);
        etSalary = (EditText)findViewById(R.id.etsalary);
        etIDNumber = (EditText)findViewById(R.id.etIDNumber);
        etNextOfKinName = (EditText)findViewById(R.id.etNextOfKinFullName);
        etNextOfKinMobile = (EditText)findViewById(R.id.etNextOfKinMobileNo);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                fullName = etFullName.getText().toString().trim();
                password = etPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString().trim();
                mobileNo = etMobileNo.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                address = etAddress.getText().toString().trim();
                salary = etSalary.getText().toString().trim();
                idNumber = etIDNumber.getText().toString().trim();
                birthDate = etBirthday.getText().toString().trim();
                idNumber = etIDNumber.getText().toString().trim();
                birthDate = etBirthday.getText().toString().trim();
                nextOfKinName = etNextOfKinName.getText().toString().trim();
                nextOfKinMobile = etNextOfKinMobile.getText().toString().trim();

                if (validateInputs()) {
                    registerUser();
                }

            }
        });
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
                String szDay = PadWithZero(dayOfMonth);
                String szMonth = PadWithZero(month + 1);
                String szYear = Integer.toString(year);

                etBirthday.setText(szDay + "-" + szMonth + "-" + szYear);
            }
        }, nYear, nMonth, nDay);

//        dpd.getDatePicker().setMinDate(1900);
//        dpd.getDatePicker().setMaxDate(2001);
        dpd.show();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(RegistrationActivity.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void registerUser() {
        displayLoader();
        /*JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USERNAME, username);
            request.put(KEY_PASSWORD, password);
            request.put(KEY_FULL_NAME, fullName);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        Map<String, String> params = new HashMap<String, String>();

        params.put(KEY_FULL_NAME, fullName);
        params.put(KEY_PASSWORD, password);
        params.put(KEY_MOBILE_NO, mobileNo);
        params.put(KEY_EMAIL, email);
        params.put(KEY_ADDRESS, address);
        params.put(KEY_SALARY, salary);
        params.put(KEY_ID_NUMBER, idNumber);
        params.put(KEY_BIRTH_DATE, birthDate);
        params.put(KEY_NEXT_OF_KIN_FULL_NAME, nextOfKinName);
        params.put(KEY_NEXT_OF_KIN_MOBILE_NO, nextOfKinMobile);

        JSONObject request = new JSONObject(params);

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, REGISTER_URL, request, new Response.Listener<JSONObject>() {
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
                                        "Thank you for registering with us, Admin will send you login details shortly via mail", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();

                            }else if(response.getInt(KEY_STATUS) == 1){
                                //Display error message if username is already existsing
                                etIDNumber.setError("Username already taken!");
                                etIDNumber.requestFocus();

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

    private String PadWithZero(int nvValue)
    {
        String szValue = Integer.toString(nvValue);
        String szPaddedValue = szValue;

        if(szValue.length() == 1)
        {
            szPaddedValue = "0" + szValue;
        }

        return szPaddedValue;
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean StringHasNumber(String szvString) {

        boolean bNumberFound = false;
        for(char chr : szvString.toCharArray()){
            if(Character.isDigit(chr)){
                bNumberFound = true;
                break;
            }
        }

        return bNumberFound;
    }

    public boolean StringHasAplhabet(String szvString) {

        boolean bAlphabetFound = false;
        for(char chr : szvString.toCharArray()){
            if(Character.isDigit(chr)){
                bAlphabetFound = false;
            }
            else
            {
                bAlphabetFound = true;
            }
        }

        return bAlphabetFound;
    }

    private boolean validateInputs() {
        if (KEY_EMPTY.equals(fullName)) {
            etFullName.setError("Full Name cannot be empty");
            etFullName.requestFocus();
            return false;
        }

        if(StringHasNumber(fullName))
        {
            etFullName.setError("Name cannot contain a Number");
            etFullName.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(password)) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(confirmPassword)) {
            etConfirmPassword.setError("Confirm Password cannot be empty");
            etConfirmPassword.requestFocus();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Password and Confirm Password do not match");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(mobileNo)) {
            etMobileNo.setError("Password cannot be empty");
            etMobileNo.requestFocus();
            return false;
        }

        if(StringHasAplhabet(mobileNo))
        {
            etMobileNo.setError("Should contain Digits only");
            etMobileNo.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(email)) {
            etEmail.setError("Email cannot be empty");
            etEmail.requestFocus();
            return false;
        }

        boolean bValidEmail = isValidEmail(email);
        if(bValidEmail == false)
        {
            etEmail.setError("Email format not valid!");
            etEmail.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(address)) {
            etAddress.setError("Email cannot be empty");
            etAddress.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(salary)) {
            etSalary.setError("Salary cannot be empty");
            etSalary.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(idNumber)) {
            etIDNumber.setError("ID Number cannot be empty");
            etIDNumber.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(nextOfKinName)) {
            etNextOfKinName.setError("Next of Kin name cannot be empty");
            etNextOfKinName.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(nextOfKinMobile)) {
            etNextOfKinMobile.setError("Next of Kin Mobile cannot be empty");
            etNextOfKinMobile.requestFocus();
            return false;
        }

        if(StringHasAplhabet(idNumber))
        {
            etIDNumber.setError("Should contain Digits only");
            etIDNumber.requestFocus();
            return false;
        }
        return true;
    }
}
