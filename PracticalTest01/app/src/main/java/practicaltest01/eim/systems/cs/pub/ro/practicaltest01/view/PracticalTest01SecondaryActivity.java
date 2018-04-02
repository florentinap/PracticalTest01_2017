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
            switch (view.getId()) {
                case R.id.ok:
                    setResult(RESULT_OK);
                    break;
                case R.id.cancel:
                    setResult(RESULT_CANCELED);
                    break;
            }
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_secondary);

        ok = findViewById(R.id.ok);
        ok.setOnClickListener(buttonClickListener);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(buttonClickListener);

        text = findViewById(R.id.textView);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras().containsKey(Constants.CLICKS)) {
            String clicks = intent.getStringExtra(Constants.CLICKS);
            text.setText(clicks);
        }
    }
}
