package models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the s_roles database table.
 * 
 */
@Entity
@Table(name="s_roles")
@NamedQuery(name="SRole.findAll", query="SELECT s FROM SRole s")
public class SRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="role_id")
	private int roleId;

	@Column(name="role_desc")
	private String roleDesc;

	@Column(name="role_name")
	private String roleName;

	public SRole() {
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleDesc() {
		return this.roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}