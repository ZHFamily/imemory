package club.imemory.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: 张杭
 * @Date: 2017/3/30 22:11
 */

public class TimeUtils {

    /**
     * 返回24小时制时间字符串
     */
    public static String getDataToString(Date date) {
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdformat.format(date);
    }
}
