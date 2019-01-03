package com.m2m.management.entity;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

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
//import org.eclipse.leshan.LinkObject;
//import org.eclipse.leshan.core.request.BindingMode;

@Entity
@Table(name = "clientdevice", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = "endpoint"))
public class ClientDevice implements java.io.Serializable {

    /**
     * The LWM2M Client's unique end point name.
     */
    private long cdid;
    private String endpoint;
	private Boolean isOnline;
    private Date registrationDate;
    private InetAddress address;
    private int port;
    private InetSocketAddress registrationEndpointAddress;
    private long lifeTimeInSec;
    private String smsNumber;
    private String lwM2mVersion;
    private int bindingMode;
    private String registrationId;
    //private LinkObject[] objectLinks;
    private String  objectLinks;
    private String rootPath;
    private Date lastUpdate;
	private String organizationID;
    private String applicationID;
    private String name;

    public ClientDevice() {}


    public ClientDevice(String registrationId, String endpoint) {
        this.registrationId = registrationId;
        this.endpoint = endpoint;
    }
    public ClientDevice(String registrationId, String endpoint, long lifeTimeInSec, String smsNumber, String lwM2mVersion, int bindingMode, String objectLinks){
        this.registrationId =  registrationId;
        this.endpoint = endpoint;
        this.lifeTimeInSec = lifeTimeInSec;
        this.smsNumber = smsNumber;
        this.lwM2mVersion = lwM2mVersion;
        this.bindingMode = bindingMode;
        this.objectLinks = objectLinks;
    }

    @Id
    @SequenceGenerator(name = "clientdevice_cdid_seq", allocationSize = 1, initialValue = 1, sequenceName = "clientdevice_cdid_seq")
    @GeneratedValue(generator = "clientdevice_cdid_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "cdid", unique = true, nullable = false)
    public long getCdid() {
        return this.cdid;
    }
    private void setCdid(long cdid) {
        this.cdid = cdid;
    }

    @Column(name = "endpoint", unique = true)
    public String getEndpoint() {
        return this.endpoint;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

	@Column(name = "isonline")
    public Boolean getIsOnline() {
        return this.isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public Date getRegistrationDate() {
        return this.registrationDate;
    }
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public InetAddress getAddress() {
        return this.address;
    }
    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getPort() {
        return this.port;
    }
    public void setPort(int port){
        this.port = port;
    }

    public InetSocketAddress getRegistrationEndpointAddress() {
        return this.registrationEndpointAddress;
    }
    public void setRegistrationEndpointAddress(InetSocketAddress address) {
        this.registrationEndpointAddress = address;
    }

    public long getLifeTimeInSec() {
        return this.lifeTimeInSec;
    }
    public void setLifeTimeInSec(long sec){
        this.lifeTimeInSec = sec;
    }

    public String getSmsNumber() {
        return this.smsNumber;
    }
    public void setSmsNumber(String smsNumber){
        this.smsNumber = smsNumber;
    }

    public String getLwM2mVersion() {
        return this.lwM2mVersion;
    }
    public void setLwM2mVersion(String lwM2mVersion){
        this.lwM2mVersion = lwM2mVersion;
    }

    public int getBindingMode(){ return this.bindingMode; }
    public void setBindingMode(int bindingMode) { this.bindingMode =  bindingMode; }


    public String getRegistrationId() {
        return this.registrationId;
    }
    public void setRegistrationId(String registrationId){
        this.registrationId = registrationId;
    }

    @Column(name = "objectLinks", length = 1024)
    public String getObjectLinks() {
        return this.objectLinks;
    }
    public void setObjectLinks(String objectLinks){
        this.objectLinks = objectLinks;
    }

    public String getRootPath() {
        return this.rootPath;
    }
    public void setRootPath(String rootPath){
        this.rootPath = rootPath;
    }

    public Date getLastUpdate() {
        return this.lastUpdate;
    }
    public void setLastUpdate(Date lastUpdate){
        this.lastUpdate = lastUpdate;
    }

    public String getOrganizationID() {
        return this.organizationID;
    }
    public void setOrganizationID(String organizationID) {
        this.organizationID = organizationID;
    }

    public String getApplicationID() {
        return this.applicationID;
    }
    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getDevName(){
        return this.name;
    }
    public void setDevName(String name){
        this.name = name;
    }
}

