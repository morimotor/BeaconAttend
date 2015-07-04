package jp.morimotor.beaconattend;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    protected static final int[] CONTENT0 = new int[] { R.drawable.top, R.drawable.kanri, R.drawable.beacon, R.drawable.database, 0, };
    protected static final String[] CONTENT1 = new String[] { "使用方法", "個人設定", "ビーコン登録", "データベース閲覧", "",};
    protected static final String[] CONTENT2 = new String[] {
            "このアプリはiBeaconを使って出席を自動ですることができるアプリです。個人設定とビーコン設定を行いBluetoothをONにしサービスを開始するだけで利用できます。サービスの開始を行っていればアプリを起動しなくても出席時にBluetoothがONになっていれば出席は自動で行われます。",
            "ここでは出席時の個人設定を行います。この情報はデータベースに送られるので正確に入力してください。UUIDは学校単位や学科単位など大きく分類し、細かくグルーピングする際はMajorとMinorの組み合わせで行ってください。",
            "Beaconは各先生ごとに持ってもらうことを想定していますのでビーコン登録では各先生ごとの識別子であるMajor値とMinor値を入力してください。この値の組み合わせでグルーピングも可能です。追加は＋ボタンから、削除はリストの長押しすることで削除できます。この値でBeaconを識別します。",
            "ここでは出席時に送信されたデータを見ることができます。自分の出席が確認できます",
            "",};

    private int mCount = CONTENT0.length;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return HelpFragment.newInstance(CONTENT0[position % CONTENT0.length], CONTENT1[position % CONTENT1.length], CONTENT2[position % CONTENT2.length]);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ViewPagerAdapter.CONTENT1[position % CONTENT1.length];
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}