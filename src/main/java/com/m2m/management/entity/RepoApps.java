package com.m2m.management.entity;

import javax.persistence.*;

@Entity
@Table(name="repofiles",schema = "public")
public class RepoApps implements java.io.Serializable {
    @Id
    @SequenceGenerator(name = "repofiles_rfid_seq", allocationSize = 1, initialValue = 1, sequenceName = "repofiles_rfid_seq")
    @GeneratedValue(generator = "repofiles_rfid_seq", strategy = GenerationType.SEQUENCE)
    @Column(unique = true, nullable = false)
    private long rfid;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String pkgname;

    @Column(nullable = false)
    private String versioncode;

    @Column(nullable = false)
    private String versionname;

    private long dlcount;

    private String license;

    private long installcount;

    private String description;

    private String summary;

    private String websit;

    @ManyToOne
    @JoinColumn(name = "rid")
    private Repo repo;

    private RepoApps(){};
    private RepoApps(long rfid){
        this.rfid = rfid;
    }
    public RepoApps(String filename, String pkgname){
        this.filename = filename;
        this.pkgname = pkgname;
    }


    public long getrfid(){
        return this.rfid;
    }
    private void setrfid(long rfid){
        this.rfid = rfid;
    }

    public String getFilename(){
        return this.filename;
    }
    public void setFilename(String filename){
        this.filename = filename;
    }

    public String getPkgname(){
        return this.pkgname;
    }
    public void setPkgname(String pkgname){
        this.pkgname = pkgname;
    }


    public String getVersionname(){
        return this.versionname;
    }
    public void setVersionname(String versionname){
        this.versionname = versionname;
    }

    public String getVersioncode(){
        return this.versioncode;
    }
    public void setVersioncode(String versioncode){
        this.versioncode = versioncode;
    }


    public long getDlcount(){
        return this.dlcount;
    }
    public void setDlcount(long dlcount){
        this.dlcount = dlcount;
    }


    public String getLicense(){
        return this.license;
    }
    public void setLicense(String license){
        this.license = license;
    }


    public long getInstallcount(){
        return installcount;
    }
    public void setInstallcount(long installcount){
        this.installcount = installcount;
    }

    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }


    public String getSummary(){
        return this.summary;
    }
    public void setSummary(String summary){
        this.summary = summary;
    }


    public String getWebsit(){
        return this.websit;
    }
    public void setWebsit(String websit){
        this.websit = websit;
    }

    public Repo getRepo() {
        return this.repo;
    }
    public void setRepo(Repo repo) {
        this.repo = repo;
    }

}
