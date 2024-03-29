package ouyj.hyena.com.materialsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener{

    private Button defaultButton;
    private Button themedButton;
    private Button voiceButton;
    private Button stickyButton;
    private Button tabButton;
    private Button inputTypeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defaultButton = findViewById(R.id.button_default);
        defaultButton.setOnClickListener(this);
        themedButton = findViewById(R.id.button_themed);
        themedButton.setOnClickListener(this);
        voiceButton = findViewById(R.id.button_voice);
        voiceButton.setOnClickListener(this);
        stickyButton = findViewById(R.id.button_sticky);
        stickyButton.setOnClickListener(this);
        tabButton = findViewById(R.id.button_tab);
        tabButton.setOnClickListener(this);
        inputTypeButton = findViewById(R.id.button_input);
        inputTypeButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button_default:
                intent = new Intent(this, DefaultActivity.class);
                break;
            case R.id.button_themed:
                intent = new Intent(this, ColoredActivity.class);
                break;
            case R.id.button_voice:
                intent = new Intent(this, VoiceActivity.class);
                break;
            case R.id.button_sticky:
                intent = new Intent(this, StickyActivity.class);
                break;
            case R.id.button_tab:
                intent = new Intent(this, TabActivity.class);
                break;
            case R.id.button_input:
                intent = new Intent(this, InputActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
