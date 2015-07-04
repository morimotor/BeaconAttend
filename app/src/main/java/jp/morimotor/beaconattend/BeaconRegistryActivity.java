package jp.morimotor.beaconattend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class BeaconRegistryActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText Ename;
    private EditText Emajor;
    private EditText Eminor;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_registry);

        getSupportActionBar().setTitle("ビーコン登録");

        // アクションバーでアプリのアイコンの表示設定
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        // アクションバーでアプリタイトルの表示設定
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // アクションバーで戻るボタンの表示設定
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.BbeaconRegistry).setOnClickListener(this);

        Ename = (EditText)findViewById(R.id.ERname);
        Emajor = (EditText)findViewById(R.id.ERmajor);
        Eminor = (EditText)findViewById(R.id.ERminor);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //アクションバーの戻るを押したときの処理
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        // 完了ボタンが押された時
        // リストにname,major,minorの形で登録

        List<String> list = new ArrayList<String>();

        // データ読み込み
        try{
            String json = sharedPref.getString("listdata", "");
            JSONArray array =new JSONArray(json);

            for(int i = 0; i < array.length(); i++)list.add(array.getString(i));
        }catch (Exception e) {
            e.printStackTrace();
        }

        if(Ename.getText().toString().matches(".*" + "," + ".*") || Emajor.getText().toString().matches(".*" + "," + ".*") || Eminor.getText().toString().matches(".*" + "," + ".*")){

            // ダイアログ
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("エラー");
            alertDialogBuilder.setMessage("「,」は使用できません");
            alertDialogBuilder.setPositiveButton("閉じる",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
        else{

            // データ登録
            list.add(Ename.getText().toString() + "," + Emajor.getText().toString() + "," + Eminor.getText().toString());


            try{
                JSONArray root = new JSONArray();
                for(int i = 0; i < list.size(); i++)root.put(list.get(i));

                SharedPreferences.Editor e = sharedPref.edit();
                e.putString("listdata", root.toString());
                e.apply();

            }catch (Exception e) {
                e.printStackTrace();
            }


            finish();
        }
    }
}
