package com.example.android.sacco;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class FinancialsActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private static final String KEY_MEMBER_NO = "memberNo";
    private static final String KEY_EMPTY = "";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_PENDING_LOANS = "PendingLoans";
    private static final String KEY_SALARY = "Salary";
    private static final String KEY_MONTHLY_CONTRIBUTION = "MonthlyContribution";
    private static final String KEY_MONTHLY_DEDUCTION = "MonthlyDeduction";
    private static final String KEY_TOTAL_DEDUCTION = "TotalDeduction";
    private static final String KEY_TOTAL_CONTRIBUTION = "TotalContribution";

    private String GET_FINANCIALS_URL = "http://10.0.2.2/sacco/db/getfinancialdetails.php";
    private String MemberNo;
    private EditText etSalary;
    private EditText etMonthlyContribution;
    private EditText etMonthlyDeduction;
    private EditText etTotalDeduction;
    private EditText etTotalContribution;
    private EditText etPendingLoans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financials);


        etSalary = (EditText)findViewById(R.id.etSalary);
        etMonthlyContribution = (EditText)findViewById(R.id.etMonthlyContribution);
        etMonthlyDeduction = (EditText)findViewById(R.id.etMonthlyDeduction);
        etTotalDeduction = (EditText)findViewById(R.id.etTotalDeductions);
        etTotalContribution = (EditText)findViewById(R.id.etTotalContributions);
        etPendingLoans = (EditText)findViewById(R.id.etPendingLoans);

        MemberNo = getIntent().getStringExtra("MemberNo");

        getFinancialDetails();
    }

    private void displayLoader(String loaderMessage) {
        pDialog = new ProgressDialog(FinancialsActivity.this);
        pDialog.setMessage(loaderMessage + "... Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void getFinancialDetails() {
        displayLoader("Getting Next of Kin..");
        Map<String, String> params = new HashMap<String, String>();

        params.put(KEY_MEMBER_NO, MemberNo);

        JSONObject request = new JSONObject(params);

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, GET_FINANCIALS_URL, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                etSalary.setText(response.getString(KEY_SALARY));
                                etMonthlyContribution.setText(response.getString(KEY_MONTHLY_CONTRIBUTION));
                                etMonthlyDeduction.setText(response.getString(KEY_MONTHLY_DEDUCTION));
                                etPendingLoans.setText(response.getString(KEY_PENDING_LOANS));
                                etTotalContribution.setText(response.getString(KEY_TOTAL_CONTRIBUTION));
                                etTotalDeduction.setText(response.getString(KEY_TOTAL_DEDUCTION));

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
