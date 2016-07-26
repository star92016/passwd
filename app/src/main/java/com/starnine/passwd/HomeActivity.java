package com.starnine.passwd;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.starnine.passwd.data.PasswdItem;
import com.starnine.passwd.data.TypeItem;
import com.starnine.passwd.utils.MySQLiteOpenHelper;
import com.starnine.passwd.utils.User;

import java.util.Vector;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(!User.IsLogin()){
            toast("未登录");
            finish();
            startActivity(new Intent(this,LoginActivity.class));
            return;
        }

        mySQLiteOpenHelper=new MySQLiteOpenHelper(this);
        initView();
    }
    private ExpandableListAdapter adapter;
    MySQLiteOpenHelper mySQLiteOpenHelper;
    private void initView() {
        initData();
        elv_list=(ExpandableListView)findViewById(R.id.elv_list);
        adapter=new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return pgvector.size();
            }

            @Override
            public int getChildrenCount(int i) {
                return pgvector.get(i).vector.size();
            }

            @Override
            public Object getGroup(int i) {
                return pgvector.get(i);
            }

            @Override
            public Object getChild(int i, int i1) {
                return pgvector.get(i).vector.get(i1);
            }

            @Override
            public long getGroupId(int i) {
                return i;
            }

            @Override
            public long getChildId(int i, int i1) {
                return i*i1;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
                TextView tv=new TextView(HomeActivity.this);
                tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setText(pgvector.get(i).typeItem.getName());
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(20);
                tv.setPadding(60,20,0,20);
                return tv;
            }

            @Override
            public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
                TextView tv=new TextView(HomeActivity.this);
                tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setText(pgvector.get(i).vector.get(i1).getUsername());
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(20);
                tv.setPadding(50,20,0,20);
                return tv;
            }

            @Override
            public boolean isChildSelectable(int i, int i1) {
                return true;
            }
        };
        elv_list.setAdapter(adapter);
        elv_list.setOnCreateContextMenuListener(this);
    }
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final EditText et_info=new EditText(this);
        et_info.setHint("输入密码验证身份");
        dialog.setTitle("剩余30秒");
        dialog.setView(et_info);


        final AlertDialog dialog2 = dialog.create();
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        if(Integer.parseInt(msg.obj.toString())<=0){
                            dialog2.dismiss();
                            return;
                        }
                        dialog2.setTitle("剩余"+msg.obj.toString()+"秒");
                        break;
                    case 2:
                        //ok
                        onContextItemSelected2(item);
                        break;
                }
            }
        };
        dialog2.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String s=et_info.getText().toString().trim();
                if(User.CheckUser(HomeActivity.this,s)){
                    handler.sendEmptyMessage(2);
                }else
                    toast("验证失败");
            }
        });


        dialog2.show();
        new Thread(){
            public void run(){
                int i=30;
                while(i>0){
                    i--;
                    Message msg=new Message();
                    msg.what=1;
                    msg.obj=i+"";
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        return true;
    }

    public boolean onContextItemSelected2(MenuItem item) {


        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();

        final int a=
        ExpandableListView.getPackedPositionGroup(info.packedPosition),b
                =ExpandableListView.getPackedPositionChild(info.packedPosition);
        if(b<0){
            switch (item.getItemId()){
                case 2:
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    final EditText et_info=new EditText(this);
                    et_info.setText(pgvector.get(a).typeItem.getName());
                    dialog.setTitle("请输入");
                    dialog.setView(et_info);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String s=et_info.getText().toString().trim();
                            pgvector.get(a).typeItem.setName(s);
                            mySQLiteOpenHelper.upateType(pgvector.get(a).typeItem);
                            updateElv_list();
                        }
                    });
                    dialog.show();
                }
                    break;
                case 3:
                    if(pgvector.get(a).vector.size()>0){
                        toast("不为空");
                    }else{
                        mySQLiteOpenHelper.delType(pgvector.get(a).typeItem);
                        updateElv_list();

                    }
                    break;
            }
        }else{
            switch (item.getItemId()){
                case 1:
                    toast(pgvector.get(a).vector.get(b).getPassword());
                    break;
                case 2:
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("请输入");
                    View v=LinearLayout.inflate(this,R.layout.dialog_newpasswd,null);
                    final EditText et_user=(EditText)v.findViewById(R.id.et_user);
                    final EditText et_pass=(EditText)v.findViewById(R.id.et_passwd);
                    final Spinner sp_type=(Spinner)v.findViewById(R.id.sp_type);
                    Vector<String> vec=new Vector<>();
                    for(PasswdGroup pg:pgvector){
                        vec.add(pg.typeItem.getName());
                    }

                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,vec);
                    sp_type.setAdapter(arrayAdapter);
                    final PasswdItem passwdItem=pgvector.get(a).vector.get(b);
                    et_user.setText(passwdItem.getUsername());
                    et_pass.setText(passwdItem.getPassword());
                    int pid=passwdItem.getPid();
                    for(int i=0;i<pgvector.size();i++){
                        if(pid==pgvector.get(i).typeItem.getId()){
                            sp_type.setSelection(i);
                            break;
                        }
                    }
                    dialog.setView(v);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String user=et_user.getText().toString().trim(),
                                    pass=et_pass.getText().toString().trim();
                            int type=pgvector.get(sp_type.getSelectedItemPosition()).typeItem.getId();
                            PasswdItem passwdItem=pgvector.get(a).vector.get(b);
                            passwdItem.setUsername(user);
                            passwdItem.setPassword(pass);
                            passwdItem.setPid(type);
                            mySQLiteOpenHelper.upatePasswd(passwdItem);
                            updateElv_list();
                        }
                    });
                    dialog.show();
                }
                    break;
                case 3:
                    mySQLiteOpenHelper.delPasswd(pgvector.get(a).vector.get(b));
                    updateElv_list();
                    break;
            }
        }

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        ExpandableListView.ExpandableListContextMenuInfo info=(ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
        if(ExpandableListView.getPackedPositionChild(info.packedPosition)<0){
            menu.add(1,2,2,"修改");
            menu.add(1,3,3,"删除");
        }else{
            menu.add(1,1,1,"显示密码");
            menu.add(1,2,2,"修改");
            menu.add(1,3,3,"删除");
        }

    }

    private void initData() {
        pgvector=new Vector<>();
        Vector<TypeItem> alltype=mySQLiteOpenHelper.getAllType();
        for(TypeItem type:alltype){
            PasswdGroup pg=new PasswdGroup();
            pg.typeItem=type;
            pg.vector=mySQLiteOpenHelper.getPasswdByType(type);
            pgvector.add(pg);
        }
    }
    private Vector<PasswdGroup> pgvector;
    public class PasswdGroup{
        public TypeItem typeItem;
        public Vector<PasswdItem> vector;
    }

    private ExpandableListView elv_list;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int i=1,j=1;
        menu.add(1,i++,j++,"新增根节点");
        menu.add(1,i++,j++,"新增项");
        return true;
    }
    private Toast toast;
    public void toast(String s){
        if(toast==null)
            toast=Toast.makeText(this,s,Toast.LENGTH_SHORT);
        else
            toast.setText(s);
        toast.show();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final EditText et_info=new EditText(this);
        et_info.setHint("输入密码验证身份");
        dialog.setTitle("剩余30秒");
        dialog.setView(et_info);


        final AlertDialog dialog2 = dialog.create();
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        if(Integer.parseInt(msg.obj.toString())<=0){
                            dialog2.dismiss();
                            return;
                        }
                        dialog2.setTitle("剩余"+msg.obj.toString()+"秒");
                        break;
                    case 2:
                        //ok
                        onOptionsItemSelected2(item);
                        break;
                }
            }
        };
        dialog2.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String s=et_info.getText().toString().trim();
                if(User.CheckUser(HomeActivity.this,s)){
                    handler.sendEmptyMessage(2);
                }else
                    toast("验证失败");
            }
        });


        dialog2.show();
        new Thread(){
            public void run(){
                int i=30;
                while(i>0){
                    i--;
                    Message msg=new Message();
                    msg.what=1;
                    msg.obj=i+"";
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        return true;
    }

    public boolean onOptionsItemSelected2(MenuItem item) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final EditText et_info=new EditText(this);
        switch (item.getItemId()){
            case 1:
                dialog.setTitle("输入类型名");
                dialog.setView(et_info);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String s=et_info.getText().toString().trim();
                        TypeItem typeItem =new TypeItem();
                        typeItem.setName(s);
                        mySQLiteOpenHelper.addType(typeItem);
                        updateElv_list();

                    }
                });
                dialog.show();
                break;
            case 2:
            {
                View v=LinearLayout.inflate(this,R.layout.dialog_newpasswd,null);
                final EditText et_user=(EditText)v.findViewById(R.id.et_user);
                final EditText et_pass=(EditText)v.findViewById(R.id.et_passwd);
                final Spinner sp_type=(Spinner)v.findViewById(R.id.sp_type);
                Vector<String> vec=new Vector<>();
                for(PasswdGroup pg:pgvector){
                    vec.add(pg.typeItem.getName());
                }

                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,vec);
                sp_type.setAdapter(arrayAdapter);
                dialog.setTitle("请输入");
                dialog.setView(v);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String user=et_user.getText().toString().trim(),
                                pass=et_pass.getText().toString().trim();
                        int type=pgvector.get(sp_type.getSelectedItemPosition()).typeItem.getId();
                        PasswdItem passwdItem=new PasswdItem();
                        passwdItem.setUsername(user);
                        passwdItem.setPassword(pass);
                        passwdItem.setPid(type);
                        mySQLiteOpenHelper.addPasswd(passwdItem);
                        updateElv_list();
                    }
                });
                dialog.show();
            }
                break;
        }
        return true;
    }

    private void updateElv_list(){
        initData();
        elv_list.invalidateViews();
        for(int i=0;i<pgvector.size();i++)
            elv_list.collapseGroup(i);
    }
}
