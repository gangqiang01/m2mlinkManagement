package com.m2m.management.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "device_upgrade_status", schema = "public")
public class DeviceUpgradeStatus implements java.io.Serializable {

    private long dusid;
    private String deviceId;
    private String deployName;
    private String pkgType;
    private String os;
    private String arch;
    private String pkgName;
    private String version;
    private String extVersion;
    private int status;
    private int updstatus;
    private int errorCode;
    private Long ts;

    private DeviceUpgradeStatus() {
    }

    private DeviceUpgradeStatus(long dusid) {
        this.dusid = dusid;
    }

    public DeviceUpgradeStatus(String deviceId, String dpName, String pkgType, String version) {
        this.deviceId = deviceId;
        this.deployName = dpName;
        this.pkgType = pkgType;
        this.version = version;
    }

    private DeviceUpgradeStatus(long dusid, String deviceId, String pkgType, String pkgName, String version, String extVersion,
                                int status, Long ts) {
        this.dusid = dusid;
        this.deviceId = deviceId;
        this.pkgType = pkgType;
        this.pkgName = pkgName;
        this.version = version;
        this.extVersion = extVersion;
        this.status = status;
        this.ts = ts;
    }

    @Id
    @SequenceGenerator(name = "device_upgrade_status_upgid_seq", allocationSize = 1, initialValue = 1, sequenceName = "device_upgrade_status_upgid_seq")
    @GeneratedValue(generator = "device_upgrade_status_upgid_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "dusid", unique = true, nullable = false)
    public long getDusid() {
        return this.dusid;
    }

    private void setDusid(long dusid) {
        this.dusid = dusid;
    }

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "device_id")
    @Column(name = "device_id")
    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Column(name = "deploy_name", unique = true, length = 256)
    public String getDeployName() {
        return deployName;
    }

    public void setDeployName(String deployName) {
        this.deployName = deployName;
    }

    @Column(name = "pkg_type", length = 256)
    public String getPkgType() {
        return this.pkgType;
    }

    public void setPkgType(String pkgType) {
        this.pkgType = pkgType;
    }

    @Column(name = "ps", length = 256)
    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    @Column(name = "arch", length = 256)
    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    @Column(name = "pkg_name", length = 256)
    public String getPkgName() {
        return this.pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    @Column(name = "version", length = 256)
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Column(name = "ext_version", length = 256)
    public String getExtVersion() {
        return extVersion;
    }

    public void setExtVersion(String extVersion) {
        this.extVersion = extVersion;
    }

    @Column(name = "status")
    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "updstatus")
    public int getUpdstatus() {
        return updstatus;
    }

    public void setUpdstatus(int updstatus) {
        this.updstatus = updstatus;
    }

    @Column(name = "error_code")
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Column(name = "ts")
    public Long getTs() {
        return this.ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }


}