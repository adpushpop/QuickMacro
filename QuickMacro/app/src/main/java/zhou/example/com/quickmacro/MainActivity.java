package zhou.example.com.quickmacro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt_1 = (Button) findViewById(R.id.bt_1);
        Button bt_2 = (Button) findViewById(R.id.bt_2);
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
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
            case R.id.bt_2:
                intent = new Intent(MainActivity.this, SaveService.class);
                SPutils.putString(MainActivity.this.getApplicationContext(), Contant.RECORD, "");
                startService(intent);
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

