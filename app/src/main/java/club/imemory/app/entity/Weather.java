package club.imemory.app.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: 张杭
 * @Date: 2017/3/9 23:12
 */

public class Weather implements Serializable {

    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @JSONField(name="daily_forecast")
    public List<Forecast> forecastList;

}
