package models;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the c_user_notice database table.
 * 
 */
@Embeddable
public class CUserNoticePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="notice_id")
	private int noticeId;

	@Column(name="user_id")
	private int userId;

	public CUserNoticePK() {
	}
	public int getNoticeId() {
		return this.noticeId;
	}
	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
	}
	public int getUserId() {
		return this.userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CUserNoticePK)) {
			return false;
		}
		CUserNoticePK castOther = (CUserNoticePK)other;
		return 
			(this.noticeId == castOther.noticeId)
			&& (this.userId == castOther.userId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.noticeId;
		hash = hash * prime + this.userId;
		
		return hash;
	}
}