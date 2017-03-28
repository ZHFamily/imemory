package club.imemory.app.json;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 当前天气信息
 *
 * @Author: 张杭
 * @Date: 2017/3/9 23:16
 */

public class Now implements Serializable {

    @JSONField(name="tmp")
    public String temperature;

    @JSONField(name="cond")
    public More more;

    public class More {

        @JSONField(name="txt")
        public String info;

    }

}
