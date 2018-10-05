package com.bike.haotian.bikerent;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mingle.widget.ShapeLoadingDialog;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import utils.Bike;
import utils.Cart;
import utils.Order;
import utils.SlideAdapter;

/**
 * Created by HAOTIAN on 2016/8/7.
 */
public class ShoppingCartActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private int total;
    private static String TAG = "shoppingcart";
    private SliderListView listview_shoppingcart;
    private List<MessageItem> messageItems;
    private TextView total_fee;
    private SlideAdapter adapter;
    private Handler mHandler;

    public class MessageItem implements Serializable {
        public String objectId;
        public String price;
        public String image;
        public String time;
        public String bikeid;
        public String cartObjectId;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingcart);
        //初始化总价格
        total = 0;
        findViews();
        // 初始化items并绑定listview
        setToolbar();
        messageItems = new ArrayList<>();
        mHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 0x100:
                        total += Integer.valueOf((String) msg.obj);
                        total_fee.setText(""+total);
                        break;
                    case 0x200:
                        total -= Integer.valueOf((String) msg.obj);
                        total_fee.setText(""+total);
                }
            }
        };
        adapter = new SlideAdapter(ShoppingCartActivity.this,messageItems,mHandler);
        getMessageItems();

    }

    private void getMessageItems() {
        BmobQuery<Cart> query = new BmobQuery<>();
        query.addWhereEqualTo("username", BmobUser.getCurrentUser().getUsername());
        query.findObjects(new FindListener<Cart>() {
            @Override
            public void done(List<Cart> list, BmobException e) {
                  for (Cart cart : list){
                      getCartBikeInfo(cart.getBikeid(),cart.getCreatedAt(),cart.getObjectId());
                      Log.i(TAG, "done: "+cart.getUsername()+cart.getBikeid());
                  }


            }
        });



    }

    private void getCartBikeInfo(String id, final String mtime, final String cartObjectId) {
           BmobQuery<Bike> query = new BmobQuery<>();
        query.addWhereEqualTo("id",id);
        query.findObjects(new FindListener<Bike>() {
            @Override
            public void done(List<Bike> list, BmobException e) {
                 for(Bike bike :list){
                     MessageItem item = new MessageItem();
                     item.image = bike.getImage();
                     item.price = bike.getPrice();
                     item.time = mtime;
                     item.bikeid = bike.getId();
                     item.objectId = bike.getObjectId();
                     item.cartObjectId = cartObjectId;
                     messageItems.add(item);
                   //  Log.i(TAG, "done: "+item.image+"  "+item.price);
                     adapter = new SlideAdapter(ShoppingCartActivity.this,messageItems,mHandler);
                     listview_shoppingcart.setAdapter(adapter);
                 }
            }
        });
    }


    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        listview_shoppingcart = (SliderListView) findViewById(R.id.listview_shoppingcart);
         total_fee = (TextView) findViewById(R.id.total_fee);

    }



    private void setToolbar() {
        toolbar.setTitle("购物车");//设置Toolbar标题
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

    public void cart2pay(View v) {
        Map<String,Boolean> isSelected = adapter.getIsSelected();
         List<String> list = new ArrayList<>();
        list.clear();
        for (MessageItem item : messageItems) {
               if(isSelected.get(item.objectId)){
                   list.add(item.objectId);
               }
        }
        Bundle bundle = new Bundle();

        Intent intent = new Intent();
        intent.putStringArrayListExtra("cartId", (ArrayList<String>) list);


        System.out.println(isSelected.toString());
        ShapeLoadingDialog dialog = new ShapeLoadingDialog(this);
        dialog.setLoadingText("正在唤起支付页面");
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }
}
