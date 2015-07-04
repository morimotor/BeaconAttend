package jp.morimotor.beaconattend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.viewpagerindicator.CirclePageIndicator;

public class HelpActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);



        getSupportActionBar().setTitle("ヘルプ");

        // アクションバーでアプリのアイコンの表示設定
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        // アクションバーでアプリタイトルの表示設定
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // アクションバーで戻るボタンの表示設定
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Pager
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        indicator.setRadius(10 * getResources().getDisplayMetrics().density);
        indicator.setOnPageChangeListener(this);

        findViewById(R.id.helpskip).setOnClickListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //アクションバーの戻るを押したときの処理
        if(id == android.R.id.home){
            finish();
            return true;
        }

        // Licence
        if(item.getTitle().toString().equals("infoButton")) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Info");
            alertDialogBuilder.setMessage("以下のライブラリを使用しています。\n・ViewPagerIndicator\n" +
                    "Copyright 2012 Jake Wharton\n" +
                    "Copyright 2011 Patrik Åkerfeldt\n" +
                    "Copyright 2011 Francisco Figueiredo Jr.\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License.");

            alertDialogBuilder.setPositiveButton("閉じる",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // メニューの要素を追加して取得
        MenuItem actionItem = menu.add("infoButton");

        // SHOW_AS_ACTION_IF_ROOM:余裕があれば表示
        actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        // アイコンを設定
        actionItem.setIcon(R.drawable.ic_info_outline_white_24dp);

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.helpskip:

                finish();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if(position == 4)finish();  // スワイプで閉じる
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
