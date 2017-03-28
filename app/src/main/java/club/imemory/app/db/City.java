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

    @JSONField(serialize=false)
    private int id;
    @JSONField(name="name")
    private String cityName;
    @JSONField(name="id")
    private int cityCode;
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
