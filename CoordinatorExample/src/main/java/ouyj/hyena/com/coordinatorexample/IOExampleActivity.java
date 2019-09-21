package ouyj.hyena.com.coordinatorexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class IOExampleActivity extends AppCompatActivity {

    public static void start(Context c) {
        c.startActivity(new Intent(c, IOExampleActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ioexample);

        //查找工具栏（点击后回退当前活动）
        Toolbar toolbar = findViewById(R.id.ioexample_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                //回退
                onBackPressed();
            }
        });
    }
}
