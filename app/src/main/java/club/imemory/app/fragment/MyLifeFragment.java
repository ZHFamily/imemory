package club.imemory.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Locale;

import club.imemory.app.R;

/**
 * @Author: 张杭
 * @Date: 2017/3/29 12:06
 */

public class MyLifeFragment extends Fragment {

    /**
     * 实例化MyLifeFragment
     * @return
     */
    public static MyLifeFragment instanceFragment(){
        return new MyLifeFragment();
    }

    public MyLifeFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_life, container, false);
    }


}
