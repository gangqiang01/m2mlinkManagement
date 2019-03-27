package com.m2m.management.entity;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name="reppapps",schema = "public")
public class RepoApp implements Serializable {
    @Id
    @SequenceGenerator(name = "repoapps_raid_seq", allocationSize = 1, initialValue = 1, sequenceName = "repoapps_raid_seq")
    @GeneratedValue(generator = "repoapps_raid_seq", strategy = GenerationType.SEQUENCE)
    @Column(unique = true, nullable = false)
    private long raid;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String pkgname;

    @Column(nullable = false)
    private String versionname;

    private String versioncode;


    private long dlcount;

    private String license;

    private long installcount;

    private String description;

    private String summary;

    private String websit;

    @ManyToOne
    @JoinColumn(name = "rid")
    private Repo repo;

    private RepoApp(){};
    private RepoApp(long raid){
        this.raid = raid;
    }
    public RepoApp(String filename, String pkgname){
        this.filename = filename;
        this.pkgname = pkgname;
    }


    public long getraid(){
        return this.raid;
    }
    private void setraid(long raid){
        this.raid = raid;
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
