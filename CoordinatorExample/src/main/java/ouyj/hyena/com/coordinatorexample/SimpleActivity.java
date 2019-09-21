package ouyj.hyena.com.coordinatorexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SimpleActivity extends AppCompatActivity {

    public static void start(Context c) {
        c.startActivity(new Intent(c, SimpleActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
    }
}
