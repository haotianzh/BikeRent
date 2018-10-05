package com.bike.haotian.bikerent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mingle.widget.ShapeLoadingDialog;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import utils.MyUser;

/**
 * Created by HAOTIAN on 2016/7/25.
 */
public class LoginActivity extends Activity {

    private EditText edt_phonenumber;
    private EditText edt_password;
    private Button btn_login;
    private ShapeLoadingDialog shapeLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBomb();
        canConnectToNet();
        //  initMob();


        setContentView(R.layout.activity_login);
        findViews();

        init();

    }

    private void canConnectToNet() {
        boolean isConnection = isNetworkAvailable();
        if (!isConnection){
            Toast.makeText(LoginActivity.this, "当前无网络,请检查网络连接", Toast.LENGTH_SHORT).show();
            return;
        }
        isLogin();
    }

    private boolean isNetworkAvailable() {
        Context context = this.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void initMob() {
        SMSSDK.initSDK(this, "15b6f8635554a", "7a0e55b65b8661192451c5ea35fc3b6b");


    }

    private void isLogin() {
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initBomb() {
        Bmob.initialize(this, "1ef9edd917b12cdf274beabaf1d3c90a");

    }

    private void init() {

        shapeLoadingDialog = new ShapeLoadingDialog(LoginActivity.this);
        shapeLoadingDialog.setLoadingText("登录中...");
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phonenumber = edt_phonenumber.getText().toString().trim();
                String password = edt_password.getText().toString().trim();

                if (phonenumber.equals("")) {
                    Toast.makeText(LoginActivity.this, "用户名为手机号，且不能为空！", Toast.LENGTH_LONG).show();
                    return;
                } else if (password.equals("")) {
                    Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }
                shapeLoadingDialog.show();
                BmobUser.loginByAccount(phonenumber, password, new LogInListener<MyUser>() {

                    @Override
                    public void done(MyUser user, BmobException e) {
                        shapeLoadingDialog.dismiss();
                        if (user != null) {
                            Log.i("smile", "用户登陆成功");

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            Log.i("smile", e.getMessage());
                        }
                    }
                });
                // 将手机号码和密码输入到服务器并返回结果
//             Boolean isSuccess =  postUsrAndPwd2Net();
//             if (isSuccess){
//                 Intent intent =  new Intent(LoginActivity.this,MainActivity.class);
//
//                 startActivity(intent);
//
//             }
            }
        });
    }


    private void findViews() {
        edt_phonenumber = (EditText) findViewById(R.id.edt_phonenumber);
        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_login = (Button) findViewById(R.id.btn_login);

    }

    public void login2register(View v) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, 1);


    }

    public void forgetPwd(View v) {

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.mipmap.icon)
                .setTitle("忘记密码了")
                .setMessage(R.string.howtogetpwd).create();
        dialog.show();
    }


}
