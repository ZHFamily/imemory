package club.imemory.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import club.imemory.app.R;

/**
 * @Author: 张杭
 * @Date: 2017/3/29 12:05
 */

public class NearShareFragment extends Fragment {

    /**
     * 实例化NearShareFragment
     * @return
     */
    public static NearShareFragment instanceFragment(){
        return new NearShareFragment();
    }

    public NearShareFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near_share, container, false);
        return view;
    }
}
