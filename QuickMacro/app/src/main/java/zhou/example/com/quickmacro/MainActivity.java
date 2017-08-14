package zhou.example.com.quickmacro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt_1 = (Button) findViewById(R.id.bt_1);
        bt_1.setOnClickListener(this);
    }

    private void initShell() {
        SharedPreferences mysf = getSharedPreferences("shell", Activity.MODE_PRIVATE);
        String cmds = mysf.getString("cmds", "");

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bt_1:
                intent = new Intent(this, RecordingService.class);
                startService(intent);
                startAPP();
                break;
        }
    }

    /**
     * 开启执行脚本的app，默认跳转到主界面
     */
    public void startAPP() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "没有安装", Toast.LENGTH_LONG).show();
        }
    }
}

