package jp.morimotor.beaconattend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        // ボタン
        findViewById(R.id.setting).setOnClickListener(this);
        findViewById(R.id.setting2).setOnClickListener(this);
        findViewById(R.id.database).setOnClickListener(this);
        findViewById(R.id.help).setOnClickListener(this);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            // 個人設定
            case R.id.setting:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;

            // ビーコン設定
            case R.id.setting2:
                intent = new Intent(this, Setting2Activity.class);
                startActivity(intent);
                break;

            // データベース閲覧
            case R.id.database:
                intent = new Intent(this, DatabaseActivity.class);
                startActivity(intent);
                break;

            // ヘルプ
            case R.id.help:
                intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;

            // サービス開始
            case R.id.start:
                intent = new Intent(this, BeaconService.class);
                startService(intent);
                break;

            // サービス終了
            case R.id.stop:
                intent = new Intent(this, BeaconService.class);
                stopService(intent);
                finish();
        }
    }
}
