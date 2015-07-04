package jp.morimotor.beaconattend;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPref;
    private EditText Ename;
    private EditText Egrade;
    private EditText Eclass;
    private EditText Enumber;
    private EditText Eid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportActionBar().setTitle("個人設定");

        // アクションバーでアプリのアイコンの表示設定
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        // アクションバーでアプリタイトルの表示設定
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // アクションバーで戻るボタンの表示設定
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        findViewById(R.id.submit).setOnClickListener(this);

        Ename = (EditText)findViewById(R.id.Ename);
        Egrade = (EditText)findViewById(R.id.Egrade);
        Eclass = (EditText)findViewById(R.id.Eclass);
        Enumber = (EditText)findViewById(R.id.Enumber);
        Eid = (EditText)findViewById(R.id.Eid);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Ename.setText(sharedPref.getString("name", ""));
        Egrade.setText(sharedPref.getString("grade", ""));
        Eclass.setText(sharedPref.getString("class", ""));
        Enumber.setText(sharedPref.getString("number", ""));
        Eid.setText(sharedPref.getString("id", ""));

        Eid.setText("00000000-0000-0000-0000-000000000000");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //アクションバーの戻るを押したときの処理
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                // データ保存
                SharedPreferences.Editor e = sharedPref.edit();
                e.putString("name", Ename.getText().toString());
                e.putString("grade", Egrade.getText().toString());
                e.putString("class", Eclass.getText().toString());
                e.putString("number", Enumber.getText().toString());
                e.putString("id", Eid.getText().toString());
                e.apply();

                finish();
        }
    }
}
