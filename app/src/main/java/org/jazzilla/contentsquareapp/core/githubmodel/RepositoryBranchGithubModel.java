package org.jazzilla.contentsquareapp.core.githubmodel;

import com.google.gson.annotations.SerializedName;

public class RepositoryBranchGithubModel {
    public String name;

    @SerializedName("protected")
    public boolean isProtected;
}
