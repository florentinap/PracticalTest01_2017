package practicaltest01.eim.systems.cs.pub.ro.practicaltest01.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import practicaltest01.eim.systems.cs.pub.ro.practicaltest01.R;
import practicaltest01.eim.systems.cs.pub.ro.practicaltest01.general.Constants;

public class PracticalTest01SecondaryActivity extends AppCompatActivity {

    Button ok, cancel;
    TextView text;

    private ButtonClickListener buttonClickListener = new ButtonClickListener();

    private class ButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.ok_button) {
                setResult(RESULT_OK);
            }
            if (view.getId() == R.id.cancel_button) {
                setResult(RESULT_CANCELED);
            }
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_secondary);

        ok = findViewById(R.id.ok_button);
        ok.setOnClickListener(buttonClickListener);
        cancel = findViewById(R.id.cancel_button);
        cancel.setOnClickListener(buttonClickListener);

        text = findViewById(R.id.number_of_clicks_text_view);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras().containsKey(Constants.NUMBER_OF_CLICKS)) {
            int numberOfClicks = intent.getIntExtra(Constants.NUMBER_OF_CLICKS, -1);
            text.setText(String.valueOf(numberOfClicks));
        }
    }
}
