package com.example.test.AccountManagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.test.R;

public class ParentRegisterActivity extends AppCompatActivity {

    private Context mContext;
    private boolean mEmailConfirmed;
    private boolean mNameConfirmed;
    private String mSelectedOrganization;

    // 서버에 요청보내는 queue 자료구조
    private static RequestQueue queue;

    // component구성하는 멤버변수 선언
    private Button cVerificationButton;
    private Button cEmailConfirmButton;
    private Button cNameConfirmButton;

    private TextView cPasswordText;
    private TextView cEmailText;
    private TextView cNameText;
    private TextView cName2Text;

    private CheckBox cCheckBoxMale;
    private CheckBox cCheckBoxFemale;

    private Spinner cSpinnerOrganization;

    // Spinner에 담을 내용들을 구성하는 Arraylist 멤버변수
    private ArrayList<String> organizationList = new ArrayList<String>();
    private ArrayAdapter organizationListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_register);

        mContext = getApplicationContext();
        mEmailConfirmed = false;
        mNameConfirmed = false;
        queue = Volley.newRequestQueue(mContext);
        initComponents();

    }


    void initComponents(){
        cVerificationButton = findViewById(R.id.button);
        cVerificationButton.setOnClickListener(verificationOnClickListener());
        cEmailConfirmButton = findViewById(R.id.email_confirm_button);
        cEmailConfirmButton.setOnClickListener(emailConfirmButtonOnClickListener());
        cNameConfirmButton = findViewById(R.id.name_confirm);
        cNameConfirmButton.setOnClickListener(nameConfirmButtonOnClickListener());

        cPasswordText = findViewById(R.id.passwordText);
        cEmailText = findViewById(R.id.emailText);
        cNameText = findViewById(R.id.nameText);
        cName2Text = findViewById(R.id.nameText2);

        cCheckBoxMale = findViewById(R.id.checkBoxMale);
        cCheckBoxFemale = findViewById(R.id.checkBoxFemale);

        organizationList.add("CIT CODING ACADEMY");
        organizationList.add("대성학원");

        cSpinnerOrganization = findViewById(R.id.spinner_organization);
        organizationListAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,organizationList);
        cSpinnerOrganization.setAdapter(organizationListAdapter);
        cSpinnerOrganization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               mSelectedOrganization =  organizationList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    // eof onCreate

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

    // 이메일 형식이 올바른지 이메일을 서버로 전송하여 서버로부터 판단하는 코드

    private View.OnClickListener emailConfirmButtonOnClickListener(){

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String apiURL = "http://54.180.152.145:3000/weatherCall/auth/register/email-confirm";

                final StringRequest getEmailConfirmRequest = new StringRequest(Request.Method.GET, apiURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("response:",response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("이메일 컨펌 결과").equals("Denied")){
                                        showDialog("이메일 형식 오류", "이메일 형식이 올바르지 않습니다.\n 다시 입력해주세요.");
                                    }
                                    else{
                                        showDialog("사용 가능한 이메일", "사용 가능한 이메일 입니다.");
                                        mEmailConfirmed = true;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("response_err:",error.getMessage());
                            }
                        }
                ){

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> headers = new HashMap<String, String> ();
                        String email = cEmailText.getText().toString();
                        headers.put("email",email);
                        return headers;
                    }
                };
                queue.add(getEmailConfirmRequest);


            }
        };

        return onClickListener;

    }

    /**********************************************
     ** methods for OnClickListener
     ****/
    private View.OnClickListener nameConfirmButtonOnClickListener(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = cNameText.getText().toString();
                String name2 = cName2Text.getText().toString();
                String[] specialCase = {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", ".", ",", "?", ";", ":",
                        "'", "\"", "{", "[", "}", "]", "-", "_", "+", "="};
                boolean isSpecialCaseContained = false;

                for(int i=0; i<specialCase.length; i++) {
                    if(name.contains(specialCase[i]) || name2.contains(specialCase[i])) {
                        isSpecialCaseContained = true;
                    }
                }

                if(name2.length() > 3) {
                    showDialog("이름 입력 오류", "올바른 성이 아닙니다");
                }
                else if (isSpecialCaseContained) {
                    showDialog("이름 입력 오류", "이름이나 성에 특수 문자가 들어가있습니다");
                }
                else {
                    showDialog("이름 입력 확인", "제대로 된 이름 형식입니다.");
                    mNameConfirmed = true;
                }

            }
        };

        return onClickListener;
    }

    private View.OnClickListener verificationOnClickListener(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                String apiURL = "http://54.180.152.145:3000/weatherCall/auth/register/verification";

                Response.Listener verificationListener = new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {

                        System.out.println(response);
                        // 새 액티비티 실행하는 코드
                        Intent intent = new Intent(getBaseContext(),VerificationActivity.class);
                        intent.putExtra("email",cEmailText.getText().toString());
                        startActivity(intent);
                    }
                };

                Response.ErrorListener verificationErrorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                };

                final StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL,
                        verificationListener, verificationErrorListener)
                {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        HashMap<String, String> body = new HashMap<String,String>();

                        // body에 담아서 보내야할 것.
                        // email, password, gender(부,모), name, organization
                        if (mEmailConfirmed){
                            body.put("email",cEmailText.getText().toString());
                        }
                        else {
                            showDialog("이메일 확인 오류","사용 가능한 이메일인지 확인해야합니다.");
                        }
                        // 패스워드 받아와서 요청 바디에 넣기
                        body.put("password",cPasswordText.getText().toString());
                        // 성별 받아아와서 요청 바디에 넣기 (String 형태로 넣어야하니까 밸류값 "male" or "female")
                        if (cCheckBoxMale.isChecked()) {
                            body.put("gender", "Male");
                        }
                        else if(cCheckBoxFemale.isChecked()) {
                            body.put("gender", "Female");
                        }
                        // 이름 받아와서 요청 바디에 넣기
                        if(mNameConfirmed == true) {
                            body.put("name", cName2Text.getText().toString() + cNameText.getText().toString());
                        }
                        // 소속학원 받아와서 요청 바디에 넣기
                        body.put("organization", mSelectedOrganization);

                        System.out.println(body);

                        return body;
                    }
                };

                queue.add(stringRequest);

            }
        };
    }




}




