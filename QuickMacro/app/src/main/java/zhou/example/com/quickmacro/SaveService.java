package zhou.example.com.quickmacro;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class SaveService extends Service implements View.OnClickListener {
    private WindowManager wm;
    private View tipView;
    private View sore;
    private Tipmenu menus;
    private int tip_x;
    private int tip_y;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("sore", "脚本录制服务已开启");
        showTip();
    }

    /**
     * 显示脚本录制对话框
     */
    public void showTip() {
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // WindowManager可以向屏幕添加一个控件, 需要知道控件如何摆放和一些其他属性 所以需要LayoutParams
        // 参见Toast中的TN源码
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高
        params.width = WindowManager.LayoutParams.WRAP_CONTENT; // 宽
        params.format = PixelFormat.TRANSLUCENT; // 半透明
        // 需要权限android.permission.SYSTEM_ALERT_WINDOW
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;// TYPE_PRIORITY_PHONE:优先于通话界面
        // WindowManager.LayoutParams.TYPE_TOAST;
        // //Toast类型

        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON // 屏幕常量
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不可获取焦点
        // | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE; //不可摸


        final WindowManager.LayoutParams params1 = new WindowManager.LayoutParams();
        params1.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高
        params1.width = WindowManager.LayoutParams.WRAP_CONTENT; // 宽
        params1.format = PixelFormat.TRANSLUCENT; // 半透明
        // 需要权限android.permission.SYSTEM_ALERT_WINDOW
        params1.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;// TYPE_PRIORITY_PHONE:优先于通话界面
        // WindowManager.LayoutParams.TYPE_TOAST;
        // //Toast类型

        params1.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON // 屏幕常量
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不可获取焦点
        // | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE; //不可摸


        tipView = View.inflate(SaveService.this, R.layout.tip, null);
        sore = View.inflate(SaveService.this, R.layout.menu, null);
        menus = (Tipmenu) sore.findViewById(R.id.tipments);
        Button click = (Button) sore.findViewById(R.id.record_click);
        Button left = (Button) sore.findViewById(R.id.record_left);
        Button right = (Button) sore.findViewById(R.id.record_right);
        Button finish = (Button) sore.findViewById(R.id.finish);
        finish.setOnClickListener(this);
        click.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        // 添加View到窗口上
        wm.addView(tipView, params);
        wm.addView(sore, params1);
        params1.x = -360;
        params1.y = -640;
        wm.updateViewLayout(sore, params1);
        tipView.setOnTouchListener(new View.OnTouchListener() {
            private int downX;
            private int downY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("test", "down");
                        downX = (int) event.getRawX();
                        downY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("test", "move");
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        int dx = moveX - downX;
                        int dy = moveY - downY;
                        Log.i("sore", "x=" + moveX);
                        Log.i("sore", "y=" + moveY);
                        tip_x = moveX;
                        tip_y = moveY;
                        WindowManager.LayoutParams params = (android.view.WindowManager.LayoutParams) tipView
                                .getLayoutParams();
                        WindowManager.LayoutParams params1 = (android.view.WindowManager.LayoutParams) sore
                                .getLayoutParams();
                        params.x = params.x + dx;
                        params.y = params.y + dy;
                        // 通过WundowManager更新TextView的位置
                        Log.i("sore", "x=" + params.x + " ====   Y=" + params.y);
                        if (params.y < -150 && params.x > 0) {
                            params1.x = -370;
                            params1.y = 600;
                            wm.updateViewLayout(sore, params1);
                        } else if (params.y > -150 && params.x < 0) {
                            params1.x = 330;
                            params1.y = -640;
                            wm.updateViewLayout(sore, params1);
                        } else if (params.y > -150 && params.x > 0) {
                            params1.x = -360;
                            params1.y = -640;
                            wm.updateViewLayout(sore, params1);
                        } else if (params.y < 150 && params1.x < 0) {
                            params1.x = 330;
                            params1.y = 600;
                            wm.updateViewLayout(sore, params1);
                        }

                        wm.updateViewLayout(tipView, params);

                        downX = moveX;
                        downY = moveY;
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("test", "up");
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }

    private String swap = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_click:
                String tap = SPutils.getString(SaveService.this.getApplicationContext(), Contant.RECORD);
                tap = tap + "input tap " + tip_x + " " + tip_y +
                        "\n sleep 2 \n";
                SPutils.putString(SaveService.this.getApplicationContext(), Contant.RECORD, tap);
                break;
            case R.id.record_left:
                swap = SPutils.getString(SaveService.this.getApplicationContext(), Contant.RECORD);
                swap = swap + "input swipe 100 700 360 700  \n sleep 2 \n ";
                SPutils.putString(SaveService.this.getApplicationContext(), Contant.RECORD, swap);
                break;
            case R.id.record_right:
                swap = SPutils.getString(SaveService.this.getApplicationContext(), Contant.RECORD);
                swap = swap + "input swipe 300 700 160 700  \n sleep 2 \n ";
                SPutils.putString(SaveService.this.getApplicationContext(), Contant.RECORD, swap);
                break;
            case R.id.finish:
                wm.removeView(tipView);
                wm.removeView(sore);
                Intent intent = new Intent(SaveService.this, SaveService.class);
                Intent intent1 = new Intent(SaveService.this.getApplicationContext(), RecordingService.class);
                stopService(intent);
                stopService(intent1);
                break;
        }
    }
}
