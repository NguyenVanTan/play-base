package models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the s_user_logs database table.
 * 
 */
@Entity
@Table(name="s_user_logs")
@NamedQuery(name="SUserLog.findAll", query="SELECT s FROM SUserLog s")
public class SUserLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String action;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="login_time")
	private Date loginTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="logout_time")
	private Date logoutTime;

	@Column(name="user_id")
	private int userId;

	public SUserLog() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLogoutTime() {
		return this.logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}