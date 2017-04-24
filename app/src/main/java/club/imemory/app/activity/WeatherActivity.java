package club.imemory.app.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;

import club.imemory.app.R;
import club.imemory.app.entity.Forecast;
import club.imemory.app.entity.Weather;
import club.imemory.app.http.HttpManager;
import club.imemory.app.json.JsonAnalyze;
import club.imemory.app.service.AutoUpdateWeatherService;
import club.imemory.app.util.AppManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @Author: 张杭
 * @Date: 2017/3/9 23:07
 */

public class WeatherActivity extends BaseActivity {

    /**
     * 启动WeatherActivity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, WeatherActivity.class);
        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
    }

    private static final String WEATHER_INFO_URL = "http://guolin.tech/api/weather?cityid=";
    private static final String WEATHER_INFO_KEY = "&key=bc0418b57b2d4918819d3974ac1285d9";
    private static final String GET_BingPic = "http://guolin.tech/api/bing_pic";
    private SwipeRefreshLayout swipeRefresh;
    private ScrollView weatherLayout;
    private TextView mTitleAreaTv;
    private TextView mUpdateTimeTv;
    private TextView mDegreeTv;
    private TextView mWeatherInfoTv;
    private LinearLayout forecastLayout;
    private TextView mAQItv;
    private TextView mPM25tv;
    private TextView mComfortTv;
    private TextView mCarWashTv;
    private TextView mSportTv;
    private ImageView mBingPicImg;
    private String mWeatherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        // 初始化各控件
        mBingPicImg = (ImageView) findViewById(R.id.img_bing_pic);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        mTitleAreaTv = (TextView) findViewById(R.id.tv_topbar_title);
        mUpdateTimeTv = (TextView) findViewById(R.id.tv_update_time);
        mDegreeTv = (TextView) findViewById(R.id.tv_degree);
        mWeatherInfoTv = (TextView) findViewById(R.id.tv_weather_info);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        mAQItv = (TextView) findViewById(R.id.tv_aqi);
        mPM25tv = (TextView) findViewById(R.id.tv_pm25);
        mComfortTv = (TextView) findViewById(R.id.tv_comfort);
        mCarWashTv = (TextView) findViewById(R.id.tv_car_wash);
        mSportTv = (TextView) findViewById(R.id.tv_sport);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        ImageButton mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setImageResource(R.drawable.ic_arrow_back);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageButton mRestsBtn = (ImageButton) findViewById(R.id.btn_rests);
        mRestsBtn.setImageResource(R.drawable.ic_weather);
        mRestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, ChooseAreaActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        mWeatherId = getIntent().getStringExtra("weatherId");
        if (mWeatherId != null) {
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String weatherString = prefs.getString("weatherInfor", null);
            if (weatherString != null) {
                // 有缓存时直接解析天气数据
                Weather weather = JsonAnalyze.handleWeatherResponse(weatherString);
                if (weather != null) {
                    mWeatherId = weather.basic.weatherId;
                    showWeatherInfo(weather);
                }
            }
        }

        loadBingPic();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                mWeatherId = data.getStringExtra("weatherId");
                requestWeather(mWeatherId);
            }
        }
    }

    /**
     * 根据天气id请求城市天气信息。
     */
    public void requestWeather(String weatherId) {
        String weatherUrl = WEATHER_INFO_URL + weatherId + WEATHER_INFO_KEY;
        HttpManager.sendOKHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = JsonAnalyze.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            AppManager.showToast("获取天气信息失败");
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AppManager.showToast("获取天气信息失败");
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {

        HttpManager.sendOKHttpRequest(GET_BingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(mBingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 处理并展示Weather实体类中的数据。
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime;
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        mTitleAreaTv.setText(cityName);
        mUpdateTimeTv.setText("更新于：" + updateTime);
        mDegreeTv.setText(degree);
        mWeatherInfoTv.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.weather_forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.tv_date);
            TextView infoText = (TextView) view.findViewById(R.id.tv_info);
            TextView maxText = (TextView) view.findViewById(R.id.tv_temperature_max);
            TextView minText = (TextView) view.findViewById(R.id.tv_temperature_min);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            mAQItv.setText(weather.aqi.city.aqi);
            mPM25tv.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运行建议：" + weather.suggestion.sport.info;
        mComfortTv.setText(comfort);
        mCarWashTv.setText(carWash);
        mSportTv.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateWeatherService.class);
        startService(intent);
    }

}
