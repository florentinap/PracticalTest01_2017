package practicaltest01.eim.systems.cs.pub.ro.practicaltest01.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import practicaltest01.eim.systems.cs.pub.ro.practicaltest01.R;
import practicaltest01.eim.systems.cs.pub.ro.practicaltest01.general.Constants;
import practicaltest01.eim.systems.cs.pub.ro.practicaltest01.service.PracticalTest01Service;

public class PracticalTest01MainActivity extends AppCompatActivity {

    Button leftBtn, rightBtn, navigate;
    TextView leftText, rightText;

    private int serviceStatus = Constants.SERVICE_STOPPED;
    private IntentFilter intentFilter = new IntentFilter();

    private ButtonClickListener buttonClickListener = new ButtonClickListener();

    private class ButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            int nr_left = Integer.valueOf(leftText.getText().toString());
            int nr_right = Integer.valueOf(rightText.getText().toString());

            switch (view.getId()) {
                case R.id.left_button:
                    nr_left ++;
                    leftText.setText(String.valueOf(nr_left));
                    break;
                case R.id.right_button:
                    nr_right ++;
                    rightText.setText(String.valueOf(nr_right));
                    break;
                case R.id.navigate_to_secondary_activity_button:
                    Intent intent1 = new Intent(getApplicationContext(), PracticalTest01SecondaryActivity.class);
                    int clicks = nr_left + nr_right;
                    intent1.putExtra(Constants.CLICKS, String.valueOf(clicks));
                    startActivityForResult(intent1, Constants.SECONDARY_ACTIVITY_REQUEST_CODE);
                    break;
            }

            if (nr_left + nr_right >= Constants.PRAG && serviceStatus == Constants.SERVICE_STOPPED) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                intent.putExtra(Constants.FIRST_NUMBER, nr_left);
                intent.putExtra(Constants.SECOND_NUMBER, nr_right);
                getApplicationContext().startService(intent);
                serviceStatus = Constants.SERVICE_STARTED;
            }
        }
    }
    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();

    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("[TEST]", intent.getStringExtra("message"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        leftBtn = findViewById(R.id.left_button);
        leftBtn.setOnClickListener(buttonClickListener);
        rightBtn = findViewById(R.id.right_button);
        rightBtn.setOnClickListener(buttonClickListener);
        navigate = findViewById(R.id.navigate_to_secondary_activity_button);
        navigate.setOnClickListener(buttonClickListener);

        leftText = findViewById(R.id.left_edit_text);
        rightText = findViewById(R.id.right_edit_text);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("nr_left")) {
                leftText.setText(savedInstanceState.getString("nr_left"));
            } else {
                leftText.setText(String.valueOf(0));
            }
            if (savedInstanceState.containsKey("nr_right")) {
                rightText.setText(savedInstanceState.getString("nr_right"));
            } else {
                rightText.setText(String.valueOf(0));
            }
        } else {
            leftText.setText(String.valueOf(0));
            rightText.setText(String.valueOf(0));
        }

        for (int index = 0; index < Constants.actionTypes.length; index++) {
            intentFilter.addAction(Constants.actionTypes[index]);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString(Constants.NR_LEFT, leftText.getText().toString());
        savedInstanceState.putString(Constants.NR_RIGHT, rightText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState.containsKey("nr_left")) {
            leftText.setText(savedInstanceState.getString("nr_left"));
        } else {
            leftText.setText(String.valueOf(0));
        }
        if (savedInstanceState.containsKey("nr_right")) {
            rightText.setText(savedInstanceState.getString("nr_right"));
        } else {
            rightText.setText(String.valueOf(0));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == Constants.SECONDARY_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Service.class);
        stopService(intent);
        super.onDestroy();
    }

}
