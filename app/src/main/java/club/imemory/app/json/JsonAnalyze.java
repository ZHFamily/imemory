package club.imemory.app.json;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import club.imemory.app.db.City;
import club.imemory.app.db.County;
import club.imemory.app.db.Province;
import club.imemory.app.db.User;
import club.imemory.app.entity.Weather;

/**
 * JSON 数据解析
 *
 * @Author: 张杭
 * @Date: 2017/3/27 18:49
 */

public class JsonAnalyze {

    /**
     * 解析并保存服务器返回的省级数据
     *
     * @param response
     * @return
     */
    public static boolean handleProvinceResponse(String response) {
        if (TextUtils.isEmpty(response)) {
            return false;
        } else {
            List<Province> provinceList = JSON.parseArray(response, Province.class);
            for (Province province : provinceList) {
                province.save();
            }
            return true;
        }
    }

    /**
     * 解析并保存服务器返回的市级数据
     *
     * @param response
     * @param provinceCode
     * @return
     */
    public static boolean handleCityResponse(String response, int provinceCode) {
        if (TextUtils.isEmpty(response)) {
            return false;
        } else {
            List<City> cityList = JSON.parseArray(response, City.class);
            for (City city : cityList) {
                city.setProvinceCode(provinceCode);
                city.save();
            }
            return true;
        }
    }

    /**
     * 解析并保存服务器返回的县级数据
     *
     * @param response
     * @param cityCode
     * @return
     */
    public static boolean handleCountyResponse(String response, int cityCode) {
        if (TextUtils.isEmpty(response)) {
            return false;
        } else {
            List<County> CountyList = JSON.parseArray(response, County.class);
            for (County county : CountyList) {
                county.setCityCode(cityCode);
                county.save();
            }
            return true;
        }
    }

    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = JSON.parseObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String string = jsonArray.get(0).toString();
            Weather weather = JSON.parseObject(string,Weather.class);
            return weather;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将返回的JSON数据解析成User实体类
     */
    public static boolean handleUserResponse(String response) {
        if (TextUtils.isEmpty(response)) {
            return false;
        } else {
            User user = JSON.parseObject(response, User.class);
            user.save();
            return true;
        }
    }
}
