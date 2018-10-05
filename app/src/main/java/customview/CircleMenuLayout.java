package customview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bike.haotian.bikerent.BikesListActivity;
import com.bike.haotian.bikerent.MyInfoActivity;
import com.bike.haotian.bikerent.R;
import com.squareup.picasso.Picasso;

/**
 * Created by HAOTIAN on 2016/5/12.
 */
public class CircleMenuLayout extends ViewGroup {

    private Context mContext;

    private int mRadius;

    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 4f;

    private float RADIO_DEFAULT_CENTERITEM_DIMENSION = 1 / 3f;

    private static final float RADIO_PADDING_LAYOUT = 1 / 24f;

    private static final int FLINGABLE_VALUE = 300;

    private static final int NOCLICK_VALUE = 3;

    private int mFlingableValue = FLINGABLE_VALUE;

    private float mPadding;

    private double mStartAngle = 0;

    private String[] mItemTexts;

    private int[] mItemImgs;

    private int mMenuItemCount;

    private float mTmpAngle;

    private long mDownTime;

    private boolean isFling;

    private int mMenuItemLayoutId = R.layout.circle_menu_item;


    public class ViewHolder {
        public ImageView imageView;
        public TextView textView;
    }

    //add view items
    public void setMenuItemIconsAndTexts(int[] resId, String[] texts) {
        mItemImgs = resId;
        mItemTexts = texts;

        if (resId == null && texts == null) {
            throw new IllegalArgumentException("至少设置其一");
        }

        mMenuItemCount = resId == null ? texts.length : resId.length;

        if (resId != null && texts != null) {
            mMenuItemCount = Math.min(resId.length, texts.length);
        }

        addMenuItems();
    }

