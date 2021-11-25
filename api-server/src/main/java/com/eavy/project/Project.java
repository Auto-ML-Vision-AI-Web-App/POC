package com.eavy.project;

import com.eavy.account.Account;
import com.eavy.tag.Tag;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue
    Integer id;
    String name;
    LocalDate created = LocalDate.now();
    LocalDate lastModified = LocalDate.now();
    @ManyToOne
    Account account;
    @ManyToMany
    List<Tag> tags = new ArrayList<>();

    public Project() {
    }

    public void addTag(Tag tag) {
        this.getTags().add(tag);
        tag.getProjects().add(this);
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", createTime=" + created +
                ", lastModified=" + lastModified +
                ", account=" + account.getUserId() +
                '}';
    }

    public Project(String name) {
        this.name = name;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
