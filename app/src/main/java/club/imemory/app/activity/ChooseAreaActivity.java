package club.imemory.app.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import club.imemory.app.R;
import club.imemory.app.fragment.ChooseAreaFragment;

/**
 * @Author: 张杭
 * @Date: 2017/3/29 22:41
 */

public class ChooseAreaActivity extends BaseActivity {

    /**
     * 启动ChooseAreaActivity
     */
    public static void actionStart(Context context, String... strings) {
        Intent intent = new Intent(context, ChooseAreaActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);
        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("weather", null) != null) {
            WeatherActivity.actionStart(ChooseAreaActivity.this);
            finish();
        }*/
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.choose_area__frame, ChooseAreaFragment.instanceFragment()).commit();
    }

}
