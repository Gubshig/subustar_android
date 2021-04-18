package com.example.test.AccountManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.test.R;

public class StartActivity extends AppCompatActivity {

    Button cStudentLoginButton;
    Button cParentLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        cStudentLoginButton = findViewById(R.id.student_login_button2);
        cParentLoginButton = findViewById(R.id.parent_login_button);
        cParentLoginButton.setOnClickListener(parentLoginButtonOnClickListener());
        cStudentLoginButton.setOnClickListener(studentLoginButtonOnClickListener());

    }

    View.OnClickListener studentLoginButtonOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 학생 로그인 액티비티 실행하는 코드
                Intent intent = new Intent(getBaseContext(),StudentLoginActivity.class);
                startActivity(intent);

            }
        };
    }

    View.OnClickListener parentLoginButtonOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 학부모 로그인 액티비티 실행하는 코드
                Intent intent = new Intent(getBaseContext(),ParentLoginActivity.class);
                startActivity(intent);

            }
        };
    }

}