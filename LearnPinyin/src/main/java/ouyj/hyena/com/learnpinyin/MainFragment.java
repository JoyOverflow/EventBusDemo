package ouyj.hyena.com.learnpinyin;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ouyj.hyena.com.learnpinyin.data.AlphabetAdapter;
import ouyj.hyena.com.learnpinyin.data.CharacterInfo;
import ouyj.hyena.com.learnpinyin.data.PinYinContract;

import static ouyj.hyena.com.learnpinyin.MainActivity.TAG;

/**
 * 主片段类（实现LoaderCallbacks<Cursor>接口）
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private View rootView;
    private GridView gridView;

    //字母表数组
    private String[] alphabetList;
    private String alphabets = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,ü,w,x,y,z,¯,ˊ,ˇ,ˋ";

    //适配器对象
    private AlphabetAdapter adapter;
    //当前的汉字（汉字串的索引）
    private int index = 0;
    //汉字上方的进度栏
    private LinearLayout btnProgress;

    private TextView character,pinyin;
    private ImageView imgDelete,btnClose,btnPinYin,btnRead;


    static MainFragment mFragment;

    String [][]tone = {{"¯a","ā"},{"ˊa","á"},{"ˇa","ǎ"},{"ˋa","à"},
            {"¯o","ō"},{"ˊo","ó"},{"ˇo","ǒ"},{"ˋo","ò"},
            {"¯e","ē"},{"ˊe","é"},{"ˇe","ě"},{"ˋe","è"},
            {"¯i","ī"},{"ˊi","í"},{"ˇi","ǐ"},{"ˋi","ì"},
            {"¯u","ū"},{"ˊu","ú"},{"ˇu","ǔ"},{"ˋu","ù"},
            {"¯ü","ǖ"},{"ˊü","ǘ"},{"ˇü","ǚ"},{"ˋü","ǜ"},
    };


    static Typeface tf;
    final MediaPlayer mp;
    //String CONSTANTS_RES_PREFIX = "android.resource://ouyj.hyena.com.learnpinyin/";
    static final int PINYIN_LEARNING_LOADER = 0;

    //存放一组汉字
    CharacterInfo[] characters;

    /**
     * 构造方法
     */
    public MainFragment() {
        alphabetList = alphabets.split(",");
        mp  = new MediaPlayer();
        mFragment = this;
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
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        btnClose = rootView.findViewById(R.id.btnClose);
        btnPinYin= rootView.findViewById(R.id.btnPinYin);
        btnRead= rootView.findViewById(R.id.btnRead);

        btnProgress = rootView.findViewById(R.id.btnProgress);
        character = rootView.findViewById(R.id.txtCharacter);
        pinyin = rootView.findViewById(R.id.txtPinYin);
        imgDelete = rootView.findViewById(R.id.imgDelete);

        //设置汉卡字体
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/simkai.ttf");
        character.setTypeface(tf);


        //创建适配器
        adapter = new AlphabetAdapter(getContext(),alphabetList);

        //网格视图（设置项点击事件）
        gridView =  rootView.findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //目前已输入的拼音文本
                String currentPinyin = pinyin.getText().toString();
                //当前被点击的字母
                String content = alphabetList[position];

                String tmp=String.format("目前已输入的拼音文本：%s；当前被点击的字母：%s",
                        currentPinyin,content);
                Log.d(TAG, tmp);


                //当ü遇到j,q,x,y时要去掉两点
                if (content.equals("ü") &&
                        (currentPinyin.contains("j") ||
                                currentPinyin.contains("q") ||
                                currentPinyin.contains("x") ||
                                currentPinyin.contains("y"))) {
                    content = "u";
                }


                //输入音标后不能再输入其他拼音（只能删除字母）
                if (position < 26){
                    //当输入a-z间的字母时
                    currentPinyin = currentPinyin + content;
                } else {
                    //当输入四声音标（转换为标准四声音标显示）
                    currentPinyin = convertToPinyinWithTone(currentPinyin, content);
                }
                //更新已输入的拼音文本
                pinyin.setText(currentPinyin);

                //输入至少一个字符后才显示删除图像
                if (currentPinyin.length() == 1) {
                    imgDelete.setVisibility(View.VISIBLE);
                }



                //取出该汉字的拼音并判断
                if (currentPinyin.equals(characters[index].pinyin)) {

                    //加载弹出片段
                    CardFragment fragment = new CardFragment();

                    //附加数据
                    Bundle bundle = new Bundle();
                    bundle.putString("character", characters[index].character);
                    bundle.putString("pinyin", characters[index].pinyin);
                    fragment.setArguments(bundle);

                    fragment.show(getFragmentManager(), "CardFragment");

                    //并播放该汉字的声音（位于Assets目录）
                    try {
                        mp.reset();
                        AssetFileDescriptor file = getActivity().getAssets().openFd(
                                "characters/" + characters[index].sounds + ".mp3"
                        );
                        mp.setDataSource(
                                file.getFileDescriptor(),
                                file.getStartOffset(),
                                file.getLength()
                        );
                        file.close();
                        mp.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    //更新进度栏
                    ImageView progressGrid = rootView.findViewWithTag(index);
                    progressGrid.setImageResource(R.drawable.grid_green);

                    /*update character learned status*/
                    characters[index].done = PinYinContract.YES;
                    ContentValues value = new ContentValues();
                    value.put(PinYinContract.Character.COLUMN_DONE, characters[index].done);
                    getActivity().getContentResolver().update(
                            PinYinContract.Character.buildCharacterUriById(characters
                                    [index].id),
                            value, null, null);

                    //go to next pinyin card
                    index++;
                    if (index < characters.length) {
                        //change to next pinyin
                        pinyin.setText("");
                        character.setText(characters[index].character);
                        imgDelete.setVisibility(View.GONE);
                    } else {
                        pinyin.setText("");
                        imgDelete.setVisibility(View.GONE);
                        new ConfirmFragment().show(getFragmentManager(), "ConfirmFragment");

                    }
                }





            }
        });
        //开始播放音频
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });



        //字母删除按钮
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置控件点击有效
                gridView.setClickable(true);

                //删除一个字母
                String currentPinyin = pinyin.getText().toString();
                pinyin.setText(currentPinyin.substring(0, currentPinyin.length() - 1));

                //图像按钮的可见性
                if (pinyin.getText().toString().length() == 0) {
                    imgDelete.setVisibility(View.GONE);
                }
            }
        });
        //关闭按钮
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        btnPinYin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AlphabetActivity.class);
                startActivity(intent);
            }
        });
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharacterInfo c=characters[index];
                readCharacter(c.sounds);
            }
        });
        return rootView;
    }

    /**
     * 弹出汉字卡片并发声
     * @param sound
     */
    private void readCharacter(String sound){
        //并播放该汉字的声音（位于Assets目录）
        try {
            mp.reset();
            AssetFileDescriptor file = getActivity().getAssets().openFd(
                    "characters/" + sound + ".mp3"
            );
            mp.setDataSource(
                    file.getFileDescriptor(),
                    file.getStartOffset(),
                    file.getLength()
            );
            file.close();
            mp.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onResume()
    {
        super.onResume();

        //取出意图内数据
        if (getActivity().getIntent().hasExtra(PinYinContract.Character.COLUMN_DONE)) {
            //仍针对原先的id加载器，清除老数据重新开始
            getLoaderManager().restartLoader(
                    PINYIN_LEARNING_LOADER,
                    null,
                    this
            );
            getActivity().getIntent().removeExtra(PinYinContract.Character.COLUMN_DONE);
        }
    }
    @Override
    public void onDestroy() {
        mp.release();
        super.onDestroy();
    }
    /**
     * 父活动已创建完成后的回调
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //创建加载器管理对象（触发onCreateLoader执行）
        getLoaderManager().initLoader(
                PINYIN_LEARNING_LOADER,
                null,
                this
        );
    }






    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = PinYinContract.Character.COLUMN_ID + " Asc Limit 10";
        Uri uri = PinYinContract.Character.buildCharacterUriByDone(PinYinContract.NO);

        Log.d(TAG, "onCreateLoader！");
        return new CursorLoader(getActivity(),
                uri,
                null,
                null,
                null,
                sortOrder
        );
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if ((data == null)||(data.getCount() == 0)) {
            Log.d(TAG, "onLoadFinished！==0");
            return;
        }
        Log.d(TAG, "onLoadFinished！>0");

        //查找出数据记录数
        int cursorCount = data.getCount();
        data.moveToFirst();

        //创建指定大小的数组
        characters = new CharacterInfo[cursorCount];
        for (int i = 0;i < characters.length;i++)
        {
            //实例化数组元素
            characters[i] = new CharacterInfo();
            generateCharactersInfo(characters[i],data);
            data.moveToNext();
        }

        //设置当前的汉字
        index = 0;
        character.setText(characters[index].character);

        //移除所有子视图
        btnProgress.removeAllViews();
        for(int i=0;i< characters.length;i++) {
            //加入白方块
            ImageView img = new ImageView(getActivity());
            img.setImageResource(R.drawable.grid_white);
            img.setTag(i);
            img.setPadding(2, 2, 2, 2);
            //加入子视图
            btnProgress.addView(img);
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }


    /**
     * 根据游标产生汉字信息
     * @param info
     * @param cursor
     */
    void generateCharactersInfo(CharacterInfo info,Cursor cursor)
    {
        info.character = cursor.getString(cursor.getColumnIndex(PinYinContract.Character.COLUMN_NAME));
        info.id = cursor.getInt(cursor.getColumnIndex(PinYinContract.Character.COLUMN_ID));
        info.done = cursor.getString(cursor.getColumnIndex(PinYinContract.Character.COLUMN_DONE));
        info.sounds =  cursor.getString(cursor.getColumnIndex(PinYinContract.Character.COLUMN_PRONUNCIATION));
        info.pinyin = convertToPinyinWithTone(
                info.sounds.substring(0, info.sounds.length() - 1),
                info.sounds.substring(info.sounds.length() - 1, info.sounds.length())
        );
    }



    
    
    
    




    String findToneAlphabet(String currentPinyin)
    {
        String toneAlphabet="";
        String allFinals = "";
        final String format = "[aoeiuü]";//所有韵母字母
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(currentPinyin);
        while (matcher.find())/*查找并获取所有韵母*/
        {
            //Log.e(LOG_TAG,"matcher.group = " + matcher.group());
            allFinals = allFinals + matcher.group();
        }
        //Log.e(LOG_TAG, "allFinals = " + allFinals + ",length = " + allFinals.length());
        if(allFinals.length() == 1)
        {
            toneAlphabet = allFinals;
        }
        else if (allFinals.length() == 2)
        {
            if (allFinals.contains("ui") ||allFinals.contains("iu"))
            {
                toneAlphabet = allFinals.substring(1,2);
            }
            else {
                if (allFinals.contains("a")) toneAlphabet = "a";
                else if (allFinals.contains("o")) toneAlphabet = "o";
                else if (allFinals.contains("e")) toneAlphabet = "e";
                else if (allFinals.contains("i")) toneAlphabet = "i";
                else if (allFinals.contains("u")) toneAlphabet = "u";
                else if (allFinals.contains("ü")) toneAlphabet = "ü";
            }
        }
        else
        {
            if (allFinals.contains("a")) toneAlphabet = "a";
            else if (allFinals.contains("o")) toneAlphabet = "o";
            else if (allFinals.contains("e")) toneAlphabet = "e";
            else if (allFinals.contains("i")) toneAlphabet = "i";
            else if (allFinals.contains("u")) toneAlphabet = "u";
            else if (allFinals.contains("ü")) toneAlphabet = "ü";
        }
        //Log.e(LOG_TAG, "toneAlphabet = " + toneAlphabet);
        return toneAlphabet;
    }



    private String convertToPinyinWithTone(String dbSound,String dbTone)
    {
        String pinyin = "";
        String toneAlphabet = findToneAlphabet(dbSound);
        switch (dbTone){
            case "1":
                dbTone = "¯";
                break;
            case "2":
                dbTone = "ˊ";
                break;
            case "3":
                dbTone = "ˇ";
                break;
            case "4":
                dbTone = "ˋ";
                break;
            case "0":/*0 轻声 没有音标*/
                dbTone = "";
            default:/*0 轻声 没有音标*/
                break;
        }
        //Log.e(LOG_TAG, "dbTone = " + dbTone);
        if (dbTone.equals("")) return dbSound;

        String flag = dbTone + toneAlphabet;
        for (int i = 0; i < tone.length; i++) {
            if (flag.equals(tone[i][0])) {
                pinyin = dbSound.replaceFirst(tone[i][0].substring(1), tone[i][1]);
                break;
            }
        }
        // Log.e(LOG_TAG, "pinyin with tone = " + pinyin);
        return pinyin;
    }





    public static class CardFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = getActivity().getLayoutInflater().inflate(R.layout.dialog, null);
            TextView character = (TextView)view.findViewById(R.id.character);
            TextView pinyin = (TextView)view.findViewById(R.id.pinyin);
            character.setText(getArguments().getString("character"));
            pinyin.setText(getArguments().getString("pinyin"));
            character.setTypeface(tf);
            builder.setView(view);
            return builder.create();
        }
    }

    /**
     * 弹出"返回或再来"对话框
     */
    public static class ConfirmFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setPositiveButton("再来一次", new DialogInterface.OnClickListener() {
                //重新生成一组拼音练习
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getLoaderManager().restartLoader(
                            PINYIN_LEARNING_LOADER,
                            null,
                            mFragment
                    );
                }
            });
            builder.setNegativeButton("马上返回", new DialogInterface.OnClickListener() {
                //返回到拼音界面
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /*返回学习拼音界面*/
                    getActivity().finish();
                }
            });
            return builder.create();
        }
    }


}
