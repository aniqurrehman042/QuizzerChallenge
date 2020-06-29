package com.openhomestudio.openhomequizzerchallenge.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.openhomestudio.openhomequizzerchallenge.R;

public class AmountSelection extends AppCompatActivity {

    private String amount;
    private String difficulty;
    private String categoryNumber;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount_selection);

        getExtras();
    }

    private void getExtras() {
        categoryNumber = getIntent().getStringExtra("categoryNumber");
        category = getIntent().getStringExtra("category");
        difficulty = getIntent().getStringExtra("difficulty");
    }

    public void amountClick(View view) {
        switch (view.getId()) {
            case R.id.ll_amount_10:
                amount = "10";
                break;
            case R.id.ll_amount_20:
                amount = "20";
                break;
            case R.id.ll_amount_30:
                amount = "30";
                break;
        }

        Intent intent = new Intent(this, Quiz.class);
        intent.putExtra("difficulty", difficulty);
        intent.putExtra("categoryNumber", categoryNumber);
        intent.putExtra("category", category);
        intent.putExtra("amount", amount);
        startActivity(intent);
        finish();

    }
}
