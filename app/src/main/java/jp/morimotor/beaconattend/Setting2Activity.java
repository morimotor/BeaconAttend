package jp.morimotor.beaconattend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Setting2Activity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    private SharedPreferences sharedPref;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting2);


        getSupportActionBar().setTitle("ビーコン設定");

        // アクションバーでアプリのアイコンの表示設定
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        // アクションバーでアプリタイトルの表示設定
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // アクションバーで戻るボタンの表示設定
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        makeListItems();
    }

    public void makeListItems(){
        List<String> list = new ArrayList<String>();

        try {

            String json = sharedPref.getString("listdata", "");
            if (json != null) {
                JSONArray array = new JSONArray(json);

                for (int i = 0; i < array.length(); i++) {
                    String[] str = array.getString(i).split(",", 0);
                    list.add(str[0] + " Major:" + str[1] + " Minor:" + str[2]);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // メニューの要素を追加して取得
        MenuItem actionItem = menu.add("addButton");

        // SHOW_AS_ACTION_IF_ROOM:余裕があれば表示
        actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        // アイコンを設定
        actionItem.setIcon(R.drawable.ic_action_new);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //アクションバーの戻るを押したときの処理
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        // アクションバーに+ボタン
        if(item.getTitle().toString().equals("addButton")){
            Intent intent = new Intent(this, BeaconRegistryActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        // Listから登録情報削除

        List<String> list = new ArrayList<String>();

        // リストデータ取り出し
        try {
            String json = sharedPref.getString("listdata", "");
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) list.add(array.getString(i));
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(list.get(position) + "を削除しますか？");
        alertDialogBuilder.setPositiveButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.setNegativeButton("削除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 削除
                List<String> list = new ArrayList<String>();
                try {
                    String json = sharedPref.getString("listdata", "");
                    JSONArray array = new JSONArray(json);
                    for (int i = 0; i < array.length(); i++) list.add(array.getString(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                list.remove(position);

                try{
                    JSONArray root = new JSONArray();
                    for(int i = 0; i < list.size(); i++)root.put(list.get(i));

                    SharedPreferences.Editor e = sharedPref.edit();
                    e.putString("listdata", root.toString());
                    e.apply();

                }catch (Exception e) {
                    e.printStackTrace();
                }

                // リスト更新
                makeListItems();


            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        return false;
    }

}
