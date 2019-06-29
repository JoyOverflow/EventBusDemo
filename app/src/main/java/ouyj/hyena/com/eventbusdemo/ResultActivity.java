package ouyj.hyena.com.eventbusdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //发送非粘性事件并关闭此活动（事件发布者Publisher）
        EventBus.getDefault().post(new Message("欢迎大家访问我的博客!"));
        finish();
    }
}

