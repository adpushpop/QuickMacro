package zhou.example.com.quickmacro;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SaveService extends Service {
    private WindowManager wm;
    private View sore;
    private Handler handler = new Handler();
    private OutputStream outputStream;
    private DataOutputStream dataOutputStream;


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
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        sore = View.inflate(SaveService.this, R.layout.tip, null);
        wm.addView(sore, params);
        sore.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(SaveService.this, SaveService.class);
                Intent intent1 = new Intent(SaveService.this.getApplicationContext(), RecordingService.class);
                stopService(intent);
                stopService(intent1);
                wm.removeView(sore);
                Toast.makeText(SaveService.this.getApplicationContext(), "脚本录制完成", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        sore.setOnTouchListener(new View.OnTouchListener() {
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
                        break;
                    case MotionEvent.ACTION_UP:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        int dx = moveX - downX;
                        int dy = moveY - downY;
                        //取绝对值
                        int x = Math.abs(dx);
                        int y = Math.abs(dy);
                        if (x < 10 && y < 10) {
                            String tap = SPutils.getString(SaveService.this.getApplicationContext(), Contant.RECORD);
                            tap = tap + "input tap " + moveX + " " + moveY +
                                    "\n sleep 2 \n";
                            SPutils.putString(SaveService.this.getApplicationContext(), Contant.RECORD, tap);
                            execShellCmd("input tap " + moveX + " " + moveY + "\n sleep 2 \n");
                        } else {
                            String swap = SPutils.getString(SaveService.this.getApplicationContext(), Contant.RECORD);
                            swap = swap + "input swipe " + downX + " " + downY + " " + moveX + " " + moveY + " " + "  \n sleep 2 \n ";
                            SPutils.putString(SaveService.this.getApplicationContext(), Contant.RECORD, swap);
                            execShellCmd("input swipe " + downX + " " + downY + " " + moveX + " " + moveY);
                        }
                        wm.removeView(sore);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                wm.addView(sore, params);
                            }
                        }, 1000);
                        Log.i("sore", "x=" + moveX);
                        Log.i("sore", "y=" + moveY);
                        Log.i("sore", "dx=" + dx);
                        Log.i("sore", "dy=" + dy);
                        Log.i("test", "up");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private Process process = null;

    private void execShellCmd(String record) {
        // 申请获取root权限，这一步很重要，不然会没有作用
        try {
//           左滑命令
//            record = "input swipe 0 700 360 700\nsleep 2\n input swipe 0 700 360 700\n" +
//                    "sleep 2\n" +
//                    " input swipe 0 700 360 700\n" +
//                    "sleep 2\n" +
//                    " input swipe 0 700 360 700\n" +
//                    "sleep 2\n" +
//                    " input swipe 0 700 360 700\n" +
//                    "sleep 2\n" +
//                    " ";
            process = Runtime.getRuntime().exec("su");
            // 获取输出流
            outputStream = process.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(record);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "脚本执行失败，请检查root！", Toast.LENGTH_SHORT).show();
        }
    }
}
