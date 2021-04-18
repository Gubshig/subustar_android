package com.example.test.AccountManagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import java.util.Set;

public class VerificationActivity extends AppCompatActivity {


    private TextView cVerificationCodeText;
    private Button cVerificationConfirmButton;

    private String mEmail;

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfication);
        mQueue = Volley.newRequestQueue(getApplicationContext());

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
                                SharedPreferences sharedPreferences = getSharedPreferences("user_code",0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString("parent_code",verificationcode.getString("parent_code"));

                                editor.commit();


                            }
                            else {
                                showDialog("이메일 인증 확인", "올바른 인증번호가 아닙니다.");

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