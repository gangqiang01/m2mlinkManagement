package com.m2m.management.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "devicegroup", schema = "public")
public class DeviceGroup implements java.io.Serializable {

	private long gid;
	private String name;
	private User user;
	private Set<Device> devices = new HashSet<Device>(0);
	private DeviceGroup() {
	}

	private DeviceGroup(long gid) {
		this.gid = gid;
	}

	public DeviceGroup(String name) {
		this.name = name;
	}
	public DeviceGroup(Long gid, String name) {
		this.gid = gid;
        this.name = name;
    }

	@Id
	@SequenceGenerator(name = "devicegroup_gid_seq", allocationSize = 1, initialValue = 1, sequenceName = "devicegroup_gid_seq")
	@GeneratedValue(generator = "devicegroup_gid_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "gid", unique = true, nullable = false)
	public long getGid() {
		return this.gid;
	}

	private void setGid(long gid) {
		this.gid = gid;
	}

	
	@Column(name = "name", unique = true, length = 256)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deviceGroup")
    public Set<Device> getDevices() {
        return this.devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

}
