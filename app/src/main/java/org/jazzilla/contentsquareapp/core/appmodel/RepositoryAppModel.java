package org.jazzilla.contentsquareapp.core.appmodel;

import android.util.Log;

import org.jazzilla.contentsquareapp.common.ConstantsUtils;
import org.jazzilla.contentsquareapp.core.githubmodel.RepositoryGithubModel;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RepositoryAppModel extends BaseAppModel<RepositoryGithubModel> implements Serializable {
    public String id;
    public UserAppModel owner;
    public String name;
    public int forksCount;
    public int starsCount;
    public List<String> topics;
    public boolean isFork;
    public String parentName;
    public String description;
    public Date createdAt;

    @Override
    public RepositoryAppModel from(RepositoryGithubModel githubModel) {
        id = githubModel.id;
        owner = new UserAppModel().from(githubModel.owner);
        name = githubModel.name;
        forksCount = githubModel.forksCount;
        starsCount = githubModel.stargazersCount;
        topics = githubModel.topics;
        isFork = githubModel.fork;

        if(githubModel.parent != null) {
            parentName = githubModel.parent.owner.login + "/" + githubModel.parent.name;
        }

        description = githubModel.description;

        if(githubModel.createdAt != null) {
            //Date style : 2014-07-31T05:56:19Z
            SimpleDateFormat format = new SimpleDateFormat(ConstantsUtils.REPOSITORY_DATE_FORMAT);

            try {
                createdAt = format.parse(githubModel.createdAt);
            } catch (ParseException e) {
                Log.e("Parsing", "Cannot parse created_at repository field");
            }
        }

        return this;
    }
}
