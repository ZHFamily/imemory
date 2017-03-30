package club.imemory.app.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import club.imemory.app.R;
import club.imemory.app.activity.MainActivity;
import club.imemory.app.activity.WeatherActivity;
import club.imemory.app.db.City;
import club.imemory.app.db.County;
import club.imemory.app.db.Province;
import club.imemory.app.db.WeatherDataAnalyze;
import club.imemory.app.http.HttpManager;
import club.imemory.app.util.AppManager;
import club.imemory.app.util.ApplicationUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static club.imemory.app.util.AppManager.GET_AREA;

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
