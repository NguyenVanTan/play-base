package models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the s_user_roles database table.
 * 
 */
@Entity
@Table(name="s_user_roles")
@NamedQuery(name="SUserRole.findAll", query="SELECT s FROM SUserRole s")
public class SUserRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SUserRolePK id;

	public SUserRole() {
	}

	public SUserRolePK getId() {
		return this.id;
	}

	public void setId(SUserRolePK id) {
		this.id = id;
	}

}