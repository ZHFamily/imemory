package club.imemory.app.activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import club.imemory.app.R;
import club.imemory.app.adapter.LifeAdapter;
import club.imemory.app.base.BaseActivity;
import club.imemory.app.bean.Life;
import club.imemory.app.util.AppManager;

import static club.imemory.app.util.AppManager.APP_NAME;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;
    private ImageView mImageHead;
    private LinearLayout mHeaderLayout;
    private LinearLayout mUserInfoLayout;
    private List<Life> mLifeList = new ArrayList<>();
    // 首次按下返回键时间戳
    private long firstBackPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppManager.logI("MainActivity", "onCreate");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //悬浮按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mHeaderLayout = (LinearLayout) navigationView.getHeaderView(0);
        mUserInfoLayout = (LinearLayout) mHeaderLayout.findViewById(R.id.layout_user_info);
        mUserInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.actionStart(MainActivity.this, "");
            }
        });

        //主要内容
        initLife();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        LifeAdapter adapter = new LifeAdapter(mLifeList);
        recyclerView.setAdapter(adapter);
    }

    private void initLife() {
        for (int i = 0; i < 20; i++) {
            Life life = new Life();
            life.setTitle("年青正好");
            life.setSubhead("在偏执的道路上越走越远");
            life.setCreatetime("20170325");
            mLifeList.add(life);
        }
    }

    /**
     * 点击返回键
     */
    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            long secondBackPressedTime = System.currentTimeMillis();
            if (secondBackPressedTime - firstBackPressedTime > 2000) {
                AppManager.showToast("再按一次退出");
                firstBackPressedTime = secondBackPressedTime;
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_life:
                AppManager.showToast("记录生活从点滴开始");
                FullscreenActivity.actionStart(MainActivity.this, "");
                break;
            case R.id.item_scan:
                AppManager.showToast("进入扫描，请做好准备");
                new IntentIntegrator(this)
                        .setCaptureActivity(ScanActivity.class)
                        .setOrientationLocked(false)
                        .setPrompt("abc")
                        .initiateScan(); // 初始化扫描
                break;
            case R.id.item_settings:
                AppManager.showToast("设置让你与众不同");
                SettingsActivity.actionStart(MainActivity.this, "");
                break;
            case R.id.item_close:
                AppManager.showToast("欢迎下次光临");
                AppManager.finishAll();
                break;
            default:
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_my:
                AppManager.showToast("美好生活");
                break;
            case R.id.nav_partake:
                AppManager.showToast("分享会是你最大的快乐");
                break;
            case R.id.nav_news:
                AppManager.showToast("沟通才能有机会");
                break;
            case R.id.nav_setting:
                AppManager.showToast("设置你的天堂");
                SettingsActivity.actionStart(MainActivity.this, "");
                break;
            case R.id.nav_about:
                AppManager.showToast("想进一步了解我么");
                break;
            case R.id.nav_recommend:
                AppManager.showToast("和朋友一起玩耍吧");
                break;
            default:
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 接收扫一扫返回数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                AppManager.showToast("内容为空");
            } else {
                AppManager.showToast("扫描成功");
                scanDialog(intentResult.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 扫一扫弹框显示数据
     */
    private void scanDialog(final String result) {
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        clipboardManager.setPrimaryClip(ClipData.newPlainText(APP_NAME, result));
                        AppManager.showToast("已复制到粘贴板");
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage(result); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("复制内容", onClickListener);
        builder.setNegativeButton("取消", onClickListener);
        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppManager.logI("MainActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppManager.logI("MainActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppManager.logI("MainActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppManager.logI("MainActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.logI("MainActivity", "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AppManager.logI("MainActivity", "onRestart");
    }
}
