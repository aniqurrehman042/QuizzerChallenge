package com.openhomestudio.openhomequizzerchallenge.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.openhomestudio.openhomequizzerchallenge.R;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void backClick(View view) {
        finish();
    }
}
