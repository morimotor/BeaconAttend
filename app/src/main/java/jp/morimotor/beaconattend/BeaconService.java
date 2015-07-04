package jp.morimotor.beaconattend;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BeaconService extends Service {


    private BluetoothAdapter mBluetoothAdapter;

    private WebView webView;

    private String beaconStr = "";

    // Toastを表示させるために使うハンドラ
    private Handler mHandler = new Handler();

    // スレッドを停止するために必要
    private boolean mThreadActive = true;

    private Context mContext = this;

    private boolean accessFlag = false;
    private boolean taikiFlag = false;
    private String URL;
    private String TEACHER;

    // Bluetooth受信のコールバック
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanData) {

            if(scanData.length > 30)
            {
                // 6-9byteは固定値
                if((scanData[5] == (byte)0x4c) && (scanData[6] == (byte)0x00) && (scanData[7] == (byte)0x02) && (scanData[8] == (byte)0x15))
                {
                    // uuid
                    String uuid = IntToHex(scanData[9] & 0xff)
                            + IntToHex(scanData[10] & 0xff)
                            + IntToHex(scanData[11] & 0xff)
                            + IntToHex(scanData[12] & 0xff)
                            + "-"
                            + IntToHex(scanData[13] & 0xff)
                            + IntToHex(scanData[14] & 0xff)
                            + "-"
                            + IntToHex(scanData[15] & 0xff)
                            + IntToHex(scanData[16] & 0xff)
                            + "-"
                            + IntToHex(scanData[17] & 0xff)
                            + IntToHex(scanData[18] & 0xff)
                            + "-"
                            + IntToHex(scanData[19] & 0xff)
                            + IntToHex(scanData[20] & 0xff)
                            + IntToHex(scanData[21] & 0xff)
                            + IntToHex(scanData[22] & 0xff)
                            + IntToHex(scanData[23] & 0xff)
                            + IntToHex(scanData[24] & 0xff);

                    // major minor
                    String major = IntToHex(scanData[25] & 0xff) + IntToHex(scanData[26] & 0xff);
                    String minor = IntToHex(scanData[27] & 0xff) + IntToHex(scanData[28] & 0xff);

                    beaconStr = "UUID:" + uuid + " Major:" + major + " Minor:" + minor;

                    // 登録されているUUID、major、moinorの読み込み
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String _name = sharedPref.getString("name", "");
                    String _grade = sharedPref.getString("grade", "");
                    String _class = sharedPref.getString("class", "");
                    String _number = sharedPref.getString("number", "");
                    String _uuid = sharedPref.getString("id", "");
                    if(uuid.equals(_uuid)){
                        List<String> _teacher = new ArrayList<>();
                        List<String> _major = new ArrayList<>();
                        List<String> _minor = new ArrayList<>();
                        try {

                            String json = sharedPref.getString("listdata", "");
                            JSONArray array = new JSONArray(json);

                            for (int i = 0; i < array.length(); i++) {
                                String[] str = array.getString(i).split(",", 0);
                                _teacher.add(str[0]);
                                _major.add(str[1]);
                                _minor.add(str[2]);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // 受信したものと登録されているものの比較
                        for (int i = 0; i < _major.size(); i++){
                            if(major.equals(_major.get(i)) && minor.equals(_minor.get(i))){

                                Log.d("BeaconService", "onLeScan : Beacon Matching!!!");

                                // 時間取得
                                Calendar calendar = Calendar.getInstance();
                                int year = calendar.get(Calendar.YEAR);
                                int month = calendar.get(Calendar.MONTH) + 1;
                                int day = calendar.get(Calendar.DAY_OF_MONTH);
                                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                int minute = calendar.get(Calendar.MINUTE);
                                int second = calendar.get(Calendar.SECOND);

                                String date = year + "-" + month + "-" + day + " " + hour + ":"+ minute + ":" + second;

                                // 出席の登録（webViewでHTTP通信）
                                URL = "http://hogehoge?par1=" + date  + "&par2=" + _name + "&par3=" + _grade + "&par4=" + _class + "&par5=" + _number + "&par6=" + _teacher.get(i);
                                TEACHER = _teacher.get(i);
                                Log.d("BeaconService", "onLeScan : " + URL);

                                accessFlag = true;

                            }
                        }
                    }
                }
            }
        }
    };

    //intデータを16進数に変換
    public String IntToHex(int i) {
        char hex[] = {Character.forDigit((i >> 4) & 0x0f, 16), Character.forDigit(i & 0x0f, 16)};
        String hex_str = new String(hex);
        return hex_str.toUpperCase();
    }

    // スレッド処理
    private Runnable mTask = new Runnable() {

        @Override
        public void run() {

            // アクティブな間だけ処理
            while (mThreadActive) {

                try {
                    if(taikiFlag){

                        for (int i = 0; i < 6; i++){
                            try {
                                Thread.sleep(10000);
                                Log.d("BeaconService", "run : " + i);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if(!mThreadActive)break;
                        }

                        taikiFlag = false;
                    }
                    if(!mThreadActive || taikiFlag)continue;
                    mBluetoothAdapter.startLeScan(mLeScanCallback);
                    Thread.sleep(5000);

                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    Thread.sleep(5000);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                // ハンドラーをはさまないとToastでエラーでる
                // UIスレッド内で処理をしないといけないらしい
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        if(!beaconStr.equals(""))showText(beaconStr);
                        beaconStr = "";

                        if(accessFlag && !taikiFlag){

                            showNotification(mContext, TEACHER + "の授業に出席", 2, Notification.FLAG_AUTO_CANCEL);

                            webView = new WebView(mContext);

                            // キャッシュを無効に (デフォルトのままだとキャッシュのせいでダウンロードが発生しない？)
                            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

                            // ウィンドウを不可視に
                            webView.setVisibility(View.INVISIBLE);

                            // ページをロード
                            webView.loadUrl(URL);

                            taikiFlag = true;
                            accessFlag = false;
                        }


                    }
                });
            }

            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    showText("スレッド終了");
                }
            });
        }
    };
    private Thread mThread;


    private void showText(final String text) {
        //Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        Log.d("BeaconService", text);

    }


    @Override
    public IBinder onBind(Intent intent) {
        this.showText("サービスがバインドされました。");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        this.showText("onStartCommand");
        this.mThread = new Thread(null, mTask, "NortifyingService");
        this.mThread.start();

        // 通知バーを表示
        showNotification(this, "サービス起動中", 1, Notification.FLAG_NO_CLEAR);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        this.showText("サービスが開始されました。");
        super.onCreate();

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }


    @Override
    public void onDestroy() {
        this.showText("サービスが終了されました。");

        // スレッド停止
        this.mThread.interrupt();
        this.mThreadActive = false;

        this.stopNotification(this, 1);
        super.onDestroy();
    }

    // 通知バーを消す
    private void stopNotification(final Context ctx, int id) {
        NotificationManager mgr = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        mgr.cancel(id);
    }

    // 通知バーを出す
    private void showNotification(final Context ctx, String Text, int id, int type) {

        NotificationManager mgr = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(ctx, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // 通知バーの内容
        Notification n = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("出席を取りました。")
                .setWhen(System.currentTimeMillis())    // 時間
                .setContentTitle("BeaaconAttend")
                .setContentText(Text)
                .setContentIntent(contentIntent)// インテント
                .build();
        n.flags = type;

        mgr.notify(id, n);

    }

}

