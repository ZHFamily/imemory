package club.imemory.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import club.imemory.app.R;

/**
 * @Author: 张杭
 * @Date: 2017/3/29 12:04
 */

public class MessageFragment extends Fragment {

    /**
     * 实例化MessageFragment
     * @return
     */
    public static MessageFragment instanceFragment(){
        return new MessageFragment();
    }

    public MessageFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }
}
