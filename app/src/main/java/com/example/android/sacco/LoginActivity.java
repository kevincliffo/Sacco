package com.example.android.sacco;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_MEMBER_NO = "memberno";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_MOBILE_NO = "MobileNo";
    private static final String KEY_ID_NUMBER = "IDNumber";
    private static final String KEY_MEMBER_ID = "MemberId";
    private static final String KEY_BIRTH_DATE = "BirthDate";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_ADDRESS = "Address";
    private static final String KEY_CREATED_DATE = "CreatedDate";
    private static final String KEY_CONFIRMED = "Confirmed";

    private static final String KEY_EMPTY = "";
    private EditText etMemberNo;
    private EditText etPassword;
    private String memberno;
    private String password;
    private ProgressDialog pDialog;
    private String LOGIN_URL = "http://10.0.2.2/sacco/db/login.php";
    //private String LOGIN_URL = "http://www.heavenlydelights.co.ke/sacco/db/login.php";
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());
        Member m = new Member();
        if(session.isLoggedIn()){
            loadDashboard(m);
        }
        setContentView(R.layout.activity_login);

        etMemberNo = findViewById(R.id.etMemberNo);
        etPassword = findViewById(R.id.etLoginPassword);

        Button register = findViewById(R.id.btnLoginRegister);
        Button login = findViewById(R.id.btnLogin);

        //Launch Registration screen when Register Button is clicked
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                memberno = etMemberNo.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                if (validateInputs()) {
                    login();
                }
            }
        });
    }

    /**
     * Launch Dashboard Activity on Successful Login
     */
    private void loadDashboard(Member m) {
//        Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
//        startActivity(i);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("member", m);
        startActivity(i);
        finish();

    }

    /**
     * Display Progress bar while Logging in
     */

    private void displayLoader() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void login() {
        displayLoader();
        //JSONObject request = new JSONObject();

        //Populate the request parameters
        //request.put(KEY_MEMBER_NO, memberno);
        //request.put(KEY_PASSWORD, password);
        Map<String, String> params = new HashMap<String, String>();

        params.put(KEY_MEMBER_NO, memberno);
        params.put(KEY_PASSWORD, password);

        JSONObject request = new JSONObject(params);

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, LOGIN_URL, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got logged in successfully

                            if (response.getInt(KEY_STATUS) == 0) {
                                 Member m = new Member();
                                int nConfirmed = Integer.parseInt(response.getString(KEY_CONFIRMED));
                                m.setMemberNo(memberno);
                                m.setFullName(response.getString(KEY_FULL_NAME));
                                m.setMemberId(response.getString(KEY_MEMBER_ID));
                                m.setConfirmed(nConfirmed);
                                m.setBirthDate(response.getString(KEY_BIRTH_DATE));
                                m.setCreatedDate(response.getString(KEY_CREATED_DATE));
                                m.setEmail(response.getString(KEY_EMAIL));
                                m.setIDNumber(response.getString(KEY_ID_NUMBER));
                                m.setMobileNo(response.getString(KEY_MOBILE_NO));
                                m.setAddress(response.getString(KEY_ADDRESS));

                                session.loginMember(m);


                                loadDashboard(m);

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

    /**
     * Validates inputs and shows error if any
     * @return
     */
    private boolean validateInputs() {
        if(KEY_EMPTY.equals(memberno)){
            etMemberNo.setError("Member No cannot be empty");
            etMemberNo.requestFocus();
            return false;
        }
        if(KEY_EMPTY.equals(password)){
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }
}
