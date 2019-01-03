package com.m2m.management.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
@Entity
@Table(name = "user", schema = "public")
public class User implements java.io.Serializable {

	private long uid;
	private String name;
	private String passwd;
	private Long ts;

	private User() {
	}

	private User(long uid) {
		this.uid = uid;
	}
	
	public User(String name, String passwd) {
		this.name = name;
		this.passwd = passwd;
	}

	@Id
	@SequenceGenerator(name = "user_uid_seq", allocationSize = 1, initialValue = 1, sequenceName = "user_uid_seq")  
	@GeneratedValue(generator = "user_uid_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "uid", unique = true, nullable = false)
	public long getUid() {
		return this.uid;
	}

	private void setUid(long uid) {
		this.uid = uid;
	}

	@Column(name = "name", unique = true, length = 256)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "passwd", length = 256)
	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	@Column(name = "ts")
	public Long getTs() {
		return this.ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}

}
