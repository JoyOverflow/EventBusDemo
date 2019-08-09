package ouyj.hyena.com.searchviewsample;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;

import ouyj.hyena.com.searchviewlayout.SearchViewLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //悬浮按钮
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(
                        view,
                        "Replace with your own action",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });

        //设置搜索视图的片段
        final SearchViewLayout searchViewLayout = findViewById(R.id.search_view_container);
//        searchViewLayout.setExpandedContentSupportFragment(this, new SearchStaticListSupportFragment());
//        searchViewLayout.handleToolbarAnimation(toolbar);
//        searchViewLayout.setCollapsedHint("Collapsed Hint");
//        searchViewLayout.setExpandedHint("Expanded Hint");
//        searchViewLayout.setHint("Global Hint");

//        searchViewLayout.setExpandedContentSupportFragment(this, new SearchStaticRecyclerFragment());
//        searchViewLayout.handleToolbarAnimation(toolbar);
//        searchViewLayout.setCollapsedHint("Collapsed Hint");
//        searchViewLayout.setExpandedHint("Expanded Hint");


        searchViewLayout.setExpandedContentSupportFragment(this, new SearchStaticScrollFragment());
        searchViewLayout.handleToolbarAnimation(toolbar);
        searchViewLayout.setCollapsedHint("Collapsed Hint");
        searchViewLayout.setExpandedHint("Expanded Hint");



        ColorDrawable collapsed = new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary));
        ColorDrawable expanded = new ColorDrawable(ContextCompat.getColor(this, R.color.default_color_expanded));
        searchViewLayout.setTransitionDrawables(collapsed, expanded);
        searchViewLayout.setSearchListener(new SearchViewLayout.SearchListener() {
            @Override
            public void onFinished(String searchKeyword) {
                searchViewLayout.collapse();
                Snackbar.make(searchViewLayout, "Start Search for - " + searchKeyword, Snackbar.LENGTH_LONG).show();
            }
        });
        searchViewLayout.setOnToggleAnimationListener(new SearchViewLayout.OnToggleAnimationListener() {
            @Override
            public void onStart(boolean expanding) {
                if (expanding) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }

            @Override
            public void onFinish(boolean expanded) { }
        });
        searchViewLayout.setSearchBoxListener(new SearchViewLayout.SearchBoxListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: " + s + "," + start + "," + count + "," + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + s + "," + start + "," + before + "," + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: " + s);
            }
        });




    }
}
