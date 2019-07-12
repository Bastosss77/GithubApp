package org.jazzilla.contentsquareapp.core.appmodel;

import android.net.Uri;

import org.jazzilla.contentsquareapp.core.githubmodel.UserGithubModel;

public class UserAppModel extends BaseAppModel<UserGithubModel> {
    public String name;
    public Uri avatarUri;

    @Override
    public UserAppModel from(UserGithubModel githubModel) {
        name = githubModel.login;
        avatarUri = Uri.parse(githubModel.avatarUrl);

        return this;
    }
}