    private void addMenuItems() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ViewHolder viewHolder = new ViewHolder();
        for (int i = 0; i < mMenuItemCount; i++) {
            final int j = i;
            View view = inflater.inflate(mMenuItemLayoutId, null);

            ImageView iv = (ImageView) view.findViewById(R.id.id_circle_menu_item_image);
            TextView tv = (TextView) view.findViewById(R.id.id_circle_menu_item_text);
            if (iv != null) {
                iv.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(mItemImgs[i]).into(iv);
                //   iv.setImageResource(mItemImgs[i]);
                //根据i确定不同的onclick方法
                setListener(i, iv);
            }
            if (tv != null) {
                tv.setVisibility(View.VISIBLE);
                tv.setText(mItemTexts[i]);
            }

            addView(view);
        }


    }

    private void setListener(int which, ImageView iv) {
        switch (which) {
            case 0:
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, BikesListActivity.class);
                        mContext.startActivity(intent);
                    }
                });
                break;
            case 1:
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                        dialog.setTitle("医大自行车平台").
                                setMessage("如果有想将自己的爱车向外出租的童鞋,把你的联系方式通过邮件告诉我们\n" +
                                        "我们的邮箱是:perryre@163.com\n" +
                                        "\n" +
                                        "或者联系QQ：2057712412 \n" +
                                        "\n" +
                                        "我们将在核实完基本信息之后将您的爱车上架出租" +
                                        "\n谢谢大家的配合,敬礼！" +
                                        "\n～(￣▽￣～)(～￣▽￣)～")
                                .setIcon(R.drawable.hmu1);
                        dialog.show();
                    }
                });
                break;
            case 2:
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                        dialog.setIcon(R.drawable.hmu1).
                                setTitle("联系我们")
                                .setMessage("我们是哈尔滨医科大学生物信息学院的一群致力于骑车外出郊游的狂热分子,我们发现有很多同学们在哈没有自己的自行车,苦于和同学外出游玩，于是乎有此想法，希望大家多多配合，多多支持\n" +
                                        "\n" +
                                        "公寓地址：哈尔滨医科大学校部4公寓\n" +
                                        "联系电话：18845075855\n" +
                                        "邮箱：perryre@163.com\n" +
                                        "qq群：23120540\n" +
                                        "\n" +
                                        "目前仅限哈尔滨医科大学的学生持学生证租车");
                        dialog.show();
                    }
                });
                break;
            case 3:
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, MyInfoActivity.class);
                        mContext.startActivity(intent);
                    }
                });


            default:
                break;
        }
    }

    public CircleMenuLayout(Context context) {
        this(context, null);
    }

    public CircleMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(0, 0, 0, 0);
        mContext = context;
        Log.i("666666", "CircleMenuLayout: " + getChildCount());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //      Log.i("66666","LAYOUT------------ONMEASURE");
        //      Log.i("6666","LAYOUTmeasure-------"+getChildCount());
        int resWidth = 0;
        int resHeight = 0;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            resWidth = getSuggestedMinimumWidth();
            resWidth = resWidth == 0 ? getDefaultWidth() : resWidth;
            resHeight = getSuggestedMinimumHeight();
            resHeight = resHeight == 0 ? getDefaultWidth() : resHeight;
        }

        setMeasuredDimension(resWidth, resHeight);

        //measure all children views

        mRadius = Math.min(getMeasuredHeight(), getMeasuredWidth());
        final int count = getChildCount();
        int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
        int childMode = MeasureSpec.EXACTLY;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            int makeMeasureSpec = -1;
            if (child.getId() == R.id.id_circle_menu_item_center) {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (mRadius * RADIO_DEFAULT_CENTERITEM_DIMENSION), childMode);
                child.measure(makeMeasureSpec, makeMeasureSpec);
            } else {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize, childMode);
                int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec((int) (childSize * 2), childMode);
                child.measure(makeMeasureSpec, makeMeasureSpec2);
            }
            int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec((int) (childSize * 1.5), childMode);
            //         child.measure(makeMeasureSpec,makeMeasureSpec);
            Log.i("ss", "onMeasure: " + i + "  :" + childSize + "  " + child.getMeasuredWidth() + "  " + child.getMeasuredHeight());
        }

        mPadding = RADIO_PADDING_LAYOUT * mRadius;
    }

    public void setPadding(float mPadding) {
        this.mPadding = mPadding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("6666", "LAYOUT----onDraw: ");

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            //         Log.i("66666","LAYOUT------------ONLAYOUT"+"true");
        } else {//Log.i("66666","LAYOUT------------ONLAYOUT"+"false");
        }
        //      Log.i("6666","LAYOUT-------"+getChildCount());
        int layoutRadius = mRadius;
        final int childCount = getChildCount();

        int left, top;

        int cWidth = (int) (layoutRadius * RADIO_DEFAULT_CHILD_DIMENSION);

        float angleDelay = 0;
        if (getChildCount() != 1) {
            angleDelay = 360 / (getChildCount() - 1);
        }


        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getId() == R.id.id_circle_menu_item_center) {
                continue;
            }
            if (child.getVisibility() == GONE) {
                continue;
            }

            mStartAngle %= 360;

            float tmp = layoutRadius / 2f - cWidth / 2 - mPadding;

            left = layoutRadius
                    / 2
                    + (int) Math.round(tmp
                    * Math.cos(Math.toRadians(mStartAngle)) - 1 / 2f
                    * cWidth);
            top = layoutRadius
                    / 2
                    + (int) Math.round(tmp
                    * Math.sin(Math.toRadians(mStartAngle)) - 1 / 2f
                    * cWidth);
            //left = layoutRadius /2 +(int)Math.round(tmp*Math.cos(Math.toRadians(mStartAngle))-1/2f*cWidth);
            child.layout(left, top, left + cWidth, top + cWidth);

            mStartAngle += angleDelay;
        }

        View cView = findViewById(R.id.id_circle_menu_item_center);
        int cl = layoutRadius / 2 - cView.getMeasuredWidth() / 2;
        int cr = cl + cView.getMeasuredWidth();
        cView.layout(cl, cl, cr, cr);

    }

    private int getDefaultWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
    }

    private AutoFlingRunnable mFlingRunnable;


    private float mLastX;
    private float mLastY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        // Log.e("TAG", "x = " + x + " , y = " + y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mLastX = x;
                mLastY = y;
                mDownTime = System.currentTimeMillis();
                mTmpAngle = 0;

           
                if (isFling) {
                    // 移除快速滚动的回调
                    removeCallbacks(mFlingRunnable);
                    isFling = false;
                    return true;
                }

                break;
            case MotionEvent.ACTION_MOVE:

           
                float start = getAngle(mLastX, mLastY);
      
                float end = getAngle(x, y);

                // Log.e("TAG", "start = " + start + " , end =" + end);
       
                if (getQuadrant(x, y) == 1 || getQuadrant(x, y) == 4) {
                    mStartAngle += end - start;
                    mTmpAngle += end - start;
                } else
          
                {
                    mStartAngle += start - end;
                    mTmpAngle += start - end;
                }
          
                requestLayout();

                mLastX = x;
                mLastY = y;

                break;
            case MotionEvent.ACTION_UP:

        
                float anglePerSecond = mTmpAngle * 1000
                        / (System.currentTimeMillis() - mDownTime);

                // Log.e("TAG", anglePrMillionSecond + " , mTmpAngel = " +
                // mTmpAngle);

                if (Math.abs(anglePerSecond) > mFlingableValue && !isFling) {
                    // post一个任务，去自动滚动
                    post(mFlingRunnable = new AutoFlingRunnable(anglePerSecond));

                    return true;
                }

                if (Math.abs(mTmpAngle) > NOCLICK_VALUE) {
                    return true;
                }

                break;
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }


    private float getAngle(float xTouch, float yTouch) {
        double x = xTouch - (mRadius / 2d);
        double y = yTouch - (mRadius / 2d);
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }

    private int getQuadrant(float x, float y) {
        int tmpX = (int) (x - mRadius / 2);
        int tmpY = (int) (y - mRadius / 2);
        if (tmpX >= 0) {
            return tmpY >= 0 ? 4 : 1;
        } else {
            return tmpY >= 0 ? 3 : 2;
        }

    }

    public void setMenuItemLayoutId(int mMenuItemLayoutId) {
        this.mMenuItemLayoutId = mMenuItemLayoutId;
    }


    public void setFlingableValue(int mFlingableValue) {
        this.mFlingableValue = mFlingableValue;
    }

    private class AutoFlingRunnable implements Runnable {

        private float angelPerSecond;

        public AutoFlingRunnable(float velocity) {
            this.angelPerSecond = velocity;
        }

        public void run() {

            if ((int) Math.abs(angelPerSecond) < 20) {
                isFling = false;
                return;
            }
            isFling = true;

            mStartAngle += (angelPerSecond / 30);

            angelPerSecond /= 1.0666F;
            postDelayed(this, 30);

            requestLayout();
        }
    }
}