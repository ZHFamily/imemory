package club.imemory.app.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import club.imemory.app.R;

/**
 * @Author: 张杭
 * @Date: 2017/4/18 20:10
 */

public class LaunchActivity extends BaseActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this, 800);
    }

    @Override
    public void run() {
        Boolean isAlive = this != null && !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && this.isDestroyed()) && !this.isFinishing();
        if (isAlive) {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

}
