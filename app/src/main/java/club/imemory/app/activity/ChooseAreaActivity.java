package club.imemory.app.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;

import club.imemory.app.R;
import club.imemory.app.fragment.ChooseAreaFragment;
import club.imemory.app.util.AppManager;

/**
 * @Author: 张杭
 * @Date: 2017/3/29 22:41
 */

public class ChooseAreaActivity extends BaseActivity {

    // 首次按下返回键时间戳
    private long firstBackPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("weather", null) != null) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.choose_area__frame, ChooseAreaFragment.instanceFragment()).commit();
    }

    public void gotoWeather(String weatherId) {
        Intent intent = new Intent();
        intent.putExtra("weather_id", weatherId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        long secondBackPressedTime = System.currentTimeMillis();
        if (secondBackPressedTime - firstBackPressedTime > 2000) {
            AppManager.showToast("再按一次将回到主页");
            firstBackPressedTime = secondBackPressedTime;
        } else {
            Intent intent = new Intent(ChooseAreaActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
