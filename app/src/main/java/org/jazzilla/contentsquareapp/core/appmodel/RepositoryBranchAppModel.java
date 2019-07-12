package org.jazzilla.contentsquareapp.core.appmodel;

import org.jazzilla.contentsquareapp.core.githubmodel.RepositoryBranchGithubModel;

public class RepositoryBranchAppModel extends BaseAppModel<RepositoryBranchGithubModel> {
    public String name;
    public boolean isProtected;

    @Override
    public RepositoryBranchAppModel from(RepositoryBranchGithubModel githubModel) {
        name = githubModel.name;
        isProtected = githubModel.isProtected;

        return this;
    }
}
