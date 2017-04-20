package club.imemory.app.db;

import com.alibaba.fastjson.annotation.JSONField;

import org.litepal.crud.DataSupport;

/**
 * 省
 *
 * @Author: 张杭
 * @Date: 2017/3/27 17:20
 */

public class Province extends DataSupport {

    @JSONField(name = "name")
    private String provinceName;
    @JSONField(name = "id")
    private int provinceCode;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
