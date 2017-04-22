package club.imemory.app.db;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * @Author: 张杭
 * @Date: 2017/4/1 13:06
 */

public class Message extends DataSupport implements java.io.Serializable{

    private int id;
    private int code;
    private String text;
    private String avatar;
    private Date createtime;
    private Date updatetime;

    public Message() {
    }

    public Message(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}
