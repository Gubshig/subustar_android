package com.example.test.MainPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.test.R;

public class StudentMainActivity extends AppCompatActivity {

    private Button cNfcGetOnButton;
    private Button cNfcGetOffButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        cNfcGetOffButton = findViewById(R.id.nfc_get_on);
        cNfcGetOffButton = findViewById(R.id.nfc_get_off);
    }
}