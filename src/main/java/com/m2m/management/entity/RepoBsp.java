package com.m2m.management.entity;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name="repobsps",schema = "public")
public class RepoBsp implements Serializable {
    @Id
    @SequenceGenerator(name = "repobsps_rbid_seq", allocationSize = 1, initialValue = 1, sequenceName = "repobsps_rbid_seq")
    @GeneratedValue(generator = "repobsps_rbid_seq", strategy = GenerationType.SEQUENCE)
    @Column(unique = true, nullable = false)
    private long rbid;

//    @Column(nullable = false)
//    private String filename;

    @Column(nullable = false)
    private String boardname;

    @Column(nullable = false)
    private String versionname;

    @Column(nullable = false)
    private String address;

    private String description;

    @Transient
    private long rid;

    @ManyToOne
    @JoinColumn(name = "rid")
    private Repo repo;

    private RepoBsp(){};
    private RepoBsp(long rfid){
        this.rbid = rbid;
    }
    public RepoBsp(String versionname, String boardname){
        this.versionname = versionname;
        this.boardname = boardname;
    }


    public long getrbid(){
        return this.rbid;
    }
    private void setrbid(long rbid){
        this.rbid = rbid;
    }

//    public String getFilename(){
//        return this.filename;
//    }
//    public void setFilename(String filename){
//        this.filename = filename;
//    }

    public String getBoardname(){
        return this.boardname;
    }
    public void setBoardname(String boardname){
        this.boardname = boardname;
    }


    public String getVersionname(){
        return this.versionname;
    }
    public void setVersionname(String versionname){
        this.versionname = versionname;
    }

    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address = address;
    }

    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public Repo getRepo() {
        return this.repo;
    }
    public void setRepo(Repo repo) {
        this.repo = repo;
    }

    public long getRid(){
        return this.rid;
    }

}
