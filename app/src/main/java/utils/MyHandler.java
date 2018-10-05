package utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by HAOTIAN on 2016/8/12.
 */
public class MyHandler extends Handler{
    private Context  mContext;


    public MyHandler(Context context){
          this.mContext = context;
    }


}
