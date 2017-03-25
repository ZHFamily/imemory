package club.imemory.app.bean;

/**
 * Life entity.
 */

public class Life implements java.io.Serializable {

	// Fields

	private Integer id;
	private String title;
	private String subhead;
	private byte[] avatar;
	private Integer top;
	private String createtime;
	private String updatetime;

	// Constructors

	/** default constructor */
	public Life() {
	}

	/** full constructor */
	public Life(String title, String subhead, byte[] avatar, Integer top,
			String createtime, String updatetime) {
		this.title = title;
		this.subhead = subhead;
		this.avatar = avatar;
		this.top = top;
		this.createtime = createtime;
		this.updatetime = updatetime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public byte[] getAvatar() {
		return this.avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public Integer getTop() {
		return this.top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public String getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

}