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

public class ErrenousDeductionActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etMemberNo;
    private EditText etFullName;
    private EditText etAmount;
    private EditText etDeductionDate;
    private Button btnRequestChange;
    private ProgressDialog pDialog;

    private int nYear;
    private int nMonth;
    private int nDay;

    private String fullName;
    private String memberNo;
    private String amount;
    private String deductionDate;
    private static final String KEY_EMPTY = "";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_MEMBER_NO = "memberNo";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_DEDUCTION_DATE = "deductionDate";
    private static final String KEY_AMOUNT = "amount";

    private String ERRORNEOUS_DEDUCTIONS_URL = "http://10.0.2.2/sacco/db/errorneousdeduction.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errenous_deduction);

        etMemberNo = findViewById(R.id.etMemberNo);
        etFullName = findViewById(R.id.etFullName);
        etAmount = findViewById(R.id.etAmount);
        etDeductionDate = findViewById(R.id.etDeductionDate);
        etDeductionDate.setOnClickListener(this);
        btnRequestChange = findViewById(R.id.btnRequestChange);
        btnRequestChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fullName = etFullName.getText().toString().trim();
                memberNo = etMemberNo.getText().toString();
                amount = etAmount.getText().toString().trim();
                deductionDate = etDeductionDate.getText().toString().trim();

                if (validateInputs()) {
                    makeErrorDeductionRequest();
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

                etDeductionDate.setText(szDay + "-" + szMonth + "-" + szYear);
            }
        }, nYear, nMonth, nDay);

//        dpd.getDatePicker().setMinDate(1900);
//        dpd.getDatePicker().setMaxDate(2001);
        dpd.show();
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

    private boolean validateInputs() {
        if (KEY_EMPTY.equals(fullName)) {
            etFullName.setError("Full Name cannot be empty");
            etFullName.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(memberNo)) {
            etMemberNo.setError("Member No cannot be empty");
            etMemberNo.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(amount)) {
            etAmount.setError("Amount cannot be empty");
            etAmount.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(deductionDate)) {
            etDeductionDate.setError("Deduction Date cannot be empty");
            etDeductionDate.requestFocus();
            return false;
        }

        return true;
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(ErrenousDeductionActivity.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    private void makeErrorDeductionRequest() {
        displayLoader();
        Map<String, String> params = new HashMap<String, String>();

        params.put(KEY_FULL_NAME, fullName);
        params.put(KEY_MEMBER_NO, memberNo);
        params.put(KEY_DEDUCTION_DATE, deductionDate);
        params.put(KEY_AMOUNT, amount);

        JSONObject request = new JSONObject(params);

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, ERRORNEOUS_DEDUCTIONS_URL, request, new Response.Listener<JSONObject>() {
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

                                Intent i = new Intent(ErrenousDeductionActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();

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
