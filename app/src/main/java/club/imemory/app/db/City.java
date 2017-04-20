package club.imemory.app.db;

import com.alibaba.fastjson.annotation.JSONField;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 城市
 *
 * @Author: 张杭
 * @Date: 2017/3/27 17:23
 */

public class City extends DataSupport implements Serializable {

    @JSONField(name = "name")
    private String cityName;
    @JSONField(name = "id")
    private int cityCode;
    private int provinceCode;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
