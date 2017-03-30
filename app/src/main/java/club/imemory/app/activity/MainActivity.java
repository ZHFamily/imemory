package club.imemory.app.activity;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import club.imemory.app.R;
import club.imemory.app.base.BaseActivity;
import club.imemory.app.fragment.MessageFragment;
import club.imemory.app.fragment.MyLifeFragment;
import club.imemory.app.fragment.NearShareFragment;
import club.imemory.app.other.zxing;
import club.imemory.app.util.AppManager;

import static club.imemory.app.util.AppManager.APP_NAME;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MyLifeFragment mMyLifeFragment;
    private NearShareFragment mNearShareFragment;
    private MessageFragment mMessageFragment;
    /**
     * 记录当前显示的Fragment
     */
    private int currentFragment = 0;
    private static final int SHOW_LIFE = 1;
    private static final int SHOW_NEARSHARE = 2;
    private static final int SHOW_MESSAGE = 3;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    // 首次按下返回键时间戳
    private long firstBackPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppManager.logI("MainActivity", "onCreate");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("生活");
        setSupportActionBar(mToolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        //点击头像
        headerView.findViewById(R.id.layout_user_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.actionStart(MainActivity.this);
            }
        });
        //点击二维码
        headerView.findViewById(R.id.btn_two_dimension_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                ImageView imgView = getQrCodeView();
                builder.setTitle("我的二维码");
                builder.setView(imgView);
                final AlertDialog dialog = builder.show();
                imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        //点击天气
        headerView.findViewById(R.id.btn_weather).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseAreaActivity.actionStart(MainActivity.this);
            }
        });

        //初始化主显示界面
        if(currentFragment==0){
            addOrShowFragment(SHOW_LIFE);
        }else{
            addOrShowFragment(currentFragment);
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_my:
                mToolbar.setTitle("生活");
                addOrShowFragment(SHOW_LIFE);
                break;
            case R.id.nav_share:
                mToolbar.setTitle("附近分享");
                addOrShowFragment(SHOW_NEARSHARE);
                AppManager.showToast("分享会是你最大的快乐");
                break;
            case R.id.nav_message:
                mToolbar.setTitle("消息");
                addOrShowFragment(SHOW_MESSAGE);
                AppManager.showToast("沟通才能有机会");
                break;
            case R.id.nav_setting:
                SettingsActivity.actionStart(MainActivity.this);
                break;
            case R.id.nav_about:
                AboutActivity.actionStart(MainActivity.this);
                AppManager.showToast("这里有我的联系方式");
                break;
            case R.id.nav_recommend:
                AppManager.showToast("和朋友一起玩耍吧");
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addOrShowFragment(int index) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (currentFragment==index){
            return;
        }
        //将上次显示的fragment隐藏
        switch (currentFragment){
            case SHOW_LIFE:
                fragmentTransaction.hide(mMyLifeFragment);
                break;
            case SHOW_NEARSHARE:
                fragmentTransaction.hide(mNearShareFragment);
                break;
            case SHOW_MESSAGE:
                fragmentTransaction.hide(mMessageFragment);
                break;
        }
        //显示选择的fragment
        switch (index) {
            case SHOW_LIFE:
                if (mMyLifeFragment == null) {
                    mMyLifeFragment = MyLifeFragment.instanceFragment();
                    fragmentTransaction.add(R.id.content_frame, mMyLifeFragment);
                } else {
                    fragmentTransaction.show(mMyLifeFragment);
                }
                break;
            case SHOW_NEARSHARE:
                if (mNearShareFragment == null) {
                    mNearShareFragment = NearShareFragment.instanceFragment();
                    fragmentTransaction.add(R.id.content_frame, mNearShareFragment);
                } else {
                    fragmentTransaction.show(mNearShareFragment);
                }
                break;
            case SHOW_MESSAGE:
                if (mMessageFragment == null) {
                    mMessageFragment = MessageFragment.instanceFragment();
                    fragmentTransaction.add(R.id.content_frame, mMessageFragment);
                } else {
                    fragmentTransaction.show(mMessageFragment);
                }
                break;
        }
        currentFragment = index;//记录当前显示的fragment
        fragmentTransaction.commit();//提交事务
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
                MapActivity.actionStart(MainActivity.this);
                break;
            case R.id.item_enjoy:
                AppManager.showToast("不一样的美");
                FullscreenActivity.actionStart(MainActivity.this);
                break;
            case R.id.item_scan:
                AppManager.showToast("扫描开始，请做好准备");
                new IntentIntegrator(this)
                        .setCaptureActivity(ScanActivity.class)
                        .setOrientationLocked(false)
                        .initiateScan(); // 初始化扫描
                break;
            case R.id.item_settings:
                AppManager.showToast("设置让你与众不同");
                SettingsActivity.actionStart(MainActivity.this);
                break;
            case R.id.item_close:
                AppManager.showToast("欢迎下次光临");
                AppManager.finishAll();
                break;
            default:
        }
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
        builder.setIcon(R.mipmap.logo);//设置图标，图片id即可
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

    /**
     * 返回产生的二维码view
     * @return
     */
    private ImageView getQrCodeView() {
        ImageView imgView = new ImageView(this);
        imgView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        imgView.setImageBitmap(new zxing().getEncodeBitmap("年青正好"));
        return imgView;
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
}
