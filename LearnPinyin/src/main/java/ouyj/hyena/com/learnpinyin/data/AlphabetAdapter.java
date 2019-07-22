package ouyj.hyena.com.learnpinyin.data;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ouyj.hyena.com.learnpinyin.R;

/**
 * 自定义适配器类
 */
public class AlphabetAdapter extends BaseAdapter {

    private final String TAG = this.getClass().getSimpleName();
    private Context context ;
    private String[] array;

    /**
     * 构造方法（传入上下文和数据源）
     * @param context
     * @param array
     */
    public AlphabetAdapter(Context context, String[] array) {
        super();
        this.context = context;
        this.array = array;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = new TextView(context); // 实例化ImageView的对象
            //imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // 设置缩放方式
            //textView.setPadding(0, 3, 0, 3); // 设置ImageView的内边距
        } else {
            textView = (TextView) convertView;
        }

        //设置文本视图
        textView.setText(array[position]);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        textView.setTypeface(null, Typeface.BOLD);
        switch (array[position]) {
            case "a":
            case "o":
            case "e":
            case "i":
            case "u":
            case "ü":
            case "iu":
            case "en":
                textView.setTextColor(context.getResources().getColor(R.color.violet));
                break;
            case "b":
            case "h":
            case "r":
            case "y":
            case "z":
            case "ai":
            case "in":
            case "üe":
            case "ong":
                textView.setTextColor(context.getResources().getColor(R.color.deepskyblue));
                break;
            case "c":
            case "p":
            case "w":
            case "n":
            case "sh":
            case "ei":
            case "un":
                textView.setTextColor(context.getResources().getColor(R.color.lime));
                break;
            case "d":
            case "l":
            case "x":
            case "t":
            case "ch":
            case "ui":
            case "ün":
            case "ie":
                textView.setTextColor(context.getResources().getColor(R.color.yellow));
                break;
            case "m":
            case "s":
            case "q":
            case "f":
            case "ao":
            case "ing":
            case "ang":
                textView.setTextColor(context.getResources().getColor(R.color.orange));
                break;
            case "g":
            case "j":
            case "k":
            case "zh":
            case "ou":
            case "eng":
            case "er":
            case "an":
                textView.setTextColor(context.getResources().getColor(R.color.white));
                break;
            default:
                textView.setTextColor(context.getResources().getColor(R.color.white));
        }
        textView.setBackgroundResource(R.drawable.grid_item_border);
        textView.setGravity(0x11);
        return textView;
    }




    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public int getCount() {
        return array.length;/*26字母 + 4 声调*/
    }
}
