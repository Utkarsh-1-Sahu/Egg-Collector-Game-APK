package com.example.eggcollector;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class gameActivity extends AppCompatActivity {

    ImageView s, s2;
    ImageButton left, right;
    float x;
    ImageView egg[] = new ImageView[5];
    float imgY;
    int sc = 0, drop = 0;
    TextView scoreBar, dropBar, levelBar;
    int lev = 1;
    int barSpeed = 45;
    LinearLayout game_screen, results;
    TextView resultsScore, resultsDrops, resultsPercent;
    MediaPlayer coin_sound, gameOverSound;
    Button gobackbtn;
    boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        s = findViewById(R.id.slideBar);
        x = s.getX();
        left = findViewById(R.id.leftBut);
        right = findViewById(R.id.rightBut);

        left.setMinimumWidth((Resources.getSystem().getDisplayMetrics().widthPixels) / 2);
        right.setMinimumWidth((Resources.getSystem().getDisplayMetrics().widthPixels) / 2);

        scoreBar = findViewById(R.id.score);
        dropBar = findViewById(R.id.drops);
        levelBar = findViewById(R.id.level);

        egg[0] = findViewById(R.id.egg1);
        egg[1] = findViewById(R.id.egg2);
        egg[2] = findViewById(R.id.egg3);
        egg[3] = findViewById(R.id.egg4);
        egg[4] = findViewById(R.id.egg5);

        imgY = egg[0].getY();
        game_screen = findViewById(R.id.game_screen);
        results = findViewById(R.id.results);

        results.setVisibility(View.INVISIBLE);
        game_screen.setVisibility(View.VISIBLE);

        resultsScore = findViewById(R.id.resultsScore);
        resultsDrops = findViewById(R.id.resultsDrops);
        resultsPercent = findViewById(R.id.resultsPercent);

        coin_sound = MediaPlayer.create(getApplicationContext(), R.raw.sound);
        gameOverSound = MediaPlayer.create(getApplicationContext(), R.raw.gameoversound);

        gobackbtn = findViewById(R.id.gobackbtn);

        gobackbtn.setHighlightColor(Color.BLACK);



        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    hitLeft();
                }
                return true;
            }
        });
        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                if (action == MotionEvent.ACTION_DOWN){
                    hitRight();
                }
                return true;
            }
        });

        for(int i = 1; i <= 200; i++){
//            if(!flag)
//                break;
            new Handler().postDelayed(() -> moveEgg(), (lev == 1) ? i*1000 : (( lev == 2) ? i*650 : ((lev == 3) ? i * 450 : (lev == 4) ? i * 350 : i * 250)));
        }

        gobackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    @Override
    public void onBackPressed(){
//        coin_sound = null;
        flag = false;
        super.onBackPressed();
//        finish();
    }

    public void hitLeft(){
        if(x < barSpeed)
            x = 0;
        else
            x = x - barSpeed;
        s.setX(x);
    }

    public void hitRight(){
        if(x > (Resources.getSystem().getDisplayMetrics().widthPixels - barSpeed - s.getWidth()))
            x = Resources.getSystem().getDisplayMetrics().widthPixels - s.getWidth();

        else
            x += barSpeed;
        s.setX(x);
    }

    public void moveEgg(){
        int r = (int)(Math.random()*(egg.length));
        while (egg[r].getVisibility() == View.VISIBLE)
            r = (int)(Math.random() * (egg.length));
        final int a = r;
        egg[r].setVisibility(View.VISIBLE);
        s2 = findViewById(R.id.slideBar);
        float y = s2.getY(); //1150f;
        ObjectAnimator animation = ObjectAnimator.ofFloat( egg[r], "translationY", y);
        animation.setDuration(getSpeed());
        animation.start();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(egg[a].getX() >= (s2.getX()) && (egg[a].getX() <= (s2.getX() + s2.getWidth())) && (s2.getY() == egg[a].getY()) && flag) {
//                    s2.getGlobalVisibleRect(rect);
                    coin_sound.start();
                    setScore(true);
                }
                else
                    setScore(false);
                egg[a].setVisibility(View.INVISIBLE);
                egg[a].setY(imgY);
            }
        }, (getSpeed() + 100));
    }

    public int getSpeed(){
        switch (lev){
            case 1 :
                return 4000;
            case 2 :
                return 3000;
            case 3 :
                return 2000;
            case 4 :
                return 1600;
            case 5 :
                return 1200;
        }
        return 4000;
    }
    public void setScore(boolean isGot){
        if(isGot) {
            sc++;
        }
        else
            drop++;
        scoreBar.setText("Score : "+sc);
        dropBar.setText("Drops : "+drop);

        if((sc == 125) && (lev != 5)){
            lev = 5;
            Toast toast = Toast.makeText(getApplicationContext(), "Level 5", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if((sc == 100) && (lev != 4)){
            lev = 4;
            Toast toast = Toast.makeText(getApplicationContext(), "Level 4", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if((sc == 65) && (lev != 3)){
            lev = 3;
            Toast toast = Toast.makeText(getApplicationContext(), "Level 3", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if((sc == 30) && (lev != 2)) {
            lev = 2;
            Toast toast = Toast.makeText(getApplicationContext(), "Level 2", Toast.LENGTH_SHORT);
            toast.show();
        }
        levelBar.setText("Level: "+lev);
        if((sc + drop) == 200){
            gameOverSound.start();
            game_screen.setVisibility(View.INVISIBLE);
            results.setVisibility(View.VISIBLE);
            resultsScore.setText("Score : "+sc);
            resultsDrops.setText("Drops : "+drop);
            resultsPercent.setText("Catch Percent : "+(sc/2.0)+"%");
        }
    }
}