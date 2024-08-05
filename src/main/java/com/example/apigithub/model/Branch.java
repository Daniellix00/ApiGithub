package com.example.apigithub.model;

public class Branch {
    private String name;
    private String lastCommitSha;

    public String getName() {
        return name;
    }

    public String getLastCommitSha() {
        return lastCommitSha;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastCommitSha(String lastCommitSha) {
        this.lastCommitSha = lastCommitSha;
    }
}
