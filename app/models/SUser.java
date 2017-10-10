package models;

import play.data.validation.Constraints;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the s_user database table.
 * 
 */
@Entity
@Table(name="s_user")
@NamedQuery(name="SUser.findAll", query="SELECT s FROM SUser s")
public class SUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(name="creation_by")
	private String creationBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="creation_date")
	private Date creationDate;

	private String description;

	@Constraints.Required
	@Constraints.Email
	private String email;

	private int gender;

	private String mobile;

	@Constraints.Required
	private String name;

	@Constraints.Required
	private String password;

	private int type;

	public SUser() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreationBy() {
		return this.creationBy;
	}

	public void setCreationBy(String creationBy) {
		this.creationBy = creationBy;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getGender() {
		return this.gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

}