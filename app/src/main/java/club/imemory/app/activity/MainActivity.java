package club.imemory.app.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import club.imemory.app.R;
import club.imemory.app.db.User;
import club.imemory.app.fragment.FindFragment;
import club.imemory.app.fragment.MessageFragment;
import club.imemory.app.fragment.MyLifeFragment;
import club.imemory.app.util.AppManager;
import club.imemory.app.util.AppUtils;
import de.hdodenhof.circleimageview.CircleImageView;

import static club.imemory.app.util.AppManager.APP_NAME;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private MyLifeFragment mMyLifeFragment;
    private FindFragment mFindFragment;
    private MessageFragment mMessageFragment;
    private CircleImageView mHeadImage;
    private TextView mNameTv;
    private TextView mPersonalityTv;
    private ImageView mDegreeImage;
    private TextView mAreaTv;
    private User user;

    /**
     * 记录当前显示的Fragment
     */
    private int currentFragment = 0;
    private static final int SHOW_LIFE = 1;
    private static final int SHOW_FIND = 2;
    private static final int SHOW_MESSAGE = 3;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    // 首次按下返回键时间戳
    private long firstBackPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        navigationView.getMenu().findItem(R.id.nav_my).setChecked(true);
        View headerView = navigationView.getHeaderView(0);
        mHeadImage = (CircleImageView) headerView.findViewById(R.id.image_head);
        mPersonalityTv = (TextView) headerView.findViewById(R.id.tv_user_personality);
        mNameTv = (TextView) headerView.findViewById(R.id.tv_user_name);
        mDegreeImage = (ImageView) headerView.findViewById(R.id.tv_degree);
        mAreaTv = (TextView) headerView.findViewById(R.id.tv_area);
        //点击头像
        headerView.findViewById(R.id.layout_user_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    UserActivity.actionStart(MainActivity.this, user);
                } else {
                    LoginActivity.actionStart(MainActivity.this);
                }
            }
        });
        //点击二维码
        headerView.findViewById(R.id.btn_two_dimension_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    ImageView imgView = getQrCodeView(user.getPhone());
                    builder.setTitle("我的二维码");
                    builder.setView(imgView);
                    final AlertDialog dialog = builder.show();
                    imgView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    LoginActivity.actionStart(MainActivity.this);
                }
            }
        });
        //点击天气
        headerView.findViewById(R.id.btn_weather).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(intent);
            }
        });

        requestsForPermissions();

        fragmentManager = getFragmentManager();
        // 从savedInstanceState中恢复数据, 如果没有数据需要恢复savedInstanceState为null
        if (savedInstanceState != null) {
            restore(savedInstanceState.getInt("current"));
        } else {
            //没有数据可以还原，初始化主显示界面
            if (currentFragment == 0) {
                addOrShowFragment(SHOW_LIFE);
            } else {
                addOrShowFragment(currentFragment);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = DataSupport.findLast(User.class);
        if (user != null) {
            mNameTv.setText(user.getName());
            mPersonalityTv.setText(user.getPersonality());
            if (user.getHead() != null) {
                Glide.with(this).load(user.getHead()).into(mHeadImage);
            }
        }
    }

    /**
     * 还原数据
     */
    private void restore(int current) {
        //当Activity发生重建时还原Fragment
        mMyLifeFragment = (MyLifeFragment) fragmentManager.findFragmentByTag("MyLifeFragment");
        mFindFragment = (FindFragment) fragmentManager.findFragmentByTag("FindFragment");
        mMessageFragment = (MessageFragment) fragmentManager.findFragmentByTag("MessageFragment");
        if (mMessageFragment != null) {
            fragmentManager.beginTransaction().hide(mMessageFragment).commit();
        }
        if (mFindFragment != null) {
            fragmentManager.beginTransaction().hide(mFindFragment).commit();
        }
        if (mMyLifeFragment != null) {
            //说明Activity发生了重建，设置之前保存的currentFragment
            fragmentManager.beginTransaction().hide(mMyLifeFragment).commit();
            addOrShowFragment(current);
        }
    }

    /**
     * 根据定位信息显示当前地区
     */
    private void showWeather() {
        //声明定位回调监听器
        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        mAreaTv.setText(aMapLocation.getCity());
                    } else {
                        AppManager.showToast(aMapLocation.getErrorCode() + ":" + aMapLocation.getErrorInfo());
                        AppManager.logE("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                    }
                }
            }
        };

        //声明AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = null;
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //获取一次定位结果
        mLocationOption.setOnceLocation(true);

        //声明AMapLocationClient类对象  初始化
        AMapLocationClient mLocationClient = new AMapLocationClient(getApplicationContext());
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 权限申请
     */
    private void requestsForPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            showWeather();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        AppManager.showToast("必须同意所有权限才能正常运行程序");
                        finish();
                        return;
                    }
                }
                showWeather();
            } else {
                AppManager.showToast("发生未知错误,我感到非常抱歉");
                finish();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_my:
                mToolbar.setTitle("生活");
                addOrShowFragment(SHOW_LIFE);
                break;
            case R.id.nav_find:
                mToolbar.setTitle("发现");
                addOrShowFragment(SHOW_FIND);
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
                // TODO  上线时请修改
                //DataCleanManager.cleanApplicationData(this,CRASH_LOG_PATH);
                AppManager.showToast("和朋友一起玩耍吧");
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addOrShowFragment(int index) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (currentFragment == index) {
            return;
        }
        //将上次显示的fragment隐藏
        switch (currentFragment) {
            case SHOW_LIFE:
                fragmentTransaction.hide(mMyLifeFragment);
                break;
            case SHOW_FIND:
                fragmentTransaction.hide(mFindFragment);
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
                    fragmentTransaction.add(R.id.content_frame, mMyLifeFragment, "MyLifeFragment");
                } else {
                    fragmentTransaction.show(mMyLifeFragment);
                }
                break;
            case SHOW_FIND:
                if (mFindFragment == null) {
                    mFindFragment = FindFragment.instanceFragment();
                    fragmentTransaction.add(R.id.content_frame, mFindFragment, "FindFragment");
                } else {
                    fragmentTransaction.show(mFindFragment);
                }
                break;
            case SHOW_MESSAGE:
                if (mMessageFragment == null) {
                    mMessageFragment = MessageFragment.instanceFragment();
                    fragmentTransaction.add(R.id.content_frame, mMessageFragment, "MessageFragment");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage(result); //设置内容
        builder.setIcon(R.mipmap.logo);//设置图标，图片id即可
        builder.setPositiveButton("复制内容", onClickListener);
        builder.setNegativeButton("取消", onClickListener);
        builder.create().show();
    }

    /**
     * 返回产生的二维码view
     *
     * @return
     */
    private ImageView getQrCodeView(String s) {
        ImageView imgView = new ImageView(this);
        imgView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        imgView.setImageBitmap(AppUtils.getEncodeBitmap(s));
        return imgView;
    }


    /**
     * 在Activity销毁时保存当前显示的Fragment
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("current", currentFragment);
        super.onSaveInstanceState(outState);
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
