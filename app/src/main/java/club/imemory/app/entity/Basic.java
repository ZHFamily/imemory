package club.imemory.app.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 天气基本信息
 *
 * @Author: 张杭
 * @Date: 2017/3/9 23:10
 */

public class Basic implements Serializable {

    @JSONField(name = "city")
    public String cityName;

    @JSONField(name = "id")
    public String weatherId;

    public Update update;

    public class Update {

        @JSONField(name = "loc")
        public String updateTime;

    }

}
