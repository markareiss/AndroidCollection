package com.makromarko.readysetpomodoro;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    RelativeLayout mainRelativeLayout;
    Button playPauseStopButton;
    TextView timerTextView, pomoCountTextView, currentModeTextView, breakCountTextView;
    Boolean isPlaying, isPomomode;
    CountDownTimer countDownTimer;
    long timeLeft = 1500000;
    String timeLeftString;
    Boolean DEVMODE;

    int pomoCount = 0;
    int breakCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainRelativeLayout = (RelativeLayout) findViewById(R.id.mainRelativeLayout);
        playPauseStopButton = (Button) findViewById(R.id.playPauseStopButton);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        pomoCountTextView = (TextView) findViewById(R.id.pomoCountTextView);
        currentModeTextView = (TextView) findViewById(R.id.currentModeTextView);
        breakCountTextView = (TextView) findViewById(R.id.breakCountTextView);

        //Dev mode bool
        DEVMODE = true;
        timeForDevMode();

        //Starts Paused
        isPlaying = false;
        timeLeftString = convertTimeLeftToString(timeLeft);
        mainRelativeLayout.setBackgroundColor(Color.WHITE);
        currentModeTextView.setText("Waiting Mode");
        pomoCount = 0;

        timerTextView.setText(timeLeftString);

        playPauseStopButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                resetTimeLeft();
                return true;
            }
        });
    }

    private long timeForDevMode() {
        if(DEVMODE){
            timeLeft = 10000;
        }else{
            timeLeft = 1500000;
        }
        return timeLeft;
    }

    private boolean inPomoOrBreakMode(){
        if(pomoCount <= breakCount){
            isPomomode = true;
        }else{
            isPomomode = false;
        }
        return isPomomode;
    }

    private void resetTimeLeft() {
        if(isPomomode){
            timeLeft = 10000;
            //timeForDevMode();
        }else{
            timeLeft = 5000;
        }

        timeLeftString = convertTimeLeftToString(timeLeft);
        timerTextView.setText(timeLeftString);
        isPlaying = false;
        playPauseStopButton.setText("Play");
        countDownTimer.cancel();
        mainRelativeLayout.setBackgroundColor(Color.WHITE);
    }


    public void playPauseToggle(View view) {
        if(isPlaying){
            isPlaying = false;
            playPauseStopButton.setText("Play");
            countDownTimer.cancel();
            mainRelativeLayout.setBackgroundColor(Color.GREEN);
        }else{
            runBackgroundTimer();
            isPlaying = true;

            playPauseStopButton.setText("Pause");
            mainRelativeLayout.setBackgroundColor(Color.RED);

            if(inPomoOrBreakMode()){
                currentModeTextView.setText("Pomo Mode");
                timeLeft = 10000;
            }else{
                currentModeTextView.setText("Break Mode");
                timeLeft = 5000;
            }

            startCountDownTimer();
        }
    }

    private void runBackgroundTimer() {
        final TextView tv = (TextView) findViewById(R.id.counter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;

                while (i < 10){
                    SystemClock.sleep(1000);
                    i++;

                    final int curCount = i;
                    if (curCount % 1 == 0){
                        tv.post(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(curCount * 10 + "% Complete!");
                            }
                        });
                    }
                }

                tv.post(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("Count Complete!");
                    }
                });
            }
        }).start();
    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            public void onFinish(){
                if(inPomoOrBreakMode()){
                    pomoCount++;
                    pomoCountTextView.setText(String.valueOf(pomoCount));
                    currentModeTextView.setText("Break Mode");
                }else {
                    breakCount++;
                    breakCountTextView.setText(String.valueOf(breakCount));
                    currentModeTextView.setText("Pomo Mode");
                }

                playPauseStopButton.setText("DONE");

                timeLeft = 0;
                timeLeftString = convertTimeLeftToString(timeLeft);
                timerTextView.setText(timeLeftString);

                Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

                final MediaPlayer ring = MediaPlayer.create(getApplicationContext(), alarm);
                ring.start();

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("End Noise");
                alertDialog.setMessage("Press 'END' to end alarm");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "END", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ring.stop();
                        dialog.dismiss();
                    }
                });
                alertDialog.show();

                //Ringtone ring = RingtoneManager.getRingtone(getApplicationContext(), alarm);
                //ring.play();
            }
            public void onTick(long millisUntilFinished){
                timeLeft = millisUntilFinished;
                timeLeftString = convertTimeLeftToString(timeLeft);
                timerTextView.setText(timeLeftString);
            }
        }.start();
    }



    private String convertTimeLeftToString(long time) {
        String timeString = String.format("%02d min, %02d sec",
                java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(time),
                java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(time) -
                        java.util.concurrent.TimeUnit.MINUTES.toSeconds(java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(time))
        );

        return timeString;
    }
}


