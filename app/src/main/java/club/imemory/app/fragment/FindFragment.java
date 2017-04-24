package club.imemory.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import club.imemory.app.R;
import club.imemory.app.activity.MainActivity;
import club.imemory.app.adapter.SectionsPagerAdapter;

/**
 * @Author: 张杭
 * @Date: 2017/3/29 12:05
 */

public class FindFragment extends Fragment {

    private static FindFragment mFindFragment = null;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;


    /**
     * 实例化FindFragment
     */
    public static FindFragment instanceFragment() {
        if (mFindFragment == null) {
            mFindFragment = new FindFragment();
        }
        return mFindFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        MainActivity activity = (MainActivity) getActivity();
        mSectionsPagerAdapter = new SectionsPagerAdapter(activity.getMySupportFragmentManager());

        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }


}
