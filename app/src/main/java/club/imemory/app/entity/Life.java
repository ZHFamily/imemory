package club.imemory.app.entity;

import java.util.Date;

/**
 * Life entity. @author MyEclipse Persistence Tools
 */

public class Life implements java.io.Serializable {

	private Integer id;
	private Integer userId;
	private String title;
	private String subhead;
	private String avatar;
	private Integer top;
	private Date createtime;
	private Date updatetime;

	/** minimal constructor */
	public Life(Integer userId, String title, Date createtime) {
		this.userId = userId;
		this.title = title;
		this.createtime = createtime;
	}

	/** full constructor */
	public Life(Integer userId, String title, String subhead, String avatar,
			Integer top, Date createtime, Date updatetime) {
		this.userId = userId;
		this.title = title;
		this.subhead = subhead;
		this.avatar = avatar;
		this.top = top;
		this.createtime = createtime;
		this.updatetime = updatetime;
	}

	@Override
	public String toString() {
		return "Life [id=" + id + ", userId=" + userId + ", title=" + title
				+ ", subhead=" + subhead + ", avatar=" + avatar + ", top="
				+ top + ", createtime=" + createtime + ", updatetime="
				+ updatetime + "]";
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubhead() {
		return this.subhead;
	}

	public void setSubhead(String subhead) {
		this.subhead = subhead;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Integer getTop() {
		return this.top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

}