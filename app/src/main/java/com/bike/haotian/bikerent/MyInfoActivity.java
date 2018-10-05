package com.bike.haotian.bikerent;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import utils.MyHandler;
import utils.MyUser;

/**
 * Created by HAOTIAN on 2016/8/11.
 */
public class MyInfoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView runbike;
    private ImageView mybike;
    private LinearLayout linear;
    private ImageView logo;
    private Button btn_submit;
    private Button btn_cancel;
    private Button btn_submit2;
    private Button btn_cancel2;
    private EditText edt_oldpwd;
    private EditText edt_newpwd;
    private TextView tv_time;
    private TextView text_mycurrentaddress;
    private EditText edt_updatemyaddress;
    TranslateAnimation ta;
    private Thread thread;
    private static int FLAG_START_TIMING = 0x100;
    private static int FLAG_STOP_TIMING = 0X200;
    private static int FLAG_KEEP_TIMING = 0X300;
    private static int time;
    private static  Context mContext;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FLAG_START_TIMING) {
                //修改密码成功 即将退出并注销
                time = 3;
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View view = inflater.inflate(R.layout.alert_timing,null);
                 tv_time = (TextView) view.findViewById(R.id.tiaozhuantime);
                AlertDialog dialog3 = new AlertDialog.Builder(mContext).setIcon(R.mipmap.icon).setTitle("success！")
                        .setView(view).create();
                dialog3.show();
                mHandler.sendEmptyMessage(FLAG_KEEP_TIMING);
            }
            if (msg.what == FLAG_KEEP_TIMING ){
                if (time == 0){
                    mHandler.sendEmptyMessage(FLAG_STOP_TIMING);
                    return;
                }
                tv_time.setText(time+"");
                time--;
                sendEmptyMessageDelayed(FLAG_KEEP_TIMING,1000);
            }

            if (msg.what == FLAG_STOP_TIMING){
                 BmobUser.logOut();
                 Intent intent = new Intent(mContext,LoginActivity.class);
                 startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        mContext = this;
        findViews();
        setToolbar();
        postAlpha();
        setAnimation();

    }

    private void setAnimation() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        width = width - mybike.getDrawable().getIntrinsicWidth();
        ta = new TranslateAnimation(0, width - mybike.getWidth(), 0, 0);
        ta.setStartOffset(100);
        ta.setDuration(2000);
        ta.setRepeatCount(Animation.INFINITE);//设置重复次数
        ta.setRepeatMode(Animation.REVERSE);//设置反方向执行
        mybike.setAnimation(ta);
    }

    private void postAlpha() {


        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    mHandler.sendEmptyMessage(0x123);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        //   thread.run();

    }

    private void setToolbar() {
        toolbar.setTitle("个人页面");//设置Toolbar标题
        toolbar.setBackgroundColor(Color.parseColor("#909acd32"));
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);


        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        runbike = (ImageView) findViewById(R.id.runbike);
        linear = (LinearLayout) findViewById(R.id.runbikelinear);
        linear.setAlpha(0.8f);
        runbike.setTranslationX(606);
        runbike.setAlpha(0.5f);
        mybike = (ImageView) findViewById(R.id.mybike);
        logo = (ImageView) findViewById(R.id.imageview_logo);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ta.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ta.cancel();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        runbike.setImageBitmap(null);
        mybike.setImageBitmap(null);
        linear.setBackground(null);
        logo.setImageBitmap(null);
        mybike.clearAnimation();
    }

    public void checkForUpdate(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("已是最新版本").setTitle("检查更新").setIcon(R.mipmap.icon);
        builder.show();

    }

    public void logoutMyAccount(View v) {
        BmobUser.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void myinfo2cart(View v) {
        Intent intent = new Intent(this, ShoppingCartActivity.class);
        startActivity(intent);
    }


    public void updateMyPassword(View v) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.edit_mypassword, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).setIcon(R.mipmap.icon).setTitle("修改密码").setView(view).create();
        dialog.setCancelable(false);
        edt_newpwd = (EditText) view.findViewById(R.id.newpassword);
        edt_oldpwd = (EditText) view.findViewById(R.id.oldpassword);
        btn_submit2 = (Button) view.findViewById(R.id.btn_submitmypassword);
        btn_cancel2 = (Button) view.findViewById(R.id.btn_canceledit2);

        btn_submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpwd = edt_oldpwd.getText().toString().trim();
                String newpwd = edt_newpwd.getText().toString().trim();
                if (oldpwd.equals("")) {
                    Toast.makeText(MyInfoActivity.this, "原密码不为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (newpwd.equals("")) {
                    Toast.makeText(MyInfoActivity.this, "新密码不为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //修改密码
                BmobUser.updateCurrentUserPassword(oldpwd, newpwd, new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if (e == null) {

                            dialog.dismiss();
                            mHandler.sendEmptyMessage(FLAG_START_TIMING);
                        } else {
                            Toast.makeText(MyInfoActivity.this, "失败了" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });


            }
        });

         btn_cancel2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                     dialog.dismiss();
             }
         });

        dialog.show();
    }


    public void editMyAddress(View v) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.edit_myaddress, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final AlertDialog dialog = builder.setIcon(R.mipmap.icon).setTitle("编辑我的地址").setView(view).create();
        dialog.setCancelable(false);
        text_mycurrentaddress = (TextView) view.findViewById(R.id.text_mycurrentaddress);
        edt_updatemyaddress = (EditText) view.findViewById(R.id.edt_updatemyaddress);
        btn_submit = (Button) view.findViewById(R.id.btn_submitmyaddress);
        btn_cancel = (Button) view.findViewById(R.id.btn_canceledit);
        text_mycurrentaddress.setText(BmobUser.getCurrentUser(MyUser.class).getAddress());
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = edt_updatemyaddress.getText().toString().trim();
                if (address.equals("")) {
                    Toast.makeText(MyInfoActivity.this, "地址不应该为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                MyUser user = new MyUser();
                user.setAddress(address);
                user.update(BmobUser.getCurrentUser().getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(MyInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(MyInfoActivity.this, "出错啦!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showMyInfo(View v) {
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        String phonenumber = user.getMobilePhoneNumber();
        String address = user.getAddress();
        String realname = user.getRealname();
        String studentId = user.getStudentId();
        String createDate = user.getCreatedAt();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.icon).setTitle("我的信息").setMessage("手机号码:  " + phonenumber + "\n" +
                "真实姓名:  " + realname + "\n" +
                "学生证号码:" + studentId + "\n" +
                "公寓地址:  " + address + "\n" +
                "注册日期:  " + createDate);
        builder.show();
    }

}
