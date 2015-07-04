package jp.morimotor.beaconattend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class HelpFragment extends android.support.v4.app.Fragment {

    private int mImage;
    private String mTitle;
    private String mText;

    public static HelpFragment newInstance(int img, String title, String text) {
        HelpFragment fragment = new HelpFragment();

        fragment.mImage = img;
        fragment.mTitle = title;
        fragment.mText = text;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        ImageView iv = (ImageView) view.findViewById(R.id.helpImageView);
        TextView tv1 = (TextView) view.findViewById(R.id.helpTitle);
        TextView tv2 = (TextView) view.findViewById(R.id.helpText);

        iv.setImageResource(mImage);
        tv1.setText(mTitle);
        tv2.setText(mText);

        return view;
    }
}

