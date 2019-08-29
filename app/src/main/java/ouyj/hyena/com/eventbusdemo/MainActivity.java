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
 * 1.首先在包含订阅者的活动里注册EventBus和解除注册
 * 2.定义订阅者并指定线程模型
 *   Posting (默认) 表示发布者和订阅者（事件处理）在同一线程
 *   Main 表示事件处理在主(UI)线程（因此不能进行耗时操作）
 *   Background 如果发布者在主(UI)线程那么订阅者（事件处理）会开启后台线程，如果发布者已在后台线程那么事件处理使用同线程
 *   Async 订阅者（事件处理）始终新建一个线程来运行（不能进行UI操作）
 * 3.自定义事件实体类（用来传输）
 * 4.在其它活动中（无需注册）发送事件（传递自定义实体类）
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

        //注册EventBus
        //静态getDefault()方法返回一个EventBus单例对象
        //会去获取当前对象里所有的被@Subscribe注解的方法，并得到一个集合
        EventBus.getDefault().register(this);
    }
    /**
     * 解除EventBus
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //取消EventBus的注册
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
    /**
     * 声明订阅者来处理事件（必需加上订阅注解）
     * 线程模式为运行在主（UI）线程，因此不能进行耗时操作
     * 当同事件有多个订阅者时，优先级（默认=0）最高的最先获得消息
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
