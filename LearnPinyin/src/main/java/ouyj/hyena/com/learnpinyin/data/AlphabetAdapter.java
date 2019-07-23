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

    private Context context;
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
    /**
     * 每一列表项的视图
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txtView;
        if (convertView == null) {
            //为null时加载项布局样式
            txtView = new TextView(context);
        }
        else
            txtView = (TextView) convertView;

        //设置文本视图
        txtView.setText(array[position]);
        txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        txtView.setTypeface(null, Typeface.BOLD);
        txtView.setPadding(2,2,2,2);

        switch (array[position]) {
            case "a":
            case "o":
            case "e":
            case "i":
            case "u":
            case "ü":
            case "iu":
            case "en":
                txtView.setTextColor(context.getResources().getColor(R.color.violet));
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
                txtView.setTextColor(context.getResources().getColor(R.color.deepskyblue));
                break;
            case "c":
            case "p":
            case "w":
            case "n":
            case "sh":
            case "ei":
            case "un":
                txtView.setTextColor(context.getResources().getColor(R.color.lime));
                break;
            case "d":
            case "l":
            case "x":
            case "t":
            case "ch":
            case "ui":
            case "ün":
            case "ie":
                txtView.setTextColor(context.getResources().getColor(R.color.yellow));
                break;
            case "m":
            case "s":
            case "q":
            case "f":
            case "ao":
            case "ing":
            case "ang":
                txtView.setTextColor(context.getResources().getColor(R.color.orange));
                break;
            case "g":
            case "j":
            case "k":
            case "zh":
            case "ou":
            case "eng":
            case "er":
            case "an":
                txtView.setTextColor(context.getResources().getColor(R.color.aqua));
                break;
            default:
                txtView.setTextColor(context.getResources().getColor(R.color.white));
        }
        txtView.setBackgroundResource(R.drawable.grid_item_border);
        txtView.setGravity(0x11);
        return txtView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public int getCount() {
        return array.length;
    }
}
