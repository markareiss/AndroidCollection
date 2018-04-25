package com.makromarko.readysetpomodoro;

public abstract class CountDown {

    long startTime;
    long timeLeft = startTime;

    public String toString(long time){

        String timeString = String.format("%02d min, %02d sec",
                java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(timeLeft),
                java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(timeLeft) -
                        java.util.concurrent.TimeUnit.MINUTES.toSeconds(java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(timeLeft))
        );

        return timeString;
    }

}
