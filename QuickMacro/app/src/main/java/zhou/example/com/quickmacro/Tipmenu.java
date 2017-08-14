package zhou.example.com.quickmacro;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by zhou on 2017/8/10.
 */

public class Tipmenu extends ViewGroup {
    private int radius = 150;
    private boolean flag = false;
    public Tipmenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        getChildAt(0).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    open();
                } else {
                    getChildAt(1).setVisibility(View.GONE);
                    getChildAt(2).setVisibility(View.GONE);
                    getChildAt(3).setVisibility(View.GONE);
                    getChildAt(4).setVisibility(View.GONE);
                    close();
                }
            }
        });
    }


    //关闭菜单
    protected void close() {
        int childCount = getChildCount() - 1;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i + 1);
            TranslateAnimation translateAnimation =
                    new TranslateAnimation(0, -child.getLeft(), 0, -child.getTop()); //平移动画

            RotateAnimation rotateAnimation =
                    new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, //旋转动画
                            0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

            AnimationSet set = new AnimationSet(true);
            set.addAnimation(rotateAnimation);
            set.addAnimation(translateAnimation);
            set.setStartOffset(100 * i);//设置动画延时多长时间后播放
            set.setDuration(300);
            child.startAnimation(set);
            flag = true;
        }

    }


    //打开菜单
    private void open() {
        int childCount = getChildCount() - 1;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i + 1);
            TranslateAnimation translateAnimation =
                    new TranslateAnimation(-child.getLeft(), 0, -child.getTop(), 0); //平移动画

            RotateAnimation rotateAnimation =
                    new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, //旋转动画
                            0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

            AnimationSet set = new AnimationSet(true);
            set.addAnimation(translateAnimation);
            set.addAnimation(rotateAnimation);
            set.setFillAfter(true);
            set.setStartOffset(100 * i);//设置动画延时多长时间后播放
            set.setDuration(300);
            child.startAnimation(set);
            flag = false;

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);//测量自已
        measureChildren(widthMeasureSpec, heightMeasureSpec);//测量儿子
        View child = getChildAt(0);
        setMeasuredDimension(radius + child.getMeasuredWidth(), radius + child.getMeasuredHeight());
    }


    // 卫星菜单测量和排版
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View main = getChildAt(0);
        main.layout(0, 0, main.getMeasuredWidth(), main.getMeasuredHeight());
        int count = getChildCount() - 1;
        double arc = Math.PI / 2 / (count - 1);
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i + 1);
            int left = (int) (Math.sin(arc * i) * radius);
            int top = (int) (Math.cos(arc * i) * radius);
            child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
        }
    }

}

