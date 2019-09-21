package ouyj.hyena.com.coordinatorexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

public class BehaviorActivity extends AppCompatActivity {

    public static void start(Context c) {
        c.startActivity(new Intent(c, BehaviorActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);


        //实现侧滑删除效果（Behavior是CoordinatorLayout特有的，因此根布局必需为CoordinatorLayout）
        final SwipeDismissBehavior swipe = new SwipeDismissBehavior();
        swipe.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY);
        swipe.setListener(new SwipeDismissBehavior.OnDismissListener() {
            //侧滑消失后的回调
            @Override public void onDismiss(View view) {
                Toast.makeText(
                        BehaviorActivity.this,
                        "Card swiped !!",
                        Toast.LENGTH_SHORT
                ).show();
            }
            @Override public void onDragStateChanged(int state) {}
        });

        //为卡片视图设置layout_behavior,其后便可左右滑动（首先得到布局参数）
        CardView cardView = findViewById(R.id.cardView);
        CoordinatorLayout.LayoutParams coordinatorParams =
                (CoordinatorLayout.LayoutParams) cardView.getLayoutParams();
        coordinatorParams.setBehavior(swipe);
    }
}
