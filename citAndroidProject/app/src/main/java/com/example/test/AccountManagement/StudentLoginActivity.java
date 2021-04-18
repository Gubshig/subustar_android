package com.example.test.AccountManagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import java.net.Authenticator;
import java.util.HashMap;
import java.util.Map;

public class StudentLoginActivity extends AppCompatActivity {

    private boolean ParentCodeVerification = false;

    private Button cParentCodeVerificationButton;
    private Button cStudentLoginButton;

    private EditText cParentCodeNum;
    private EditText cStudentNameText;
    private EditText cStudentCodeNum;

    private RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);



        cParentCodeVerificationButton = findViewById(R.id.parent_code_verification_button);
        cParentCodeVerificationButton.setOnClickListener(parentCodeVerificationButtonOnClickListener());
        cStudentLoginButton = findViewById(R.id.student_login_button2);
        cStudentLoginButton.setOnClickListener(studentLoginButtonOnclickListener());

        cParentCodeNum = findViewById(R.id.parent_code);
        cStudentNameText = findViewById(R.id.parent_code);
        cStudentCodeNum = findViewById(R.id.student_code);
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

    View.OnClickListener parentCodeVerificationButtonOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String apiURL = "http://54.180.152.145:3000/weatherCall/auth/register/verification/confirm";

                final Response.Listener<String> responseListener =  new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject verificationcode = new JSONObject(response);
                            if(verificationcode.getString("msg").equals("verification_confirmed")) {
                                showDialog("학부모코드 인증 확인", "확인되셨습니다");
                                ParentCodeVerification = true;
                            }
                            else {
                                showDialog("학부모코드 인증 확인", "올바른 학부모코드가 아닙니다. 다시 입력해주세요");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                final Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                };

                StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL, responseListener, errorListener) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        String verificationcode = cParentCodeNum.getText().toString();

                        HashMap<String, String> header = new HashMap<>();
                        header.put("authcode", verificationcode);

                        return header;
                    }
                };

                mQueue.add(stringRequest);
            }
        };
    }

    View.OnClickListener studentLoginButtonOnclickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String apiURL = "http://54.180.152.145:3000/weatherCall/auth/register/verification/confirm";

                final Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject studentloginresponse = new JSONObject(response);
                            if(studentloginresponse.getString("msg").equals("login_confirmed") && ParentCodeVerification == true) {
                                showDialog("로그인 확인", "로그인에 성공하셨습니다");

                                Intent intent = new Intent(getBaseContext(), StudentMainActivity.class);
                                startActivity(intent);

                            }
                            else if (studentloginresponse.getString("msg").equals("login_failed")){
                                showDialog("로그인 확인", "로그인에 실패하셨습니다. 자녀이름과 자녀코드를 다시 확인해주세");
                            }
                            else if(ParentCodeVerification = false) {
                                showDialog("로그인 확인", "학부모 코드를 입력해주세");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                final Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                };

                StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL, responseListener, errorListener) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        String studentname = cStudentNameText.getText().toString();
                        String studentcode = cStudentCodeNum.getText().toString();

                        HashMap<String, String> header = new HashMap<>();
                        header.put("studentname", studentname);
                        header.put("studentcode", studentcode);

                        return header;
                    }
                };

                mQueue.add(stringRequest);
            }
        };
    }
}