package ouyj.hyena.com.eventbusdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * EventBus（Event事件，Subscriber订阅者，Publisher发布者）事件发布/订阅框架（不支持跨进程）
 * 1.首先在一个活动里注册EventBus事件和解除注册
 * 2.定义订阅者并指定线程模型
 *   Posting (默认) 表示发布者和订阅者（事件处理）在同一线程
 *   Main 表示事件处理在主(UI)线程（因此不能进行耗时操作）
 *   Background 如果发布者在主(UI)线程那么事件处理会开启一个后台线程，如果发布者是在后台线程那么事件处理也会使用该线程。
 *   Async 事件处理始终新建一个线程来运行（不能进行UI操作）
 * 3.自定义事件类（用来传输）
 * 4.定义发送者来传递自定义事件类
 */
public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private TextView mText;

    /**
     * 注册EventBus
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText = findViewById(R.id.txtTile);
        mButton = findViewById(R.id.btnTest);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(
                        MainActivity.this,
                        ResultActivity.class
                );
                startActivity(intent);
            }
        });

        //静态getDefault()方法返回一个EventBus单例对象
        //1.去获取当前注册对象里所有的被@Subscribe注解的方法集合
        EventBus.getDefault().register(this);
    }
    /**
     * 解除EventBus
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
    /**
     * 声明订阅者来处理事件（必需加上特定注解）
     * 线程模式为运行在主（UI）线程，因此不能进行耗时操作
     * 当多个订阅者对同一事件类型进行订阅时，优先级越高越先获得消息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = false,priority = 100)
    public void receiveEvent(Message event) {
        mText.setText(event.getTitle());

        //终止对事件的传递（订阅了该事件的低优先级者将接收不到该事件）
        EventBus.getDefault().cancelEventDelivery(event);
    }
    /**
     * 粘性事件
     * @param event
     */
    @Subscribe(priority = 100,sticky = true)
    public void receiveStickyEvent(Message event){
        Toast.makeText(
                MainActivity.this,
                event.getTitle(),
                Toast.LENGTH_SHORT
        ).show();
        EventBus.getDefault().cancelEventDelivery(event);
    }
}
