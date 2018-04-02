package practicaltest01.eim.systems.cs.pub.ro.practicaltest01.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;
import java.util.Random;

import practicaltest01.eim.systems.cs.pub.ro.practicaltest01.general.Constants;

/**
 * Created by Florentina on 02-Apr-18.
 */

public class ProcessingThread extends Thread {

    private Context context = null;
    private boolean isRunning = true;
    private Random rand = new Random();

    private double aritmetic;
    private double geometric;

    public ProcessingThread(Context context, int firstNumber, int secondNumber) {
        this.context = context;

        aritmetic = (firstNumber + secondNumber)/2;
        geometric = Math.sqrt(firstNumber * secondNumber);
    }

    @Override
    public void run() {
        Log.d("[TEST]", "Thread has started!");

        while (isRunning) {
            sendMessage();
            sleep();
        }

        Log.d("[TEST]", "Thread has stopped!");
    }

    private void sendMessage() {
        Intent intent = new Intent();
        intent.setAction(Constants.actionTypes[rand.nextInt(Constants.actionTypes.length)]);
        intent.putExtra("message", new Date(System.currentTimeMillis()) + " "  + aritmetic + " " + geometric);
        context.sendBroadcast(intent);
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}