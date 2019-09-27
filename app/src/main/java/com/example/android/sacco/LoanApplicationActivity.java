package com.example.android.sacco;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoanApplicationActivity extends AppCompatActivity {

    private SessionHandler session;
    private EditText etMemberNo;
    private EditText etIDNumber;
    private EditText etLoanAmount;
    private EditText etPaymentPeriod;
    private EditText etBasicSalary;
    private EditText etGuarantorMemberNo;
    private Button btnApply;
    private String loanType;
    private String county;
    private static final String KEY_EMPTY = "";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_MEMBER_NO = "MemberNo";
    private static final String KEY_LOAN_AMOUNT = "LoanAmount";
    private static final String KEY_MONTHS_PAYMENT_PERIOD ="MonthsPaymentPeriod";
    private static final String KEY_ID_NUMBER ="IDNumber";
    private static final String KEY_BASIC_SALARY = "BasicSalary";
    private static final String KEY_GUARANTOR_MEMBER_NO = "GuarantorMemberNo";

    private String LOAN_APPLICATION_URL = "http://10.0.2.2/sacco/db/loanapplication.php";

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);
        session = new SessionHandler(getApplicationContext());
        Member m = session.getMemberDetails();

        etMemberNo = findViewById(R.id.etmemberno);
        etMemberNo.setText(m.getMemberNo());
        etMemberNo.setEnabled(false);

        etIDNumber = findViewById(R.id.etIDNumber);
        etIDNumber.setText(m.getIDNumber());
        etIDNumber.setEnabled(false);
        etLoanAmount = findViewById(R.id.etLoanAmount);
        etPaymentPeriod = findViewById(R.id.etPaymentPeriod);
        etBasicSalary = findViewById(R.id.etBasicSalary);
        etGuarantorMemberNo = findViewById(R.id.etGuarantorNo);
        btnApply = findViewById(R.id.btnApply);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (validateInputs()) {
                    makeLoanApplication();
                }
            }
        });

    }

    private void resetValues()
    {
        etMemberNo.setText("");
        etIDNumber.setText("");
        etLoanAmount.setText("");
        etPaymentPeriod.setText("");
        etBasicSalary.setText("");
        etGuarantorMemberNo.setText("");

    }

    private void displayLoader() {
        pDialog = new ProgressDialog(LoanApplicationActivity.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void makeLoanApplication()
    {
        displayLoader();
        Map<String, String> params = new HashMap<String, String>();

        params.put(KEY_MEMBER_NO, etMemberNo.getText().toString().trim());
        params.put(KEY_ID_NUMBER, etIDNumber.getText().toString().trim());
        params.put(KEY_LOAN_AMOUNT, etLoanAmount.getText().toString().trim());
        params.put(KEY_MONTHS_PAYMENT_PERIOD, etPaymentPeriod.getText().toString().trim());
        params.put(KEY_BASIC_SALARY, etBasicSalary.getText().toString().trim());
        params.put(KEY_GUARANTOR_MEMBER_NO, etGuarantorMemberNo.getText().toString().trim());

        JSONObject request = new JSONObject(params);

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, LOAN_APPLICATION_URL, request, new Response.Listener<JSONObject>() {
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

                                resetValues();
                                Intent i = new Intent(LoanApplicationActivity.this, MainActivity.class);
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
        if (KEY_EMPTY.equals(etMemberNo.getText().toString().trim())) {
            etMemberNo.setError("Member No. cannot be empty");
            etMemberNo.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(etIDNumber.getText().toString().trim())) {
            etIDNumber.setError("Mobile Number cannot be empty");
            etIDNumber.requestFocus();
            return false;
        }

        if(StringHasAplhabet(etIDNumber.getText().toString().trim()))
        {
            etIDNumber.setError("Should contain Digits only");
            etIDNumber.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(etLoanAmount.getText().toString().trim())) {
            etLoanAmount.setError("Loan Amount cannot be empty");
            etLoanAmount.requestFocus();
            return false;
        }

        if(StringHasAplhabet(etLoanAmount.getText().toString().trim()))
        {
            etLoanAmount.setError("Should contain Digits only");
            etLoanAmount.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(etPaymentPeriod.getText().toString().trim())) {
            etPaymentPeriod.setError("Payment Period cannot be empty");
            etPaymentPeriod.requestFocus();
            return false;
        }

        if(StringHasAplhabet(etPaymentPeriod.getText().toString().trim()))
        {
            etPaymentPeriod.setError("Should contain Digits only");
            etPaymentPeriod.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(etBasicSalary.getText().toString().trim())) {
            etBasicSalary.setError("Basic Salary cannot be empty");
            etBasicSalary.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(etGuarantorMemberNo.getText().toString().trim())) {
            etGuarantorMemberNo.setError("Guarantor Member No cannot be empty");
            etGuarantorMemberNo.requestFocus();
            return false;
        }

        return true;
    }
}
