package club.imemory.app.entity;

import java.util.Date;

/**
 * @Author: 张杭
 * @Date: 2017/3/31 21:52
 */

public class Find {

    private int id;
    private int userId;
    private String userName;
    private String userHead;
    private String title;
    private String subhead;
    private String avatar;
    private int top;
    private int hits;
    private Date createtime;
    private Date updatetime;

    public Find(){}

    public Find(String userName, String userHead, String title, String subhead, String avatar, int hits, Date createtime) {
        this.userName = userName;
        this.userHead = userHead;
        this.title = title;
        this.subhead = subhead;
        this.avatar = avatar;
        this.hits = hits;
        this.createtime = createtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubhead() {
        return subhead;
    }

    public void setSubhead(String subhead) {
        this.subhead = subhead;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
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
