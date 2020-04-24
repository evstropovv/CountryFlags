package com.vasyaevstropov.countryflags;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vasyaevstropov.countryflags.database.ScorePreferences;

public class MainActivity extends AppCompatActivity {
    Button btnGame1, btnExit;
    TextView tvWinScore;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        btnGame1 = (Button)findViewById(R.id.btnGame1);
        btnGame1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent startGame1 = new Intent(getBaseContext(), GameActivity.class);
                startActivity(startGame1);
            }
        });
        btnExit = (Button)findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tvWinScore = (TextView)findViewById(R.id.tvWinScore);
        ScorePreferences.init(getBaseContext());
        tvWinScore.setText("Побед подряд: " + ScorePreferences.getProperty());
    }
}
