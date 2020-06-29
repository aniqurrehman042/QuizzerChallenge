package com.openhomestudio.openhomequizzerchallenge.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.openhomestudio.openhomequizzerchallenge.R;

public class DifficultySelection extends AppCompatActivity {

    private String difficulty;
    private String categoryNumber;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_selection);

        getExtras();
    }

    private void getExtras() {
        categoryNumber = String.valueOf(getIntent().getIntExtra("categoryNumber", 9));
        category = getIntent().getStringExtra("category");
    }

    public void difficultyClick(View view) {
        switch (view.getId()) {
            case R.id.ll_difficulty_easy:
                difficulty = "easy";
                break;
            case R.id.ll_difficulty_medium:
                difficulty = "medium";
                break;
            case R.id.ll_difficulty_hard:
                difficulty = "hard";
                break;
        }

        Intent intent = new Intent(this, AmountSelection.class);
        intent.putExtra("difficulty", difficulty);
        intent.putExtra("categoryNumber", categoryNumber);
        intent.putExtra("category", category);
        startActivity(intent);
        finish();

    }
}
