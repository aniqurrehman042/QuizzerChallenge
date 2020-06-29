package com.openhomestudio.openhomequizzerchallenge.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.openhomestudio.openhomequizzerchallenge.R;

public class QuizResult extends AppCompatActivity {

    private TextView tvRightAnswers;
    private TextView tvPostRightAnswers;
    private TextView tvQuizResult;
    private TextView tvScore;

    private int rightAnswers;
    private String amount;
    private boolean isLost;
    private int score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        init();

    }

    private void init() {
        getExtras();
        findViews();
        setViews();
    }

    private void setViews() {
        tvRightAnswers.setText(String.valueOf(rightAnswers));
        tvPostRightAnswers.setText("/ " + amount);
        tvScore.setText(String.valueOf(score));
        if (!isLost)
            tvQuizResult.setText("You Won!");
    }

    private void findViews() {
        tvRightAnswers = findViewById(R.id.tv_right_answers);
        tvPostRightAnswers = findViewById(R.id.tv_post_right_answers);
        tvQuizResult = findViewById(R.id.tv_quiz_result);
        tvScore = findViewById(R.id.tv_score);

    }

    private void getExtras() {
        rightAnswers = getIntent().getIntExtra("rightAnswers", 0);
        amount = getIntent().getStringExtra("amount");
        isLost = getIntent().getBooleanExtra("isLost", false);
        score = getIntent().getIntExtra("score", 0);
    }

    public void backClick(View view) {
        finish();
    }
}
