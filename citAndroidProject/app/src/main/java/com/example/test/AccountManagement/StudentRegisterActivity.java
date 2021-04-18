package com.example.test.AccountManagement;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class StudentRegisterActivity extends AppCompatActivity {

    private TextView cStudentName;
    private TextView cStudentAge;
    private TextView cStudentGender;
    private TextView cStudentGroup;

    private Button cStudentRegisterButton;

    private RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        cStudentName = findViewById(R.id.student_name_textview);
        cStudentAge = findViewById(R.id.student_age_textview);
        cStudentGender = findViewById(R.id.student_gender_textview);
        cStudentGroup = findViewById(R.id.student_group_textview);

        cStudentRegisterButton = findViewById(R.id.student_register_button2);
        cStudentRegisterButton.setOnClickListener(studentregisterbuttononclicklistener());
    }

    View.OnClickListener studentregisterbuttononclicklistener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String apiURL = "http://54.180.152.145:3000/weatherCall/auth/register/verification/confirm";

                final Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getString("msg").equals("register_confirmed")) {

                                showDialog("등록 확인", "확인되셨습니다.");
                            }
                            else {
                                showDialog("등록 확인", "실패하셨습니다.");
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
                        String studentname = cStudentName.getText().toString();
                        String studentage = cStudentAge.getText().toString();
                        String studentgender = cStudentGender.getText().toString();
                        String studentgroup = cStudentGroup.getText().toString();


                        HashMap<String, String> header = new HashMap<>();
                        header.put("name", studentname);
                        header.put("age", studentage);
                        header.put("gender", studentgender);
                        header.put("group", studentgroup);

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