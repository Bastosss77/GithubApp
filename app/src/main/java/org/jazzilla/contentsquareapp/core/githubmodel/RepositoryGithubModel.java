package org.jazzilla.contentsquareapp.core.githubmodel;

import java.util.List;

public class RepositoryGithubModel {
    public String id;
    public UserGithubModel owner;
    public String name;
    public int forksCount;
    public int stargazersCount;
    public List<String> topics;
    public boolean fork;
    public RepositoryGithubModel parent;
    public String description;
    public String createdAt;
}
