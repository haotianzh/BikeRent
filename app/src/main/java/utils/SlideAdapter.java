package utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bike.haotian.bikerent.R;
import com.bike.haotian.bikerent.ShoppingCartActivity;
import com.bike.haotian.bikerent.SliderView;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by HAOTIAN on 2016/8/7.
 */
public class SlideAdapter extends BaseAdapter {
    private List<ShoppingCartActivity.MessageItem>  mMessageItems;
    private LayoutInflater mInflater;
    private Context mContext;
    private static String TAG ="slideapter    ";
    private static String URI_HOST = "http://www.hmu614.wang/";
    private Handler mHandler;
    private  Map<String,Boolean> isSelected;
    class ViewHolder{
        public CheckBox checkBox;
        public ImageView imageView;
        public TextView textView;
        public ViewGroup deleteHolder;
        public TextView time;

        ViewHolder(View view) {
           checkBox = (CheckBox) view.findViewById(R.id.cart_checkbox);
            imageView = (ImageView) view.findViewById(R.id.cart_imageview);
            textView = (TextView) view.findViewById(R.id.cart_textview);
            deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
            time = (TextView) view.findViewById(R.id.cart_time);
        }
    }
    public SlideAdapter(Context context, List<ShoppingCartActivity.MessageItem> list,Handler handler) {
        super();
        mHandler = handler;
        mContext = context;
        mMessageItems = list;
        isSelected = new HashMap<String, Boolean>();
        isSelected.clear();
        mInflater = LayoutInflater.from(mContext);

        Log.i("s", "SlideAdapter: wcao");
        for (ShoppingCartActivity.MessageItem item :mMessageItems) {

            isSelected.put(item.objectId,false);

            Log.i(TAG, "SlideAdapter: "+item.objectId);
        }



    }
    public Map<String,Boolean> getIsSelected(){
        return  isSelected;
    }

    @Override
    public int getCount() {
        return mMessageItems.size();

    }

    @Override
    public Object getItem(int position) {
        return mMessageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int newposition;
        newposition = position;
        final ViewHolder holder;
        SliderView slideView = (SliderView) convertView;
        if (slideView == null) {
            View itemView = mInflater.inflate(R.layout.shoppingcart_item, null);

            slideView = new SliderView(mContext);
            slideView.setContentView(itemView);
            holder = new ViewHolder(slideView);
            slideView.setTag(holder);
        } else {
            holder = (ViewHolder) slideView.getTag();
        }
        final ShoppingCartActivity.MessageItem item = mMessageItems.get(position);
        slideView.shrink();
        holder.textView.setText("单价："+item.price);
        holder.time.setText(item.time);
        Log.i("ss", "getView: "+isSelected.size());
        holder.checkBox.setChecked(isSelected.get(item.objectId));
        Picasso.with(mContext).load(URI_HOST+item.image).resize(200,200).into(holder.imageView);
        holder.imageView.setBackgroundResource(R.mipmap.ic_launcher);
        holder.deleteHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected.get(item.objectId)){
                    Message message = new Message();
                    message.what = 0x200;
                    message.obj = mMessageItems.get(newposition).price;
                    mHandler.sendMessage(message);
                }
                Cart cart = new Cart();
                cart.setObjectId(item.cartObjectId);
                cart.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null)
                        { Toast.makeText(mContext,"已从购物车移除"+item.bikeid+" "+BmobUser.getCurrentUser().getUsername(),Toast.LENGTH_SHORT).show();}
                        else
                        {
                            Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
                mMessageItems.remove(newposition);
                isSelected.remove(item.objectId);

                SlideAdapter.this.notifyDataSetChanged();

                Log.i("ss", "onClick: "+mMessageItems.size()+"positon"+newposition);
            }
        });
//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Log.i("ss", "onCheckedChanged: "+position);
//                if (isChecked){
//
//                      Message message = new Message();
//                      message.what = 0x100;
//                      message.obj = mMessageItems.get(position).price;
//                      mHandler.sendMessage(message);
//                  }
//                else {
//                      Message message = new Message();
//                      message.what = 0x200;
//                      message.obj = mMessageItems.get(position).price;
//                      mHandler.sendMessage(message);
//                  }
//            }
//        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (holder.checkBox.isChecked()){
                     isSelected.put(item.objectId,true);
                     Message message = new Message();
                      message.what = 0x100;
                      message.obj = mMessageItems.get(newposition).price;
                      mHandler.sendMessage(message);
                 }else
                 {
                      isSelected.put(item.objectId,false);
                     Message message = new Message();
                     message.what = 0x200;
                     message.obj = mMessageItems.get(newposition).price;
                     mHandler.sendMessage(message);
                 }
            }
        });


        return slideView;
    }



}
