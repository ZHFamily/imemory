package club.imemory.app.entity;

import java.io.Serializable;

/**
 * 空气质量指数
 *
 * @Author: 张杭
 * @Date: 2017/3/9 23:07
 */

public class AQI implements Serializable {

    public AQICity city;

    public class AQICity {

        public String aqi;

        public String pm25;

    }

}
