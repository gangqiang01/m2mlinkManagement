package com.m2m.management.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "repo",schema = "public")
public class Repo implements java.io.Serializable{
    @Id
    @SequenceGenerator(name = "repo_rid_seq", allocationSize = 1, initialValue = 1, sequenceName = "repo_rid_seq")
    @GeneratedValue(generator = "repo_rid_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "rid", unique = true, nullable = false)
    private long rid;

    @Column(nullable = false, unique = true)
    private String reponame;

    @Column(nullable = false)
    private String darkname;

    private String repotype;

    private String type;

    @Column(name="description", length = 32)
    private String description;

//    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @OneToMany(mappedBy = "repo", cascade={CascadeType.REMOVE})
    private Set<RepoApp> repoApps = new HashSet<RepoApp>(0);

    @Transient
    private long uid;
    private Repo(){

    }

    private Repo(long rid){
        this.rid = rid;
    }

    public Repo(String reponame, String description){
        this.reponame = reponame;
        this.description = description;
    }

    public Repo(String reponame, String description, User user){
        this.reponame = reponame;
        this.description = description;
        this.user = user;
    }


    public long getRid() {
        return this.rid;
    }
    private void setRid(long rid) {
        this.rid = rid;
    }


    public String getReponame(){
        return this.reponame;
    }
    public void setReponame(String reponame){
        this.reponame = reponame;
    }


    public String getType(){
        return this.type;
    }
    public void setType(String type){
        this.type = type;
    }

    public String getDarkname(){
        return this.darkname;
    }
    public void setDarkname(String darkname){
        this.darkname = darkname;
    }


    public String getRepoType(){
        return this.repotype;
    }
    public void setRepoType(String repotype){
        this.repotype = repotype;
    }

    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public User getUser(){
        return this.user;
    }
    public void setUser(User user){
        this.user = user;
    }

//    public Set<RepoApps> getRepoApps() {
//        return this.repoApps;
//    }
    public void setRepoApps(Set<RepoApp> repoApps) {
        this.repoApps = repoApps;
    }

    public long getUid(){
        return this.uid;
    }
}
