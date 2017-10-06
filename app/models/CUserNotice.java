package models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the c_user_notice database table.
 * 
 */
@Entity
@Table(name="c_user_notice")
@NamedQuery(name="CUserNotice.findAll", query="SELECT c FROM CUserNotice c")
public class CUserNotice implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CUserNoticePK id;

	private int status;

	public CUserNotice() {
	}

	public CUserNoticePK getId() {
		return this.id;
	}

	public void setId(CUserNoticePK id) {
		this.id = id;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}