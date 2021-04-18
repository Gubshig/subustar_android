package com.example.test.AccountManagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.test.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerificationActivity extends AppCompatActivity {


    private TextView cVerificationCodeText;
    private Button cVerificationConfirmButton;

    private String mEmail;

    private RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfication);

        mEmail = getIntent().getStringExtra("email");

        cVerificationCodeText = findViewById(R.id.verification_code_text);
        cVerificationConfirmButton = findViewById(R.id.verification_confitm_button);
        cVerificationConfirmButton.setOnClickListener(verificationConfirmButtonOnClickListener());

    }


    private View.OnClickListener verificationConfirmButtonOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String apiURL = "http://54.180.152.145:3000/weatherCall/auth/register/verification/confirm";

                final Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject verificationcode = new JSONObject(response);
                            if (verificationcode.getString("msg").equals("verification_confirmed")) {

                                showDialog("이메일 인증 확인", "확인되셨습니다.");
                            }
                            else {
                                showDialog("이메일 인증 확인", "올바른 인증번호가 아닙니다.");

                                final String apiURL1 = "http://54.180.152.145:3000/weatherCall/auth/register/verification";

                                final Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                    }
                                };
                                Response.ErrorListener errorListener1 = new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                };

                                StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL1, responseListener1, errorListener1){
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        HashMap<String, String> header = new HashMap<>();
                                        header.put("email", mEmail);

                                        return header;
                                    }
                                };
                                mQueue.add(stringRequest);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                };

                StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL,responseListener, errorListener){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        String verificationcode = cVerificationCodeText.getText().toString();

                        HashMap<String, String> header = new HashMap<>();
                        header.put("authcode", verificationcode);

                        return header;
                    }
                };

                mQueue.add(stringRequest);

            }
        };
    }


    void showDialog(String title, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}