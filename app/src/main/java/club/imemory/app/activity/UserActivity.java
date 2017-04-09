package club.imemory.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import club.imemory.app.R;

/**
 * @Author: 张杭
 * @Date: 2017/3/9 23:07
 */

public class UserActivity extends BaseActivity {

    /**
     * 启动UserActivity
     */
    public static void actionStart(Context context, String... strings) {
        Intent intent = new Intent(context, UserActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }


}
