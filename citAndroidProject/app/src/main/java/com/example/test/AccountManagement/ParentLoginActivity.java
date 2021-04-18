package com.example.test.AccountManagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.test.MainPage.ParentMainActivity;
import com.example.test.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class ParentLoginActivity extends AppCompatActivity {

    TextView cEmailTextView;
    TextView cPasswordTextView;
    Button cParentRegisterButton;
    Button cParentLoginButton;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);

        cEmailTextView = findViewById(R.id.parent_login_email_address);
        cPasswordTextView = findViewById(R.id.parent_login_password);
        cParentRegisterButton = findViewById(R.id.parent_register);
        cParentLoginButton = findViewById(R.id.parent_login_button);

        cParentRegisterButton.setOnClickListener(parentRegisterButtonOnClickListener());

    }

    View.OnClickListener parentRegisterButtonOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),ParentRegisterActivity.class);
                startActivity(intent);
            }
        };
    }

    View.OnClickListener parentLoginButtonOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String apiURL = "http://54.180.152.145:3000/weatherCall/login/parent";

                Response.Listener<String> loginListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println(response);
                        //{"msg":"password_err" or "email_err" or "login_confirmed", "parent_code":"xcdscscvv"}
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String msg = jsonResponse.getString("msg");
                            if (msg.equals("password_err")){
                                cPasswordTextView.setText("");
                                showDialog("비밀번호 오류","비밀번호가 올바르지 않습니다.");
                            }
                            else if (msg.equals("email_err")){
                                cEmailTextView.setText("");
                                showDialog("이메일 오류","없는 이메일입니다.");
                            }
                            else if (msg.equals("login_confirmed")){
                                String parentCode = jsonResponse.getString("parent_code");
                                Intent intent = new Intent(getBaseContext(), ParentMainActivity.class);
                                intent.putExtra("parent_code",parentCode);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                Response.ErrorListener loginErrorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                };

                final StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL,
                        loginListener, loginErrorListener)
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        HashMap<String, String> body = new HashMap<String,String>();

                        body.put("email",cEmailTextView.getText().toString());
                        body.put("password",cPasswordTextView.getText().toString());
                        return body;
                    }
                };

                queue.add(stringRequest);



            }
        };
    }

    // 사용자에게 확인이 필요한 사항을 전달하는 다이얼로그 생성기.
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