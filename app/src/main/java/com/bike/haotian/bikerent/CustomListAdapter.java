package com.bike.haotian.bikerent;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import utils.Bike;
import utils.Cart;

/**
 * Created by HAOTIAN on 2016/7/26.
 */
public class CustomListAdapter extends BaseAdapter {

    static class ViewHolder { // 自定义控件集合
        public Button button;
        public ImageView imageView;
        public TextView text_bikedesc;
        public TextView text_biketype;
        public TextView text_bikeprice;

    }

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<Bike> list;
    private static String URI_HOST = "http://www.hmu614.wang/";
    public CustomListAdapter(Context context, List<Bike> list) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.bikelist_item, null);
            viewHolder.button = (Button) convertView.findViewById(R.id.addCart);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageview_bike);
            viewHolder.text_bikedesc = (TextView) convertView.findViewById(R.id.text_bikedesc);
            viewHolder.text_bikeprice = (TextView) convertView.findViewById(R.id.text_bikeprice);
            viewHolder.text_biketype = (TextView) convertView.findViewById(R.id.text_biketype);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text_bikedesc.setText(list.get(position).getDesc());
        viewHolder.text_biketype.setText(list.get(position).getType());
        viewHolder.text_bikeprice.setText(list.get(position).getPrice()+"/天");
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cart cart = new Cart();
                cart.setUsername(BmobUser.getCurrentUser().getUsername());
                cart.setBikeid(list.get(position).getId());

                cart.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                    if (e==null){
                        Toast.makeText(mContext, "成功添加购物车", Toast.LENGTH_SHORT).show();}
                        else {
                         if (e.getErrorCode() == 401){
                             Toast.makeText(mContext,"不能重复添加购物车",Toast.LENGTH_SHORT).show();
                         }else
                        Toast.makeText(mContext,""+e.getErrorCode(),Toast.LENGTH_SHORT).show();
                    }
                    }
                });

            }
        });
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext,"..",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,ShowImageActivity.class);

                intent.putExtra("image",URI_HOST+list.get(position).getImage());

                mContext.startActivity(intent);
            }
        });
        Log.i("ss", "getView: "+list.get(position).getImage());
        Picasso.with(mContext).
                load(URI_HOST+list.get(position).getImage()).
             //   memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).
             //   resize(50,50).
                into(viewHolder.imageView);

        return convertView;
    }
}