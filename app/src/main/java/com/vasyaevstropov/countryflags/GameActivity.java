package com.vasyaevstropov.countryflags;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vasyaevstropov.countryflags.database.DatabaseAccess;
import com.vasyaevstropov.countryflags.database.ScorePreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity {
    ImageSwitcher flagSwitcher;
    ImageView imageView, imageView2;
    Button btnVar1, btnVar2, btnVar3, btnVar4;
    TextView tvGold, tvScore, tvTime;
    CountDownTimer countDownTimer;
    Animation animationGold;
    private final Integer TIMER_START = 1;
    private final Integer TIMER_STOP = 0;
    Integer gold = 0;
    Integer rightAnswer=1;
    int m1liseconds = 60000;
    Integer rQuestion1, rQuestion2, rQuestion3,rQuestion4;
    ArrayList<String> qList1 = new ArrayList<>();
    ArrayList<String> qList2 = new ArrayList<>();
    ArrayList<String> qList3 = new ArrayList<>();
    ArrayList<String> qList4 = new ArrayList<>();
    SharedPreferences preferences;
    private Integer images[] = {R.drawable.a1, R.drawable.a2};
    private final Integer COUNTRY_ID = 0;
    private final Integer COUNTRY_NAME_RU = 1;
    private final Integer COUNTRY_AREA = 2;
    private final Integer COUNTRY_NAME_EN = 3;
    int currImage =0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initialize();
        randomQuestions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTime(TIMER_START, m1liseconds);

    }

    @Override
    protected void onPause() {
        super.onPause();
        startTime(TIMER_STOP, m1liseconds);

    }

    private void startTime(int startOrStop, int miliseconds ) {
       if (startOrStop == TIMER_START) {
           countDownTimer = new CountDownTimer(miliseconds, 1000) {
               @Override
               public void onTick(long millisUntilFinished) {
                   //tvTime.setText("00:" + millisUntilFinished / 1000);
                   tvTime.setText(String.format("%02d:%02d",
                           TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                           TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                   m1liseconds = (int) millisUntilFinished;
               }

               @Override
               public void onFinish() {
                   tvTime.setText("00:00");
                   ScorePreferences.init(getBaseContext());
                   ScorePreferences.addProperty(false); //обнуление в преференсах счетчика побед.
                   AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                   builder.setTitle("Поражение. Время завершилось...")
                           .setMessage("Сыграть еще ?")
                           //.setIcon(R.drawable.goldcup)
                           .setCancelable(false)
                           .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   finish();
                                   Intent startGame1 = new Intent(getBaseContext(), GameActivity.class);
                                   startActivity(startGame1);
                               }
                           })
                           .setNegativeButton("В меню",
                                   new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog, int id) {
                                           finish();
                                           Intent ma = new Intent(getBaseContext(), MainActivity.class);
                                           startActivity(ma);
                                       }
                                   });
                   AlertDialog alert = builder.create();
                   alert.show();
               }
           }.start();
       }
       else{
           countDownTimer.cancel();
       }
    }

    private void initialize() {
        animationGold = AnimationUtils.loadAnimation(this, R.anim.mytrans);
        flagSwitcher = (ImageSwitcher) findViewById(R.id.flagSwitcher);
        tvGold = (TextView) findViewById(R.id.tvGold);
        tvGold.setVisibility(View.INVISIBLE);
        btnVar1 = (Button) findViewById(R.id.btnVar1);
        btnVar2 = (Button) findViewById(R.id.btnVar2);
        btnVar3 = (Button) findViewById(R.id.btnVar3);
        btnVar4 = (Button) findViewById(R.id.btnVar4);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        tvScore = (TextView) findViewById(R.id.tvScore);
        tvTime = (TextView) findViewById(R.id.tvTime);
        //flagAnimation();
        updateScore(0);
        btnVar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedVarriant(1);

            }
        });
        btnVar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedVarriant(2);
            }
        });
        btnVar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedVarriant(3);
            }
        });
        btnVar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedVarriant(4);
            }
        });
    }

    private void wrongAnswer(){ // при ошибочном ответе
        flagAnimation();
        flagSwitcher.showNext();
        tvGold.setText("-150 $");
        tvGold.setTextColor(getResources().getColor(R.color.red));
        tvGold.startAnimation(animationGold);
        tvGold.setVisibility(View.VISIBLE);
        tvGold.setVisibility(View.INVISIBLE);
        updateScore(-150);
    }
    private void rightAnswer(){ //при правильном ответе
        flagAnimation();
        flagSwitcher.showNext();
        tvGold.setText("+250 $");
        tvGold.setTextColor(getResources().getColor(R.color.colorGreen));
        tvGold.startAnimation(animationGold);
        tvGold.setVisibility(View.VISIBLE);
        tvGold.setVisibility(View.INVISIBLE);
        updateScore(+250);

    }
    private void winner(){
        ScorePreferences.init(getBaseContext());
        ScorePreferences.addProperty(true);

        startTime(TIMER_STOP, m1liseconds);
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("ПОБЕДА!!! У Вас " + gold + "$ !")
                .setMessage("Сыграть еще ?")
                //.setIcon(R.drawable.goldcup)
                .setCancelable(false)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent startGame1 = new Intent(getBaseContext(), GameActivity.class);
                        startActivity(startGame1);
                    }
                })
                .setNegativeButton("В меню",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                Intent ma = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(ma);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void flagAnimation(){  // Анимация смены ФЛАГА
       // Animation inAnimation = new AlphaAnimation(0, 1);
       // Animation outAnimation = new AlphaAnimation(1, 0);
        Animation inAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation outAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        inAnimation.setDuration(500);
        outAnimation.setDuration(500);

        flagSwitcher.setInAnimation(inAnimation);
        flagSwitcher.setOutAnimation(outAnimation);



    }
    private void updateScore(int score){
        gold = gold + (score);
        tvScore.setText(gold+"$ из 1000$");
        if (gold>=0) {
            tvScore.setTextColor(getResources().getColor(R.color.colorGreen));
        } else if (gold<0){
            tvScore.setTextColor(getResources().getColor(R.color.red));
        }
        /***********/
        if (gold>=1000){
            winner();
        }
    }
    private void nextQuestion(Integer rQuestion1, Integer rQuestion2, Integer rQuestion3, Integer rQuestion4, Integer rightAnswer){
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        qList1 = databaseAccess.getQuestion(rQuestion1); //список первого варрианта, ИД(0), СТРАНА(1), ПЛОЩАДЬ(2), страна на английском(3)
        qList2 = databaseAccess.getQuestion(rQuestion2);
        qList3 = databaseAccess.getQuestion(rQuestion3);
        qList4 = databaseAccess.getQuestion(rQuestion4);

        btnVar1.setText(qList1.get(COUNTRY_NAME_RU));
        btnVar2.setText(qList2.get(COUNTRY_NAME_RU));
        btnVar3.setText(qList3.get(COUNTRY_NAME_RU));
        btnVar4.setText(qList4.get(COUNTRY_NAME_RU));

        switch (rightAnswer){
            case 1:
                flagSwitcher.setImageResource(getResources().getIdentifier("a"+qList1.get(COUNTRY_ID),"drawable", getPackageName()));
                break;
            case 2:
                flagSwitcher.setImageResource(getResources().getIdentifier("a"+qList2.get(COUNTRY_ID),"drawable", getPackageName()));
                break;
            case 3:
                flagSwitcher.setImageResource(getResources().getIdentifier("a"+qList3.get(COUNTRY_ID),"drawable", getPackageName()));
                break;
            case 4:
                flagSwitcher.setImageResource(getResources().getIdentifier("a"+qList4.get(COUNTRY_ID),"drawable", getPackageName()));
                break;
            default:break;
        }



        databaseAccess.close();
    }
    private void randomQuestions(){

        Random random = new Random();
        rQuestion1 = random.nextInt( 194 - 1 ) + 1 ;
        rQuestion2 = random.nextInt( 194 - 1 ) + 1 ;
        rQuestion3 = random.nextInt( 194 - 1 ) + 1 ;
        rQuestion4 = random.nextInt( 194 - 1 ) + 1 ;

        rightAnswer = random.nextInt( 4 - 1 + 1) + 1 ;
       // Toast.makeText(this, rightAnswer + "", Toast.LENGTH_SHORT).show();
        nextQuestion(rQuestion1, rQuestion2, rQuestion3, rQuestion4, rightAnswer);

    }

    private void selectedVarriant(Integer selectedVarriant){
      if (selectedVarriant.equals(rightAnswer)) {
            rightAnswer();
        } else{
            wrongAnswer();
        }
        randomQuestions();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startTime(TIMER_STOP,m1liseconds);
        finish();
        Intent ma = new Intent(getBaseContext(),MainActivity.class);
        startActivity(ma);

    }

}
