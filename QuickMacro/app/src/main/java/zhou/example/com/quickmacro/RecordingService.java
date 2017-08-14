package zhou.example.com.quickmacro;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RecordingService extends Service {

    private OutputStream outputStream;
    private DataOutputStream dataOutputStream;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread() {
            @Override
            public void run() {
                //从sp中取出保存的操作，用adb命令进行执行
                String record = SPutils.getString(RecordingService.this.getApplicationContext(), Contant.RECORD);
                execShellCmd(record);
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 执行shell命令
     *
     * @param record
     */
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
