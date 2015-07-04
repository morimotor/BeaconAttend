package jp.morimotor.beaconattend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DatabaseActivity extends AppCompatActivity {

    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);


        getSupportActionBar().setTitle("データベース");

        // アクションバーでアプリのアイコンの表示設定
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        // アクションバーでアプリタイトルの表示設定
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // アクションバーで戻るボタンの表示設定
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        wv = (WebView)findViewById(R.id.webView);

        //リンクをタップしたときに標準ブラウザを起動させない
        wv.setWebViewClient(new WebViewClient());

        wv.loadUrl("http://hogehoge.php");
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
}
