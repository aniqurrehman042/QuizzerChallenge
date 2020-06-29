package com.openhomestudio.openhomequizzerchallenge.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.openhomestudio.openhomequizzerchallenge.R;
import com.openhomestudio.openhomequizzerchallenge.models.Mcq;
import com.openhomestudio.openhomequizzerchallenge.models.Result;
import com.openhomestudio.openhomequizzerchallenge.utils.TriviaUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Quiz extends AppCompatActivity implements RewardedVideoAdListener {

    private static final String REWARDED_ID = "ca-app-pub-3940256099942544/5224354917";
    private final String INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712";

    private AdView mAdView;
    private TextView tvCategory;
    private TextView tvQuestionNumber;
    private TextView tvTimer;
    private TextView tvQuestion;
    private TextView tvChoice1;
    private TextView tvChoice2;
    private TextView tvChoice3;
    private TextView tvChoice4;
    private TextView tvHearts;
    private TextView tvScore;

    private Drawable bgChoiceCorrect;
    private Drawable bgChoiceWrong;
    private Drawable bgChoice;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;
    Intent intent;

    private List<Result> results;
    private int position;
    private String categoryNumber;
    private String category;
    private String correctAnswer;
    private int amount = 10;
    private String difficulty;
    private int rightAnswers;
    private boolean clicked;
    private int lives = 3;
    private int correctInRow;
    private boolean isAdLoaded;
    private int score;
    private boolean rewarded;

    private CountDownTimer countDownTimer = new CountDownTimer(20000, 1000) {

        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished > 9999)
                tvTimer.setText(String.valueOf(millisUntilFinished / 1000));
            else
                tvTimer.setText("0" + millisUntilFinished / 1000);
        }

        public void onFinish() {
            if (!clicked) {
                tvTimer.setText("00");
                startAd(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        init();

    }

    private void init() {
        getDrawables();
        getExtras();
        findViews();
        setViews();
        loadAds();
        getQuestions();
    }

    private void setViews() {
        updateQuestionNumber();
        setCategory();
    }

    private void updateScore() {
        score = score + 100;
        tvScore.setText(String.valueOf(score));
    }

    private void updateQuestionNumber() {
        tvQuestionNumber.setText((position + 1) + " / " + amount);
    }

    private void setCategory() {
        tvCategory.setText(category);
    }

    private void getDrawables() {
        bgChoiceCorrect = ResourcesCompat.getDrawable(getResources(), R.drawable.bg_choice_correct, null);
        bgChoiceWrong = ResourcesCompat.getDrawable(getResources(), R.drawable.bg_choice_wrong, null);
        bgChoice = ResourcesCompat.getDrawable(getResources(), R.drawable.bg_choice, null);
    }

    private void getExtras() {
        categoryNumber = getIntent().getStringExtra("categoryNumber");
        category = getIntent().getStringExtra("category");
        difficulty = getIntent().getStringExtra("difficulty");
        amount = Integer.valueOf(getIntent().getStringExtra("amount"));
    }

    private void findViews() {
        tvCategory = findViewById(R.id.tv_category);
        tvQuestion = findViewById(R.id.tv_question);
        tvChoice1 = findViewById(R.id.tv_choice1);
        tvChoice2 = findViewById(R.id.tv_choice2);
        tvChoice3 = findViewById(R.id.tv_choice3);
        tvChoice4 = findViewById(R.id.tv_choice4);
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvTimer = findViewById(R.id.tv_timer);
        tvHearts = findViewById(R.id.tv_hearts);
        tvScore = findViewById(R.id.tv_score);
    }

    private void getQuestions() {
        TriviaUtils.getTriviaClient().getMcqs(String.valueOf(amount), categoryNumber, difficulty, "multiple").enqueue(new Callback<Mcq>() {
            @Override
            public void onResponse(Call<Mcq> call, Response<Mcq> response) {
                results = response.body().getResults();
                updateQuestion(position);
            }

            @Override
            public void onFailure(Call<Mcq> call, Throwable t) {
                getQuestions();
            }
        });
    }

    private void updateQuestion(int position) {
        if (results == null || results.size() == 0) {
            getQuestions();
            return;
        }

        countDownTimer.start();
        updateQuestionNumber();
        Result result = results.get(position);
        correctAnswer = Html.fromHtml(result.getCorrectAnswer()).toString();
        List<String> choices = new ArrayList<>();
        choices.add(correctAnswer);
        choices.add(Html.fromHtml(result.getIncorrectAnswers().get(0)).toString());
        choices.add(Html.fromHtml(result.getIncorrectAnswers().get(1)).toString());
        choices.add(Html.fromHtml(result.getIncorrectAnswers().get(2)).toString());

        Collections.shuffle(choices);

        tvQuestion.setText(Html.fromHtml(result.getQuestion()).toString());
        tvChoice1.setText(choices.get(0));
        tvChoice2.setText(choices.get(1));
        tvChoice3.setText(choices.get(2));
        tvChoice4.setText(choices.get(3));
    }

    private void loadAds() {
        RequestConfiguration conf = new RequestConfiguration.Builder()
                .setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
                .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G)
                .build();

        MobileAds.setRequestConfiguration(conf);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "G");
        AdRequest adRequest = new AdRequest.Builder().
                build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(INTERSTITIAL_ID);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                isAdLoaded = true;
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                startQuizResult();
            }
        });

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(REWARDED_ID,
                new AdRequest.Builder().build());
    }

    private void startQuizResult() {
        startActivity(intent);
        finish();
    }

    public void choiceClick(View view) {
        if (correctAnswer == null || clicked)
            return;

        clicked = true;
        TextView current = (TextView) view;
        if (current.getText().equals(correctAnswer)) {
            updateScore();
            current.setBackground(bgChoiceCorrect);
            rightAnswers++;
            correctInRow++;
            if (correctInRow > 2)
                setLives(false);
        } else {
            current.setBackground(bgChoiceWrong);

            setLives(true);

            correctInRow = 0;

            if (tvChoice1.getText().toString().equals(correctAnswer))
                tvChoice1.setBackground(bgChoiceCorrect);
            else if (tvChoice2.getText().toString().equals(correctAnswer))
                tvChoice2.setBackground(bgChoiceCorrect);
            else if (tvChoice3.getText().toString().equals(correctAnswer))
                tvChoice3.setBackground(bgChoiceCorrect);
            else if (tvChoice4.getText().toString().equals(correctAnswer))
                tvChoice4.setBackground(bgChoiceCorrect);
        }

        resetQuestion();
    }

    private void setLives(boolean down) {
        if (down) {
            lives--;
        } else if (lives < 3) {
            lives++;
            correctInRow = 0;
        }


        switch (lives) {
            case 1:
                tvHearts.setText((char) 0x2764 + "");
                break;
            case 2:
                tvHearts.setText(((char) 0x2764) + "" + ((char) 0x2764));
                break;
            case 3:
                tvHearts.setText(((char) 0x2764) + "" + ((char) 0x2764) + "" + ((char) 0x2764));
                break;
        }
    }

    private void resetQuestion() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (lives == 0) {
                    countDownTimer.cancel();
                    if (!rewarded && position < amount) {
                        new AlertDialog.Builder(Quiz.this).setPositiveButton("Get One Life", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (mRewardedVideoAd.isLoaded())
                                    mRewardedVideoAd.show();
                                else {
                                    Toast.makeText(Quiz.this, "Failed to load video", Toast.LENGTH_SHORT).show();
                                    startAd(true);
                                }
                            }
                        }).setNegativeButton("End Quiz", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startAd(true);
                            }
                        }).setMessage("Watch a video and get an additional life?").setCancelable(false).setTitle("Watch Video")
                                .show();
                    } else
                        startAd(true);
                } else if (position < amount - 1) {
                    countDownTimer.cancel();
                    updateQuestion(++position);
                } else {
                    countDownTimer.cancel();
                    startAd(false);
                }
                tvChoice1.setBackground(bgChoice);
                tvChoice2.setBackground(bgChoice);
                tvChoice3.setBackground(bgChoice);
                tvChoice4.setBackground(bgChoice);
                clicked = false;
            }
        }, 1000);
    }

    private void startAd(boolean isLost) {
        intent = new Intent(Quiz.this, QuizResult.class);
        intent.putExtra("rightAnswers", rightAnswers);
        intent.putExtra("amount", String.valueOf(amount));
        intent.putExtra("isLost", isLost);
        intent.putExtra("score", score);
        if (isAdLoaded)
            mInterstitialAd.show();
        else
            startQuizResult();
    }

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        super.onBackPressed();
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        if (rewarded)
            updateQuestion(++position);
        else
            startAd(true);
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        setLives(false);
        rewarded = true;
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}
