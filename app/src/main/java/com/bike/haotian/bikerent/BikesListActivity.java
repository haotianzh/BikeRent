package com.bike.haotian.bikerent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.mingle.widget.ShapeLoadingDialog;
//import com.sdsmdg.tastytoast.TastyToast;
//import com.sdsmdg.tastytoast.TastyToast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import utils.Bike;
import utils.RecycleBitmapInLayout;

/**
 * Created by HAOTIAN on 2016/7/26.
 */
public class BikesListActivity extends AppCompatActivity {
    private static String TAG = "BIKELIST";
    private ListView bikeListView;
    private CustomListAdapter adapter;
    private ArrayList<ImageView> list;
    private Toolbar toolbar;
    private ShapeLoadingDialog loading;
    private List<Bike> data_bike;
    //   private String[] datas={"1","2","3","4","1","2","3","4","1","2","3","4"};
    private MaterialRefreshLayout materialRefeshLayout;
    private FloatingActionButton btn_bikelist2cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = new ShapeLoadingDialog(BikesListActivity.this);
        loading.setLoadingText("正在加載自行車列表\n    耐心等待哦");
        loading.show();
        setContentView(R.layout.activity_bikelist);

        findViews();
        setToolbar();
        setRefreshLayout();

        data_bike = new ArrayList<Bike>();
        //  data_bike.add(bike);
        adapter = new CustomListAdapter(this, data_bike);
        bikeListView.setAdapter(adapter);
        getListInfo();
    }

    private void getListInfo() {
        data_bike.clear();
        BmobQuery<Bike> query = new BmobQuery<Bike>();
        //    query.addWhereEqualTo("name","10");
        query.addWhereEqualTo("isrent", true);
        query.setLimit(50);

        query.findObjects(new FindListener<Bike>() {
            @Override
            public void done(List<Bike> list, BmobException e) {

                if (e == null) {
                    data_bike.clear();
                    data_bike.addAll(list);
                    //    data_bike = list;
                    adapter.notifyDataSetChanged();
                    //    adapter = new CustomListAdapter(BikesListActivity.this,data_bike);

                    //    bikeListView.setAdapter(adapter);
                    Log.i(TAG, "done: " + list.size());

                } else {
                    Log.i(TAG, "done: " + e.getMessage());
                }
                loading.dismiss();
            }

        });
    }

    private void setToolbar() {
        toolbar.setTitle("医大租车平台");//设置Toolbar标题
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

    private void setRefreshLayout() {
        materialRefeshLayout.setWaveColor(0x60ffffff);
        materialRefeshLayout.setIsOverLay(true);
        materialRefeshLayout.setWaveShow(true);
        materialRefeshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                refreshListInfo();
            }
        });

    }

    private void refreshListInfo() {
        data_bike.clear();
        BmobQuery<Bike> query = new BmobQuery<Bike>();
        //    query.addWhereEqualTo("name","10");
        query.addWhereEqualTo("isrent", true);
        query.setLimit(50);

        query.findObjects(new FindListener<Bike>() {
            @Override
            public void done(List<Bike> list, BmobException e) {

                if (e == null) {
                    data_bike.clear();
                    data_bike.addAll(list);
                    adapter.notifyDataSetChanged();
                    //       Toast.makeText(BikesListActivity.this, "刷新成功11", Toast.LENGTH_LONG).show();
                    //     data_bike = list;
                    //         adapter.notifyDataSetChanged();
                    //    TastyToast.makeText(BikesListActivity.this,"刷新成功",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                    Toast.makeText(BikesListActivity.this, "刷新成功", Toast.LENGTH_LONG).show();
                    //      adapter = new CustomListAdapter(BikesListActivity.this, data_bike);

                    //      bikeListView.setAdapter(adapter);
                    Log.i(TAG, "done: " + list.size());

                } else {
                    Log.i(TAG, "done: " + e.getMessage());
                }
                materialRefeshLayout.finishRefresh();
            }

        });
    }


    private void findViews() {
        materialRefeshLayout = (MaterialRefreshLayout) findViewById(R.id.refreshLayout);
        bikeListView = (ListView) findViewById(R.id.bikelistview);
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        btn_bikelist2cart = (FloatingActionButton) findViewById(R.id.btn_bikelist2cart);
        btn_bikelist2cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BikesListActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
            }
        });
        View v = LayoutInflater.from(BikesListActivity.this).inflate(R.layout.grey_back, null);
        bikeListView.addHeaderView(v);
        bikeListView.setDividerHeight(15);

    }


}
