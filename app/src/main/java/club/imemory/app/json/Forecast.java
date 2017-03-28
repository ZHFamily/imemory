package club.imemory.app.json;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 天气预报
 *
 * @Author: 张杭
 * @Date: 2017/3/9 23:15
 */

public class Forecast implements Serializable {

    public String date;

    @JSONField(name="tmp")
    public Temperature temperature;

    @JSONField(name="cond")
    public More more;

    public class Temperature {

        public String max;

        public String min;

    }

    public class More {

        @JSONField(name="txt_d")
        public String info;

    }

}
