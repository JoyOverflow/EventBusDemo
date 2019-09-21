package ouyj.hyena.com.coordinatorexample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener{

    private static final String GITHUB_REPO_URL = "https://github.com/saulmm/CoordinatorExamples";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置工具栏的菜单栏和点击处理事件
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                //调用系统的浏栏器打开Url
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_REPO_URL));
                startActivity(browser);
                return true;
            }
        });
        findViewById(R.id.main_txtSimple).setOnClickListener(this);
        findViewById(R.id.main_txtExample).setOnClickListener(this);
        findViewById(R.id.main_txtMaterial).setOnClickListener(this);
        findViewById(R.id.main_txtFlexible).setOnClickListener(this);
        findViewById(R.id.main_txtBehavior).setOnClickListener(this);
    }
    /**
     * 按钮事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_txtSimple:
                SimpleActivity.start(this);
                break;
            case R.id.main_txtExample:
                IOExampleActivity.start(this);
                break;
            case R.id.main_txtMaterial:
                MaterialActivity.start(this);
                break;
            case R.id.main_txtFlexible:
                FlexibleActivity.start(this);
                break;
            case R.id.main_txtBehavior:
                //实现侧滑删除
                BehaviorActivity.start(this);
                break;
        }
    }
}
