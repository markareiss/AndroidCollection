package com.makromarko.readysetpomodoro;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.AcceptDenyDialogFragment;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;

    //LinearLayout mainLinearLayout;
    RelativeLayout mainLinearLayout;
    Button playPauseStopButton;
    TextView timerTextView, pomoCountTextView, currentModeTextView;
    Boolean isPlaying;
    CountDownTimer countDownTimer;
    long timeLeft = 10000;
    String timeLeftString;
    //long timeLeft = 1500000;
    Boolean DEVMODE;
    int pomoCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        //mTextView = (TextView) findViewById(R.id.text);
        //mClockView = (TextView) findViewById(R.id.clock);

        isPlaying = false;

        timerTextView = (TextView) findViewById(R.id.timerTextView);

        playPauseStopButton = (Button) findViewById(R.id.playPauseStopButton);

        playPauseStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPauseToggleWear();
            }
        });

        //mainLinearLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);
        mainLinearLayout = (RelativeLayout) findViewById(R.id.mainLinearLayout);
        mainLinearLayout.setBackgroundColor(Color.WHITE);

    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mTextView.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setVisibility(View.VISIBLE);

            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.GONE);
        }
    }

    public void playPauseToggleWear() {
        if(isPlaying){
            isPlaying = false;
            playPauseStopButton.setText("Play");
            countDownTimer.cancel();
            mainLinearLayout.setBackgroundColor(Color.RED);

        }else{
            isPlaying = true;
            //currentModeTextView.setText("Pomo Mode");
            playPauseStopButton.setText("Pause");
            mainLinearLayout.setBackgroundColor(Color.GREEN);
            startCountDownTimer();
        }

    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            public void onFinish(){
                pomoCount++;
                //pomoCountTextView.setText(String.valueOf(pomoCount));
                playPauseStopButton.setText("DONE");
                //currentModeTextView.setText("Break Mode");
                timeLeft = 0;
                timeLeftString = convertTimeLeftToString(timeLeft);
                timerTextView.setText(timeLeftString);

                //Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

                //MediaPlayer ring = MediaPlayer.create(getApplicationContext(), alarm);
                //ring.start();

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
