package com.example.test.MainPage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.test.AccountManagement.StudentRegisterActivity;
import com.example.test.R;

public class ParentMainActivity extends AppCompatActivity {

    private TextView cTellParentCode;

    private Button cStudentRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main);

        cTellParentCode.findViewById(R.id.tell_parent_code);
        final String apiURL = "http://54.180.152.145:3000/weatherCall/auth/register/verification/confirm";


        cStudentRegisterButton.findViewById(R.id.student_register_button);
        cStudentRegisterButton.setOnClickListener(stduentregisterbuttononclicklistner());


    }

    View.OnClickListener stduentregisterbuttononclicklistner() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), StudentRegisterActivity.class);
                startActivity(intent);
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