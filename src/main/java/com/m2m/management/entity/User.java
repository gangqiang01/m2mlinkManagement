package com.m2m.management.entity;

import com.auth0.jwt.internal.com.fasterxml.jackson.annotation.JsonIgnore;
import com.auth0.jwt.internal.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.auth0.jwt.internal.com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "user", schema = "public")
public class User implements java.io.Serializable {
	@Id
	@SequenceGenerator(name = "user_uid_seq", allocationSize = 1, initialValue = 1, sequenceName = "user_uid_seq")
	@GeneratedValue(generator = "user_uid_seq", strategy = GenerationType.SEQUENCE)
	@Column(unique = true, nullable = false)
	private long uid;

	@Column(unique = true, length = 256)
	private String name;

	private String passwd;

	private Long ts;

	@OneToMany(mappedBy = "user", cascade={CascadeType.REMOVE})
	private Set<Repo> repo = new HashSet<Repo>(0);

	private User() {
	}

	private User(long uid) {
		this.uid = uid;
	}
	
	public User(String name, String passwd) {
		this.name = name;
		this.passwd = passwd;
	}


	public long getUid() {
		return this.uid;
	}

	private void setUid(long uid) {
		this.uid = uid;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}


	public Long getTs() {
		return this.ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}


//	public Set<Repo> getRepo() {
//		return this.repo;
//	}
	public void setRepo(Set<Repo> repo) {
		this.repo = repo;
	}


}
