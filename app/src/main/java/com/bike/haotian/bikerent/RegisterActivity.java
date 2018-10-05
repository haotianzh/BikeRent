package com.bike.haotian.bikerent;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import utils.MyUser;

/**
 * Created by HAOTIAN on 2016/7/26.
 */
public class RegisterActivity extends AppCompatActivity {

    private static final int FLAG_START_TIMING = 0X123;
    private static final int FLAG_STOP_TIMING = 0X122;
    private static int time = 60;
    private Button btn_getSmsCode;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
              switch (msg.what){
                  case FLAG_START_TIMING:
                       if (time == 0){
                            mHandler.sendEmptyMessage(FLAG_STOP_TIMING);
                            return;

                       }
                       time = time -1;
                       btn_getSmsCode.setClickable(false);
                       btn_getSmsCode.setText(time+"S后可重新发送");
                       mHandler.sendEmptyMessageDelayed(FLAG_START_TIMING,1000);
                        break;
                  case FLAG_STOP_TIMING:
                       btn_getSmsCode.setClickable(true);
                       btn_getSmsCode.setText("发送验证码");

              }
        }
    };
    private EditText edt_registermobilephonenumber;
    private EditText edt_SMScode;
    private EditText edt_password;
    private EditText edt_passwordagain;
    private EditText edt_address;
    private EditText edt_studentId;
    private EditText edt_realname;
    private String password;
    private String passwordagain;
    private String address;
    private String realname;
    private String studentId;
    private String SMScode;
    private String registermobilephonenumber;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
         findViews();
         setToolbar();
       // registEvent();
    }

    private void registEvent() {
        EventHandler eh=new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        sayhello();
                        sendMyInfo();
                        Log.i("ss", "afterEvent: success!!");
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){

                        Log.i("ss","get success");
                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    private void getValues() {
        registermobilephonenumber = edt_registermobilephonenumber.getText().toString().trim();
        SMScode = edt_SMScode.getText().toString().trim();
        address = edt_address.getText().toString().trim();
        realname = edt_realname.getText().toString().trim();
        password = edt_password.getText().toString().trim();
        passwordagain = edt_passwordagain.getText().toString().trim();
        studentId = edt_studentId.getText().toString().trim();
    }

    private void findViews() {
        edt_registermobilephonenumber = (EditText) findViewById(R.id.edt_registermobilephonenumber);
        edt_SMScode = (EditText) findViewById(R.id.edt_SMScode);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_realname = (EditText) findViewById(R.id.edt_realname);
        edt_password = (EditText) findViewById(R.id.edt_passwordregister);
        edt_passwordagain = (EditText) findViewById(R.id.edt_passwordagain);
        edt_studentId = (EditText) findViewById(R.id.edt_studentId);
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        btn_getSmsCode = (Button) findViewById(R.id.btn_getSMScode);

    }

    public void getSMSCode(View view){
       registermobilephonenumber = edt_registermobilephonenumber.getText().toString().trim();
//        SMSSDK.getVerificationCode("86",registermobilephonenumber);
       if (registermobilephonenumber.equals("")){
           Toast.makeText(RegisterActivity.this,"手机号码不能为空",Toast.LENGTH_SHORT).show();

           return;

       }else if (registermobilephonenumber.length() != 11 ){
            Toast.makeText(RegisterActivity.this,"手机号码不正确",Toast.LENGTH_SHORT).show();
           return;
       }
        BmobSMS.requestSMSCode(registermobilephonenumber,"Hmu", new QueryListener<Integer>() {

            @Override
            public void done(Integer smsId,BmobException ex) {
                if(ex==null){//验证码发送成功
                    time = 60;
                    Log.i("smile", "短信id："+smsId);//用于查询本次短信发送详情
                    mHandler.sendEmptyMessage(FLAG_START_TIMING);
                    Toast.makeText(RegisterActivity.this,"验证码已经发送，注意查收",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void registerMyAccount(View v){

        getValues();
        if (!password.equals(passwordagain)){
            Toast.makeText(RegisterActivity.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
            return;
        }
  //      SMSSDK.submitVerificationCode("86","13803657775", SMScode);






        //初始化用户信息
        MyUser user = new MyUser();
        user.setMobilePhoneNumber(registermobilephonenumber);
        user.setPassword(password);
        user.setAddress(address);
        user.setStudentId(studentId);
        user.setRealname(realname);



        Log.i("ss",SMScode);
        user.signOrLogin(SMScode, new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e==null){
                    if (myUser.getStudentId()!=null){
                        Toast.makeText(RegisterActivity.this,"该用户已经注册",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this,"失败:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setToolbar() {
        toolbar.setTitle("注册页面");//设置Toolbar标题
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
  private void sayhello(){
      Looper.prepare();
        Toast.makeText(RegisterActivity.this,"hello",Toast.LENGTH_SHORT).show();
      Looper.loop();
  }

    private void sendMyInfo(){
     //   初始化用户信息
        MyUser user = new MyUser();
        user.setMobilePhoneNumber(registermobilephonenumber);
        user.setPassword(password);
        user.setAddress(address);
        user.setStudentId(studentId);
        user.setRealname(realname);
        user.setUsername(registermobilephonenumber);



        user.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e==null){
//                    if (myUser.getStudentId()!=null){
//                   //     Toast.makeText(RegisterActivity.this,"该用户已经注册",Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    Log.i("zhuce", "done: success");
                  //  Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                 //   Toast.makeText(RegisterActivity.this,"失败:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.i("zhuce", "done: "+e.getMessage());
                }
            }
        });
    }
}
