package com.example.test.MainPage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.test.AccountManagement.StudentRegisterActivity;
import com.example.test.R;

import java.util.ArrayList;

public class ParentMainActivity extends AppCompatActivity {

    private TextView cTellParentCode;

    private Button cStudentRegisterButton;

    private ListView cStudentListView;

    private ArrayList<String> mStudentListViewArray = new ArrayList<String>();
    private ArrayAdapter mStudentListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main);

        cTellParentCode = findViewById(R.id.tell_parent_code);
        SharedPreferences sharedPreferences = getSharedPreferences("user_code",0);
        String parentCode = sharedPreferences.getString("parent_code","");
        cTellParentCode.setText(parentCode);


        cStudentRegisterButton = findViewById(R.id.student_register_button);
        cStudentRegisterButton.setOnClickListener(stduentregisterbuttononclicklistner());

        cStudentListView = findViewById(R.id.student_list);
        mStudentListViewAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, mStudentListViewArray);
        cStudentListView.setAdapter(mStudentListViewAdapter);

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