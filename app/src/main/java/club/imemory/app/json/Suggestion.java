package club.imemory.app.json;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 天气 建议信息
 *
 * @Author: 张杭
 * @Date: 2017/3/9 23:17
 */

public class Suggestion implements Serializable {

    @JSONField(name="comf")
    public Comfort comfort;

    @JSONField(name="cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort {

        @JSONField(name="txt")
        public String info;

    }

    public class CarWash {

        @JSONField(name="txt")
        public String info;

    }

    public class Sport {

        @JSONField(name="txt")
        public String info;

    }

}
