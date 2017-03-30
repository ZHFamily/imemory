package club.imemory.app.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import club.imemory.app.json.Weather;
import club.imemory.app.util.StringUtils;

/**
 * 将服务器获取的数据进行解析处理
 *
 * @Author: 张杭
 * @Date: 2017/3/27 18:49
 */

public class WeatherDataAnalyze {

    /**
     * 解析并保存服务器返回的省级数据
     *
     * @param response
     * @return
     */
    public static boolean handleProvinceResponse(String response) {
        if (StringUtils.isEmpty(response)) {
            return false;
        } else {
            List<Province> provinceList = JSON.parseArray(response, Province.class);
            for (Province province : provinceList) {
                province.setProvinceCode(province.getId());
                province.save();
            }
            return true;
        }
    }

    /**
     * 解析并保存服务器返回的市级数据
     *
     * @param response
     * @param provinceId
     * @return
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (StringUtils.isEmpty(response)) {
            return false;
        } else {
            List<City> cityList = JSON.parseArray(response, City.class);
            for (City city : cityList) {
                city.setProvinceId(provinceId);
                city.save();
            }
            return true;
        }
    }

    /**
     * 解析并保存服务器返回的县级数据
     *
     * @param response
     * @param cityId
     * @return
     */
    public static boolean handleCountyResponse(String response, int cityId) {
        if (StringUtils.isEmpty(response)) {
            return false;
        } else {
            List<County> CountyList = JSON.parseArray(response, County.class);
            for (County county : CountyList) {
                county.setCityId(cityId);
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
}
