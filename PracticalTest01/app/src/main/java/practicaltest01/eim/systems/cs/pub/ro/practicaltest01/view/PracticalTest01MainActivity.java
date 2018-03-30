package practicaltest01.eim.systems.cs.pub.ro.practicaltest01.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import practicaltest01.eim.systems.cs.pub.ro.practicaltest01.R;
import practicaltest01.eim.systems.cs.pub.ro.practicaltest01.general.Constants;
import practicaltest01.eim.systems.cs.pub.ro.practicaltest01.service.PracticalTest01Service;

public class PracticalTest01MainActivity extends AppCompatActivity {

    Button left_button, right_button, navigate_button;
    EditText left_text, right_text;

    private int serviceStatus = Constants.SERVICE_STOPPED;
    private IntentFilter intentFilter = new IntentFilter();

    private ButtonClickListener buttonClickListener = new ButtonClickListener();

    private class ButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            int leftNumberOfClicks = Integer.valueOf(left_text.getText().toString());
            int rightNumberOfClicks = Integer.valueOf(right_text.getText().toString());

            switch (view.getId()) {
                case R.id.left_button:
                    leftNumberOfClicks++;
                    left_text.setText(String.valueOf(leftNumberOfClicks));
                    break;
                case R.id.right_button:
                    rightNumberOfClicks++;
                    right_text.setText(String.valueOf(rightNumberOfClicks));
                    break;
                case R.id.navigate_to_secondary_activity_button:
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01SecondaryActivity.class);
                    int nr_clicks = leftNumberOfClicks + rightNumberOfClicks;
                    intent.putExtra(Constants.NUMBER_OF_CLICKS, nr_clicks);
                    startActivityForResult(intent, Constants.SECONDARY_ACTIVITY_REQUEST_CODE);
                    break;
            }
            if (leftNumberOfClicks + rightNumberOfClicks > Constants.NUMBER_OF_CLICKS_THRESHOLD
                    && serviceStatus == Constants.SERVICE_STOPPED) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                intent.putExtra(Constants.FIRST_NUMBER, leftNumberOfClicks);
                intent.putExtra(Constants.SECOND_NUMBER, rightNumberOfClicks);
                getApplicationContext().startService(intent);
                serviceStatus = Constants.SERVICE_STARTED;
            }
        }
    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();

    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(Constants.BROADCAST_RECEIVER_TAG, intent.getStringExtra(Constants.BROADCAST_RECEIVER_EXTRA));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        left_button = findViewById(R.id.left_button);
        left_button.setOnClickListener(buttonClickListener);
        right_button = findViewById(R.id.right_button);
        right_button.setOnClickListener(buttonClickListener);
        navigate_button = findViewById(R.id.navigate_to_secondary_activity_button);
        navigate_button.setOnClickListener(buttonClickListener);

        left_text = findViewById(R.id.left_edit_text);
        right_text = findViewById(R.id.right_edit_text);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("left_text")) {
                left_text.setText(savedInstanceState.getString("left_text"));
            } else {
                left_text.setText(String.valueOf(0));
            }
            if (savedInstanceState.containsKey("right_text")) {
                right_text.setText(savedInstanceState.getString("right_text"));
            } else {
                right_text.setText(String.valueOf(0));
            }
        } else {
            left_text.setText(String.valueOf(0));
            right_text.setText(String.valueOf(0));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString(Constants.LEFT_COUNT, left_text.getText().toString());
        savedInstanceState.putString(Constants.RIGHT_COUNT, right_text.getText().toString());
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
