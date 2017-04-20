package club.imemory.app.db;

import com.alibaba.fastjson.annotation.JSONField;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 市/镇/区
 *
 * @Author: 张杭
 * @Date: 2017/3/27 17:24
 */

public class County extends DataSupport implements Serializable {

    @JSONField(name = "name")
    private String countyName;
    @JSONField(name = "weather_id")
    private String weatherId;
    private int cityCode;

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }
}
