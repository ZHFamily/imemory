package club.imemory.app.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.MyLocationStyle;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import club.imemory.app.R;
import club.imemory.app.base.BaseActivity;
import club.imemory.app.util.AppManager;

/**
 * @Author: 张杭
 * @Date: 2017/3/26 12:47
 */

public class MapActivity extends BaseActivity implements View.OnClickListener{

    /**
     * 启动MapActivity
     */
    public static void actionStart(Context context, String... strings) {
        Intent intent = new Intent(context, MapActivity.class);
        context.startActivity(intent);
    }

    private MapView mMapView = null;
    private AMap aMap;
    private UiSettings mUiSettings;
    private ImageButton mLocationBtn;
    private ImageButton mRefurbishBtn;
    private ImageButton mBackBtn;
    // 首次按下返回键时间戳
    private long firstBackPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mLocationBtn = (ImageButton) findViewById(R.id.btn_location);
        mRefurbishBtn = (ImageButton) findViewById(R.id.btn_refurbish);
        mBackBtn = (ImageButton) findViewById(R.id.btn_back);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        //aMap.setTrafficEnabled(true);// 显示实时交通状况
        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        //aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setCompassEnabled(true);//指南针
        mUiSettings.setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示
        mUiSettings.setScaleControlsEnabled(true);//比例尺控件

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_location:
                aMap.setTrafficEnabled(true);// 显示实时交通状况
                break;
            case R.id.btn_refurbish:
                //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
                break;
            case R.id.btn_back:
                this.finish();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 点击返回键
     */
    @Override
    public void onBackPressed() {
            long secondBackPressedTime = System.currentTimeMillis();
            if (secondBackPressedTime - firstBackPressedTime > 2000) {
                AppManager.showToast("再按一次退出地图");
                firstBackPressedTime = secondBackPressedTime;
            } else {
                super.onBackPressed();
            }
    }

    /**
     * 对地图进行截屏
     */
        /*aMap.getMapScreenShot(new AMap.OnMapScreenShotListener() {
        @Override
        public void onMapScreenShot(Bitmap bitmap) {
        }

        @Override
        public void onMapScreenShot(Bitmap bitmap, int status) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            if(null == bitmap){
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(
                        Environment.getExternalStorageDirectory() + "/test_"
                                + sdf.format(new Date()) + ".png");
                boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                try {
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                StringBuffer buffer = new StringBuffer();
                if (b)
                    buffer.append("截屏成功 ");
                else {
                    buffer.append("截屏失败 ");
                }
                if (status != 0)
                    buffer.append("地图渲染完成，截屏无网格");
                else {
                    buffer.append( "地图未渲染完成，截屏有网格");
                }
                AppManager.showToast(buffer.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    });*/
}
