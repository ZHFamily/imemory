package club.imemory.app.entity;

import java.io.Serializable;

/**
 * @Author: 张杭
 * @Date: 2017/4/21 15:52
 */

public class Meizi implements Serializable {
    
    private String url;
    private String who;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }
}
