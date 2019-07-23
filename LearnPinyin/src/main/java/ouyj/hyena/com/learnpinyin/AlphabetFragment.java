package ouyj.hyena.com.learnpinyin;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.IOException;

import ouyj.hyena.com.learnpinyin.data.AlphabetAdapter;
import ouyj.hyena.com.learnpinyin.data.PinYinContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlphabetFragment extends Fragment {

    View rootView;
    ImageView imgShengMu,imgYunMu,imgZheng,goView,settingsView;

    private final String TAG = this.getClass().getSimpleName();

    String CONSTANTS_RES_PREFIX = "android.resource://ouyj.hyena.com.learnpinyin/";
    Button clearExciseRecord;

    GridView gridView;
    final MediaPlayer mediaPlayer;
    PopupWindow popupWin;

    //自定义适配器
    AlphabetAdapter shengMuAdapter,yunMuAdapter,zhengAdapter;
    String[] shengMuList,yunMuList,zhengList;
    String shengMu = "b,p,m,f,d,t,n,l,g,k,h,j,q,x,r,z,c,s,y,w,zh,ch,sh";
    String yunMu = "a,o,e,i,u,ü,ai,ei,ui,ao,ou,iu,ie,üe,er,an,en,un,in,ün,ang,eng,ing,ong";
    String zheng="zhi,chi,shi,ri,zi,ci,si,yi,wu,yu,ye,yue,yuan,yin,yun,ying";

    /**
     * 构造方法
     */
    public AlphabetFragment() {
        shengMuList = shengMu.split(",");
        yunMuList = yunMu.split(",");
        zhengList = zheng.split(",");
        mediaPlayer = new MediaPlayer();
    }
    /**
     * 片段创建视图
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //获取到根视图
        rootView = inflater.inflate(R.layout.fragment_alphabet, container, false);

        //查找视图引用
        settingsView = rootView.findViewById(R.id.imgSetting);
        goView = rootView.findViewById(R.id.imgGo);

        //声韵母和整体音节切换按钮
        imgShengMu = rootView.findViewById(R.id.imgShengMu);
        imgZheng = rootView.findViewById(R.id.imgZheng);
        imgYunMu = rootView.findViewById(R.id.imgYunMu);
        //创建适配器类（派生自BaseAdapter）
        shengMuAdapter = new AlphabetAdapter(getActivity(),shengMuList);
        yunMuAdapter = new AlphabetAdapter(getActivity(),yunMuList);
        zhengAdapter = new AlphabetAdapter(getActivity(),zhengList);

        //为格子视图设置适配器
        gridView = rootView.findViewById(R.id.alphabetView);
        gridView.setAdapter(shengMuAdapter);


        //要弹出的窗体布局
        View popupView = getActivity().getLayoutInflater().inflate(R.layout.popup_window_menu, null);
        popupWin = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                true
        );
        popupWin.setTouchable(true);
        popupWin.setOutsideTouchable(true);
        popupWin.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWin.getContentView().setFocusableInTouchMode(true);
        popupWin.getContentView().setFocusable(true);
        popupWin.setAnimationStyle(R.style.anim_menu_bottombar_bottom);



        //清除练习记录的按钮事件
        clearExciseRecord = popupView.findViewById(R.id.clear_excise_record);
        clearExciseRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更改字段值
                String newStatus = PinYinContract.NO;
                ContentValues value = new ContentValues();
                value.put(PinYinContract.Character.COLUMN_DONE, newStatus);

                //更改数据表记录
                //content://ouyj.hyena.com.learn/Characters
                //Log.d(TAG, PinYinContract.Character.CONTENT_URI.toString());
                ContentResolver resolver = getActivity().getContentResolver();
                resolver.update(
                        PinYinContract.Character.CONTENT_URI,
                        value,
                        null,
                        null
                );
            }
        });


        //设置网格视图的项点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //得到音频的文件名称
                TextView name = (TextView) view;
                String alpha = name.getText().toString().replace("ü", "v");
                try {
                    //恢复到空闲状态
                    mediaPlayer.reset();

                    //加载Raw目录下的资源并播放
                    int alphaId = getActivity().getResources().getIdentifier(
                            alpha,
                            "raw",
                            "ouyj.hyena.com.learnpinyin"
                    );
                    String uriString = CONSTANTS_RES_PREFIX + Integer.toString(alphaId);
                    mediaPlayer.setDataSource(getActivity(), Uri.parse(uriString));
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //音频资源已缓存到内存
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });




        //点击后切换数据源和图像
        imgShengMu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换数据源
                gridView.setAdapter(shengMuAdapter);
                //更改图像资源
                imgShengMu.setImageResource(R.drawable.shengmu_2);
                imgYunMu.setImageResource(R.drawable.yunmu_1);
                imgZheng.setImageResource(R.drawable.zheng_1);
            }
        });
        imgYunMu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换数据源
                gridView.setAdapter(yunMuAdapter);
                //更改图像资源
                imgShengMu.setImageResource(R.drawable.shengmu_1);
                imgYunMu.setImageResource(R.drawable.yunmu_2);
                imgZheng.setImageResource(R.drawable.zheng_1);
            }
        });
        imgZheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView.setAdapter(zhengAdapter);
                //更改图像资源
                imgShengMu.setImageResource(R.drawable.shengmu_1);
                imgYunMu.setImageResource(R.drawable.yunmu_1);
                imgZheng.setImageResource(R.drawable.zheng_2);
            }
        });


        //定向到指定活动
        goView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(PinYinContract.Character.COLUMN_DONE, "");
                startActivity(intent);
            }
        });
        //弹窗（设定在指定视图的下方）
        settingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWin.showAsDropDown(goView);
            }
        });
        //返回视图
        return rootView;
    }


    /**
     * 片段撤销
     */
    @Override
    public void onDestroy() {
        mediaPlayer.release();
        Log.v(TAG, "onDestroy");
        super.onDestroy();
    }
}







