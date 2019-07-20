package ouyj.hyena.com.cursorsample;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ouyj.hyena.com.cursorsample.model.ContactAdapter;
import ouyj.hyena.com.cursorsample.model.Contact;

public class ReadContactActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Contact> contactList = new ArrayList<>();
    private ContactAdapter adapter;


    private static String[] PERMISSION_CONTACT = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
    };
    private static final int REQUEST_CONTACT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_contact);

        //查找回收视图的引用
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //当确定列表项的删改不会影响到RecyclerView宽高的时候可以设置为true
        recyclerView.setHasFixedSize(true);
        //设置回收视图的适配器
        adapter = new ContactAdapter(contactList);
        recyclerView.setAdapter(adapter);

        //执行有关的权限检查
        requestContactsPermissions();
    }
    public void requestContactsPermissions(){

        //判断通讯录读和写权限是否已授权
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            //在申请权限时如果用户上次选择过拒绝，则再次申请时需显示并解释要申请的权限
            //若系统返回true（意思是要给用户一个解释，为什么需要此权限）
            //但很多手机对原生系统做了修改（比如小米4的6.0它会一直返回false）
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(
                            this, Manifest.permission.WRITE_CONTACTS)){

                //从屏幕底部弹出消息（显示你的解释）
                //make（父视图，显示内容，显示时间）
                Snackbar.make(recyclerView, "permission Contact", Snackbar.LENGTH_INDEFINITE)
                        //按钮文本和事件侦听
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(
                                        ReadContactActivity.this,
                                        PERMISSION_CONTACT,
                                        REQUEST_CONTACT
                                );
                            }
                        })
                        //消息显示和隐藏后会触发的回调事件
                        .addCallback(new Snackbar.Callback(){
                            @Override
                            public void onShown(Snackbar sb) {
                                super.onShown(sb);
                            }
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                            }
                        })
                        //显示
                        .show();
            }
            else {
                //弹出一个系统对话框来询问用户是否开启这个权限
                ActivityCompat.requestPermissions(
                        ReadContactActivity.this,
                        PERMISSION_CONTACT,
                        REQUEST_CONTACT
                );
            }
        }
        else {
            //如果已有权限则直接读取数据
            getContactInfo();
        }
    }
    private void getContactInfo(){

        //联系人的uri
        Uri uri = ContactsContract.Contacts.CONTENT_URI;


        //数据表字段
        String ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PHONE_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String PHONE_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        //查询出通讯录内所有联系人（按显示名称排序）
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(uri,null,null,null,DISPLAY_NAME);
        
        //游标内有数据（遍历所有联系人）
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){

                //得到指定联系人的信息
                String CONTACT_ID = cursor.getString(cursor.getColumnIndex(ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(HAS_PHONE_NUMBER));

                //将无电话号码的联系人排除掉
                Contact model = new Contact();
                if (hasPhoneNumber > 0){
                    model.setName(name);

                    //查询指定联系人的电话列表
                    Cursor phoneCursor = contentResolver.query(
                            PHONE_URI, 
                            new String[]{NUMBER},
                            PHONE_ID+" = ?",
                            new String[]{CONTACT_ID},
                            null
                    );


                    List<String> phoneList = new ArrayList<>();
                    //遍历游标
                    phoneCursor.moveToFirst();
                    while (!phoneCursor.isAfterLast()){
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER))
                                .replace(" ","");
                        phoneList.add(phoneNumber);
                        phoneCursor.moveToNext();
                    }
                    //设置联系人的电话列表
                    model.setNumber(phoneList);
                    contactList.add(model);
                    //关闭游标
                    phoneCursor.close();
                }
            }
            //通知数据已改变
            adapter.notifyDataSetChanged();
        }
    }
    
    /**
     * 权限请求结果的回调（派生自v4.app.FragmentActivity）
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int result : grantResults){
            if (result == PackageManager.PERMISSION_GRANTED){
                //去读取数据
                getContactInfo();
            }
        }
    }
}
