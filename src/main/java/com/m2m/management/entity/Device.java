package com.m2m.management.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
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
@Table(name = "device", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = "device_id"))
//@Table(name = "device", schema = "public")
public class Device implements java.io.Serializable {

	private long did;
	private String deviceId;
	private String name;
	private String os;
	private String arch;
	private String description;
	private Boolean isOnline;
	private Long gid;
	private Long ts;
	private DeviceGroup deviceGroup;

	private Device() {
	}

	private Device(long did) {
		this.did = did;
	}
	
	public Device(String deviceId, String os, String arch, Boolean isOnline) {
		this.deviceId = deviceId;
		this.os = os;
		this.arch = arch;
		this.isOnline = isOnline;
	}
	
	private Device(long did, String deviceId, String name, String os, String arch, String description, Boolean isOnline,
			Long gid, Long ts) {
		this.did = did;
		this.deviceId = deviceId;
		this.name = name;
		this.os = os;
		this.arch = arch;
		this.description = description;
		this.isOnline = isOnline;
		this.gid = gid;
		this.ts = ts;
	}

	@Id
	@SequenceGenerator(name = "device_did_seq", allocationSize = 1, initialValue = 1, sequenceName = "device_did_seq")  
	@GeneratedValue(generator = "device_did_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "did", unique = true, nullable = false)
	public long getDid() {
		return this.did;
	}

	private void setDid(long did) {
		this.did = did;
	}

	@Column(name = "device_id", unique = true, length = 256)
	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Column(name = "name", length = 256)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "os", length = 32)
	public String getOs() {
		return this.os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	@Column(name = "arch", length = 32)
	public String getArch() {
		return this.arch;
	}

	public void setArch(String arch) {
		this.arch = arch;
	}

	@Column(name = "description", length = 256)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "is_online")
	public Boolean getIsOnline() {
		return this.isOnline;
	}

	public void setIsOnline(Boolean isOnline) {
		this.isOnline = isOnline;
	}

	@Column(name = "gid")
	public Long getGid() {
		return this.gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}

	@Column(name = "ts")
	public Long getTs() {
		return this.ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="group_id") 
	public DeviceGroup getDeviceGroup() { 
    	return this.deviceGroup; 
	}
	public void setDeviceGroup(DeviceGroup group){
		this.deviceGroup = group;
	}
}
