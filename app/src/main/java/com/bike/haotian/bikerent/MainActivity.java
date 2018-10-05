package com.bike.haotian.bikerent;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import cn.bmob.v3.BmobUser;
import customview.CircleMenuLayout;
import utils.MyUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //声明相关变量
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;
    private TextView text_myrealname;
    private TextView text_mymobilephonenumber;
    private TextView text_mystudentId;
    private Button btn_myinfo;
    private Button btn_logout;
    private ArrayAdapter arrayAdapter;
    private ImageView ivRunningMan;
    private CircleMenuLayout  c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews(); //获取控件
        setToolbar();
        setCircleMenu();
        setSlideMenuData();
        btn_logout.setOnClickListener(this);
        btn_myinfo.setOnClickListener(this);
    }

    private void setSlideMenuData() {
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        if (user != null){
             text_mystudentId.setText(user.getStudentId());
             text_mymobilephonenumber.setText(user.getMobilePhoneNumber());
             text_myrealname.setText(user.getRealname());

        }
    }

    private void setCircleMenu() {
        int[] ids = {R.drawable.bike,R.drawable.rent1,R.drawable.contact,R.drawable.main};
        String[] strings = {"我要出行","有车要租","联系我们","个人页面"};
        c.setMenuItemIconsAndTexts(ids,strings);


    }

    private void setToolbar() {
        /**
         * toolbar  &   drawerlayout
         */
        toolbar.setTitle("医大租车平台");//设置Toolbar标题
        toolbar.setBackgroundColor(Color.parseColor("#909acd32"));
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void findViews() {

        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        c = (CircleMenuLayout) findViewById(R.id.my);
        text_mymobilephonenumber = (TextView) findViewById(R.id.text_mymobilephonenumber);
        text_myrealname = (TextView) findViewById(R.id.text_myrealname);
        text_mystudentId = (TextView) findViewById(R.id.text_mystudentId);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_myinfo = (Button) findViewById(R.id.btn_myinfo);
    }

    @Override
    public void onClick(View v) {
          switch (v.getId()){
              case R.id.btn_myinfo:
                  Intent intent1 = new Intent(MainActivity.this,MyInfoActivity.class);
                  startActivity(intent1);
                  break;

              case R.id.btn_logout:
                  BmobUser.logOut();
                  Intent intent2 = new Intent(MainActivity.this,LoginActivity.class);
                  startActivity(intent2);
                  finish();

          }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (BmobUser.getCurrentUser() == null){
              finish();
        }
    }
}
